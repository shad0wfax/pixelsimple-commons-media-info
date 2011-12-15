/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.parser;

import com.pixelsimple.commons.command.CommandRequest;
import com.pixelsimple.commons.media.parser.ffmpeg.FfprobeOutputParser;

/**
 *
 * @author Akshay Sharma
 * Dec 11, 2011
 */
public class ParserFactory {
	
	public static Parser createParserForCommandRequest(CommandRequest commandRequest) {
		// TODO: Implement intelligence to return different parsers based on type of command request made. 
		
		return new FfprobeOutputParser();
	}

}
