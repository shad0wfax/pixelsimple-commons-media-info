/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.parser.ffmpeg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.CommandResponse;
import com.pixelsimple.commons.media.Audio;
import com.pixelsimple.commons.media.Container;
import com.pixelsimple.commons.media.Container.StreamType;
import com.pixelsimple.commons.media.MediaContainer;
import com.pixelsimple.commons.media.Photo;
import com.pixelsimple.commons.media.Video;
import com.pixelsimple.commons.media.parser.Parser;

/**
 *
 * @author Akshay Sharma
 * Dec 14, 2011
 */
public class FfprobeOutputParser implements Parser {
	static final Logger LOG = LoggerFactory.getLogger(FfprobeOutputParser.class);
	
	private static final String FFPROBE_OUTPUT_STREAM_START_TAG = "[STREAM]";
	private static final String FFPROBE_OUTPUT_STREAM_END_TAG = "[/STREAM]";
	private static final String FFPROBE_OUTPUT_FORMAT_START_TAG = "[FORMAT]";
	private static final String FFPROBE_OUTPUT_FORMAT_END_TAG = "[/FORMAT]";
	private static final String NEW_LINE_CHARACTER = System.getProperty("line.separator");

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.parser.Parser#parseMediaInfo(com.pixelsimple.commons.command.CommandResponse)
	 */
	@Override
	public Container parseMediaInfo(CommandResponse commandResponse) {
		return this.createMediaContainer(commandResponse);
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.parser.Parser#parseTranscodingInfo(com.pixelsimple.commons.command.CommandResponse)
	 */
	@Override
	public Container parseTranscodingInfo(CommandResponse commandResponse) {
		throw new IllegalStateException("FFprobe does not support transcoding. Use ffmpeg instead. Use FFmpegOutputParser.");
	}

	private Container createMediaContainer(CommandResponse commandResponse) {
		String output = commandResponse.getSuccessResponseOutputStream().toString();
		
		if (output == null || output.length() < 1 || (!output.contains(FFPROBE_OUTPUT_FORMAT_START_TAG)) 
				|| (!output.contains(FFPROBE_OUTPUT_STREAM_START_TAG))) {
			throw new IllegalStateException("FFprobe did not provide a valid output");
		}
		
		String formatContent = output.substring(output.indexOf(FFPROBE_OUTPUT_FORMAT_START_TAG) + FFPROBE_OUTPUT_FORMAT_START_TAG.length(),
				output.indexOf(FFPROBE_OUTPUT_FORMAT_END_TAG));
		
		LOG.debug("createMediaContainer::formatContent::{}", formatContent);

		// TODO: This is extremely poor logic of getting the stream content, but maybe the best performing (?). Change after profiling.
		String streamData = output.substring(output.indexOf(FFPROBE_OUTPUT_STREAM_START_TAG) + FFPROBE_OUTPUT_STREAM_START_TAG.length(),
				output.lastIndexOf(FFPROBE_OUTPUT_STREAM_END_TAG));
		// Note the regex passed to the replaceAll (\[)
		String outputStripped = streamData.replaceAll("\\[\\/STREAM\\]", "");
		String [] streamContents = outputStripped.split("\\[STREAM\\]");
		
		LOG.debug("createMediaContainer::streamContent::{}\n{}", streamContents.length, Arrays.toString(streamContents));
		
		MediaContainer container = this.createContainerType(formatContent, streamContents); 
		return container;
	}

	private MediaContainer createContainerType(String formatContent, String[] streamContents) {
		MediaContainer container = null;
		Map<String , String> containerAttributesFormatAttributes = this.convertToMap(formatContent);
		List<Map<String, String>> streamsAttributes = new ArrayList<Map<String,String>>();
		
		for (String streamContent : streamContents) {
			streamsAttributes.add(this.convertToMap(streamContent));
		}

		// TODO: Junk logic maybe. Proof check and see how best to make this better.
		if (streamsAttributes.size() == 2) {
			container = new Video();
		} else if (streamsAttributes.size() == 1) {
			String codecType = streamsAttributes.get(0).get(Container.CONTAINER_AUDIO_STREAM_ATTRIBUTES.codec_type.name());
			
			if (codecType.equalsIgnoreCase("audio")) {
				container = new Audio();
			} else {
				// TODO: Is it photo or video without audio? Need better algo for sure
				container = new Photo();
			}
		} else {
			// TODO: what?? - default to video for now
			container = new Video();
		}
		container.setContainerAttributes(containerAttributesFormatAttributes);
		
		for (Map<String, String> streamsAttribute : streamsAttributes) {
			// Can use audio or video codec_type here, as both are same.
			String type = streamsAttribute.get(Container.CONTAINER_AUDIO_STREAM_ATTRIBUTES.codec_type.name());
			LOG.debug("createContainerType::type::{}", type);

			StreamType streamType = Container.StreamType.valueOf(type.toUpperCase());
			container.addStreams(streamType, streamsAttribute);
		}
		LOG.debug("createContainerType::container::{}", container);
		
		return container;
	}
	
	private Map<String, String> convertToMap(String keyValueStrings) {
		Map<String, String> keyValues = new HashMap<String, String>();
		String [] keyValueLine = keyValueStrings.split(NEW_LINE_CHARACTER);
		
		for (String keyValue : keyValueLine) {
			int equalIndex = keyValue.indexOf("=");
			
			if (equalIndex != -1) {
				String key = keyValue.substring(0, equalIndex);
				String value = keyValue.substring(equalIndex + 1);
				keyValues.put(key, value);
			}
		}
		LOG.debug("convertToMap::keyValues::{} ", keyValues);
		
		return keyValues;
	}
		
}
