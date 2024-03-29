/**
 * � PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.parser.ffprobe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.appcore.Resource;
import com.pixelsimple.appcore.media.StreamType;
import com.pixelsimple.commons.command.CommandRequest;
import com.pixelsimple.commons.command.CommandResponse;
import com.pixelsimple.commons.media.Audio;
import com.pixelsimple.commons.media.Container;
import com.pixelsimple.commons.media.MediaContainer;
import com.pixelsimple.commons.media.Photo;
import com.pixelsimple.commons.media.Stream;
import com.pixelsimple.commons.media.Video;
import com.pixelsimple.commons.media.exception.MediaException;
import com.pixelsimple.commons.media.parser.Parser;
import com.pixelsimple.commons.util.OSUtils;

/**
 *
 * @author Akshay Sharma
 * Dec 14, 2011
 */
public final class FfprobeOutputParser implements Parser {
	static final Logger LOG = LoggerFactory.getLogger(FfprobeOutputParser.class);
	
	private static final String FFPROBE_OUTPUT_STREAM_START_TAG = "[STREAM]";
	private static final String FFPROBE_OUTPUT_STREAM_END_TAG = "[/STREAM]";
	private static final String FFPROBE_OUTPUT_FORMAT_START_TAG = "[FORMAT]";
	private static final String FFPROBE_OUTPUT_FORMAT_END_TAG = "[/FORMAT]";
	private static final String FFPROBE_OUTPUT_STREAM_START_REGEX_PATTERN = "\\[STREAM\\]";
	private static final String FFPROBE_OUTPUT_STREAM_END_REGEX_PATTERN = "\\[\\/STREAM\\]";
	private static final String FFPROBE_OUTPUT_METADATA_START_TAG = "TAG:";
	
	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.parser.Parser#parseMediaInfo(com.pixelsimple.commons.command.CommandResponse)
	 */
	@Override
	public Container parseMediaInspectedData(Resource mediaResource, CommandRequest commandRequest, CommandResponse commandResponse)
			throws MediaException {
		return this.createMediaContainer(mediaResource, commandResponse);
	}

	private Container createMediaContainer(Resource mediaResource, CommandResponse commandResponse) throws MediaException {
		String output = commandResponse.getSuccessResponseOutputStream().toString();
		
		if (output == null || output.length() < 1 || (!output.contains(FFPROBE_OUTPUT_FORMAT_START_TAG)) 
				|| (!output.contains(FFPROBE_OUTPUT_STREAM_START_TAG))) {
			throw new MediaException("FFprobe did not provide a valid output");
		}
		
		String formatContent = output.substring(output.indexOf(FFPROBE_OUTPUT_FORMAT_START_TAG) + FFPROBE_OUTPUT_FORMAT_START_TAG.length(),
				output.indexOf(FFPROBE_OUTPUT_FORMAT_END_TAG));
		
		LOG.debug("createMediaContainer::formatContent::{}", formatContent);

		// TODO: This is extremely poor logic of getting the stream content, but maybe the best performing (?). Change after profiling.
		String streamData = output.substring(output.indexOf(FFPROBE_OUTPUT_STREAM_START_TAG) + FFPROBE_OUTPUT_STREAM_START_TAG.length(),
				output.lastIndexOf(FFPROBE_OUTPUT_STREAM_END_TAG));
		// Note the regex passed to the replaceAll (\[\/STREAM\]) - replace all the [/stream] with empty.
		String outputStripped = streamData.replaceAll(FFPROBE_OUTPUT_STREAM_END_REGEX_PATTERN, "");
		String [] streamContents = outputStripped.split(FFPROBE_OUTPUT_STREAM_START_REGEX_PATTERN);
		
		LOG.debug("createMediaContainer::streamContent::{}\n{}", streamContents.length, Arrays.toString(streamContents));
		
		return this.createContainerWithDetails(mediaResource, formatContent, streamContents); 
	}

	private MediaContainer createContainerWithDetails(Resource mediaResource, String formatContent, 
			String[] streamContents) throws MediaException {
		List<Map<String, String>> streamsAttributes = new ArrayList<Map<String,String>>();
		List<Map<String, String>> streamsMetadatas = new ArrayList<Map<String,String>>();
		Map<String , String> containerFormatAttributes = new HashMap<String, String>();
		Map<String , String> containerFormatMetadata = new HashMap<String, String>();
		this.populateAttributesAndMetadata(formatContent, containerFormatAttributes, containerFormatMetadata);
		
		for (String streamContent : streamContents) {
			Map<String, String> streamAttributes = new HashMap<String, String>();
			Map<String , String> streamMetadata = new HashMap<String, String>();
			this.populateAttributesAndMetadata(streamContent, streamAttributes, streamMetadata);
			streamsAttributes.add(streamAttributes);
			streamsMetadatas.add(streamMetadata);
		}
		MediaContainer container = this.createContainer(mediaResource, streamsAttributes, containerFormatAttributes);

		container.addContainerAttributes(containerFormatAttributes).addMetadata(containerFormatMetadata);
		
		for (int i = 0; i < streamsAttributes.size(); i++) {
			Map<String, String> streamsAttribute = streamsAttributes.get(i);
			Map<String, String> streamsMetadata = streamsMetadatas.get(i);
			
			// Can use audio or video codec_type here, as both are same.
			String type = streamsAttribute.get(Stream.AUDIO_STREAM_ATTRIBUTES.codec_type.name());
			LOG.debug("createContainerWithDetails::type::{}", type);

			try {
				StreamType streamType = StreamType.valueOf(type.toUpperCase());
				container.addStreams(streamType, streamsAttribute, streamsMetadata);
			} catch (Exception e) {
				// In case a stream type is not in the listed StreamType class, an exception is thrown. We will ignore it.
				LOG.error("createContainerWithDetails:: Found an unknown stream. Ignoring it{}", e);			
			}
		}
		LOG.debug("createContainerWithDetails::container::{}", container);
		
		return container;
	}

	private MediaContainer createContainer(Resource mediaResource, List<Map<String, String>> streamsAttributes, 
			Map<String , String> containerFormatAttributes) throws MediaException {
		MediaContainer container = null;
		boolean isVideoStream = false;
		boolean isAudioStream = false;
		String formatName = containerFormatAttributes.get(Container.CONTAINER_FORMAT_ATTRIBUTES.format_name.name());

		LOG.debug("createContainer::formatName::{}", formatName);
		
		if (formatName == null)
			formatName = "";
		
		for (Map<String, String> stream : streamsAttributes) {
			// Can use audio or video codec_type here, as both are same.
			String type = stream.get(Stream.AUDIO_STREAM_ATTRIBUTES.codec_type.name());
			
			// It appears that container format contains video as codec_type but format name starts with image (ex: image2).			
			if ("video".equalsIgnoreCase(type) && !formatName.startsWith("image")) {
				isVideoStream = true;
			}
			
			if ("audio".equalsIgnoreCase(type)) {
				isAudioStream = true;
			}
			// TODO: add other streams supported
		}
		
		if (isVideoStream) {
			container = new Video(mediaResource);
		} else if (isAudioStream && !isVideoStream) {
			container = new Audio(mediaResource);
		} else if (!isVideoStream && !isAudioStream) {
			// TODO: Is it photo or video without video stream!? Need better algo for sure
			container = new Photo(mediaResource);
		}

		// Old Algo: 
//		// TODO: Junk logic maybe. Proof check and see how best to make this better.
//		if (streamsAttributes.size() == 2) {
//			container = new Video();
//		} else if (streamsAttributes.size() == 1) {
//			String codecType = streamsAttributes.get(0).get(Stream.AUDIO_STREAM_ATTRIBUTES.codec_type.name());
//			
//			if (codecType.equalsIgnoreCase("audio")) {
//				container = new Audio();
//			} else {
//				// TODO: Is it photo or video without audio? Need better algo for sure
//				container = new Photo();
//			}
//		}
		
		if (container == null) {
			throw new MediaException("Could not create a container. No streamContent was found in ::" + streamsAttributes);
		}
		LOG.debug("createContainer::created container::{}", container);

		return container;
	}
	
	/**
	 * Convert an input of type: 
	 * 	key1DELIMvalue1
	 *  key2DELIMvalue2
	 *  ..
	 *  
	 *  into a map with keys as key1, key2 ... and values as value1, value2 ...
	 * 
	 * @param keyValueStrings
	 * @return
	 */
	private void populateAttributesAndMetadata(String keyValueStrings, 
			Map<String, String> attributes, Map<String, String> metadata) {
		String [] keyValueLine = keyValueStrings.split(OSUtils.NEW_LINE_CHARACTER);
		
		for (String keyValue : keyValueLine) {
			int equalIndex = keyValue.indexOf("=");
			
			if (equalIndex != -1) {
				String key = keyValue.substring(0, equalIndex);
				String value = keyValue.substring(equalIndex + 1);
				
				if (key.startsWith(FFPROBE_OUTPUT_METADATA_START_TAG)) {
					key = key.substring(FFPROBE_OUTPUT_METADATA_START_TAG.length());
					metadata.put(key, value);
				} else {
					attributes.put(key, value);
				}
			}
		}
		LOG.debug("convertMultiLineDelimitedStringsToMap::attributes::{}::\n metadata::{}", attributes, metadata);
	}
}
