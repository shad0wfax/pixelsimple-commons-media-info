/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.parser.ffmpeg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.CommandResponse;
import com.pixelsimple.commons.media.Container;
import com.pixelsimple.commons.media.parser.Parser;

/**
 *
 * @author Akshay Sharma
 * Dec 10, 2011
 */
public class FfmpegOutputParser implements Parser {
	static final Logger LOG = LoggerFactory.getLogger(FfmpegOutputParser.class);
	
	// TODO: Externalize these patterns! 
	private static final Pattern BITRATE_PATTERN = Pattern.compile(".?bitrate:\\s([0-9]*)\\skb/s", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	private static final Pattern DURATION_PATTERN = Pattern.compile(".?Duration:[\\s]*([^,]*),", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);


	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.parser.Parser#parseMediaInfo(com.pixelsimple.commons.command.CommandResponse)
	 */
	@Override
	public Container parseMediaInfo(CommandResponse commandResponse) {
		this.createContainer(commandResponse);
		
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.parser.Parser#parseTranscodingInfo(com.pixelsimple.commons.command.CommandResponse)
	 */
	@Override
	public Container parseTranscodingInfo(CommandResponse commandResponse) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void createContainer(CommandResponse commandResponse) {
		String output = commandResponse.getSuccessResponse().toString();
		
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
	}


}
