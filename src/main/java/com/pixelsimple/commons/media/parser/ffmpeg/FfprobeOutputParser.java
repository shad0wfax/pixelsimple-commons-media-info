/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.parser.ffmpeg;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.pixelsimple.commons.media.exception.MediaException;
import com.pixelsimple.commons.media.parser.Parser;
import com.pixelsimple.commons.util.CommonsUtil;

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
	private static final String FFPROBE_OUTPUT_STREAM_START_REGEX_PATTERN = "\\[STREAM\\]";
	private static final String FFPROBE_OUTPUT_STREAM_END_REGEX_PATTERN = "\\[\\/STREAM\\]";
	
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
		throw new MediaException("FFprobe does not support transcoding. Use ffmpeg instead. Use FFmpegOutputParser.");
	}

	private Container createMediaContainer(CommandResponse commandResponse) {
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
		
		return this.createContainerWithDetails(formatContent, streamContents); 
	}

	private MediaContainer createContainerWithDetails(String formatContent, String[] streamContents) {
		Map<String , String> containerAttributesFormatAttributes = CommonsUtil.convertMultiLineDelimitedStringsToMap(
				formatContent, "=");
		List<Map<String, String>> streamsAttributes = new ArrayList<Map<String,String>>();
		
		for (String streamContent : streamContents) {
			 Map<String, String> streamAttributes = CommonsUtil.convertMultiLineDelimitedStringsToMap(streamContent, "=");
			streamsAttributes.add(streamAttributes);
		}
		MediaContainer container = this.createContainer(streamsAttributes);
		container.addContainerAttributes(containerAttributesFormatAttributes);
		
		for (Map<String, String> streamsAttribute : streamsAttributes) {
			// Can use audio or video codec_type here, as both are same.
			String type = streamsAttribute.get(Container.CONTAINER_AUDIO_STREAM_ATTRIBUTES.codec_type.name());
			LOG.debug("createContainerWithDetails::type::{}", type);

			StreamType streamType = Container.StreamType.valueOf(type.toUpperCase());
			container.addStreams(streamType, streamsAttribute);
		}
		LOG.debug("createContainerWithDetails::container::{}", container);
		
		return container;
	}

	private MediaContainer createContainer(List<Map<String, String>> streamsAttributes) {
		MediaContainer container = null;
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
		}
		if (container == null) {
			throw new MediaException("Could not create a container. No streamContent was found in ::" + streamsAttributes);
		}
		LOG.debug("createContainer::created container::{}", container);

		return container;
	}
	
}
