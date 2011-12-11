/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.CommandResponse;
import com.pixelsimple.commons.media.Container;

/**
 *
 * @author Akshay Sharma
 * Dec 10, 2011
 */
public class FfmpegOutputParser {
	static final Logger LOG = LoggerFactory.getLogger(FfmpegOutputParser.class);

	public Container parseMediaInfo(CommandResponse commandResponse) {
		this.createContainer(commandResponse);
		
		return null;
		
	}
	
	public Container parseTranscodingInfo(CommandResponse commandResponse) {
		// TODO: Implement it
		return null;
		
	}
	
	private void createContainer(CommandResponse commandResponse) {
		String output = commandResponse.getSuccessResponse().toString();
		
		//String text = "Duration: 00:05:25.00, start: 0.000000, bitrate: 554 kb/s";
		
		final Pattern p = Pattern.compile(".?bitrate:\\s([0-9]*)\\skb/s");
		final Matcher m = p.matcher(output);
		
		if(m.find())
		{
			LOG.debug("createContainer::bitrate extracted::{}", m.group(1));
		}		
	}
	

}
