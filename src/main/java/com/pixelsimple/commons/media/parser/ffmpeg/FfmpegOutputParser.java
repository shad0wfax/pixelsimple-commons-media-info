/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.parser.ffmpeg;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.CommandResponse;
import com.pixelsimple.commons.media.Audio;
import com.pixelsimple.commons.media.Container;
import com.pixelsimple.commons.media.MediaContainer;
import com.pixelsimple.commons.media.Photo;
import com.pixelsimple.commons.media.Video;
import com.pixelsimple.commons.media.parser.Parser;

/**
 *
 * @author Akshay Sharma
 * Dec 10, 2011
 */
public class FfmpegOutputParser implements Parser {
	static final Logger LOG = LoggerFactory.getLogger(FfmpegOutputParser.class);
	static final String FFMPEG_STREAM_VIDEO = "video:";
	static final String FFMPEG_STREAM_AUDIO = "audio:";
	
	// TODO: Externalize these patterns! 
	// Find the word bitrate followed by colon(:), followed by a whitespace, followed by any combination [0-9] followed by space and then lastly by kb/s
	private static final Pattern BITRATE_PATTERN = Pattern.compile(".?bitrate:\\s([0-9]*)\\skb/s", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	
	// Find the word Duration followed by :, followed by any number of whitespaces, followed by any number of characters that is not a comma(,), followed by a comma
	private static final Pattern DURATION_PATTERN = Pattern.compile(".?Duration:[\\s]*([^,]*),", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);

	// Find the word Stream followed by :, followed by any number of any characters that is not a #, followed by a #, followed by [09].[0-9], 
	// followed by any number of any characters that is not a colon, followed by a colon. followed again by any number of any characters that is not a colon
	private static final Pattern STREAM_COUNT_PATTERN = Pattern.compile(".?Stream[^#]*#[0-9].[0-9][^:]*:[^:]*[^\n]*", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	//..?Stream(.#[0-9].[0-9]:[^:]+)
	// .?Stream(.#[0-9].[0-9].*[:])
	// .?Stream(.#[0-9].[0-9][.*]*?)


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
		// TODO Auto-generated method stub
		return null;
	}
	
	private Container createMediaContainer(CommandResponse commandResponse) {
		String output = commandResponse.getSuccessResponse().toString();
		MediaContainer container = this.createContainerType(output); 
		
		//String text = "Duration: 00:05:25.00, start: 0.000000, bitrate: 554 kb/s";
		String bitRate = null;
		String duration = null;
		
		Matcher m = BITRATE_PATTERN.matcher(output);
		if(m.find())
		{
			bitRate = m.group(1);
		}		
		LOG.debug("createContainer::bitrate extracted::{}", bitRate);
		
		m = DURATION_PATTERN.matcher(output);
		if(m.find())
		{
			duration = m.group(1);
		}		
		LOG.debug("createContainer::duration extracted::{}", duration);

		// Set the container data
		container.setBitRate(bitRate).setDuration(duration);
		
		return container;
	}
	
	// TODO: Refine this algo
	private MediaContainer createContainerType(String output) {
		int streamCount = 0;
		MediaContainer container = null;
		Map<Container.StreamType, String> streams = new HashMap<Container.StreamType, String>(4);
		Matcher m = STREAM_COUNT_PATTERN.matcher(output);
		
		while (m.find()) {
			++streamCount;
			String stream = m.group();
			
			if (stream.toLowerCase().contains(FFMPEG_STREAM_VIDEO)) {
				streams.put(Container.StreamType.VIDEO, stream);
			} else if (stream.toLowerCase().contains(FFMPEG_STREAM_AUDIO)) {
				streams.put(Container.StreamType.AUDIO, stream);
			}
			LOG.debug("createContainer::streams found::{}", stream);
		}
		LOG.debug("createContainer::number of streams found::{}", streamCount);
		
		// Junk logic maybe. Proof check and see how best to make this better.
		if (streams.size() == 2) {
			container = new Video();
		} else if (streams.size() == 1) {
			
			if (streams.containsKey(Container.StreamType.AUDIO)) {
				container = new Audio();
			} else if (streams.containsKey(Container.StreamType.VIDEO)) {
				// Is it photo or video? Need better algo
				container = new Photo();
			}
		} else {
			// TODO: what?? - default to video for now
			container = new Video();
		}
		container.addStreams(streams);
		
		return container;
	}


}
