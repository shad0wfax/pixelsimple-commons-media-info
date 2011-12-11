/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.CommandRequest;
import com.pixelsimple.commons.command.CommandResponse;
import com.pixelsimple.commons.command.CommandRunner;
import com.pixelsimple.commons.command.CommandRunnerFactory;
import com.pixelsimple.commons.media.parser.Parser;
import com.pixelsimple.commons.media.parser.ParserFactory;

/**
 *
 * @author Akshay Sharma
 * Dec 10, 2011
 */
public class MediaContainer {
	static final Logger LOG = LoggerFactory.getLogger(MediaContainer.class);
	
	private MediaContainer() {}
	public static void readContainerInfo(String filePathWithFileName) {
		
		CommandRequest commandRequest = buildCommand(filePathWithFileName);
		// Use the blocking command since this is a fast call
		CommandRunner runner = CommandRunnerFactory.newBlockingCommandRunner();
		CommandResponse commandResponse = new CommandResponse();
		
		// Will block and run and fill the response out.
		runner.runCommand(commandRequest, commandResponse);
		
		LOG.debug("readContainerInfo::commandResponse::{}", commandResponse);
		
		Parser parser = ParserFactory.createParserForCommandRequest(commandRequest);
		Container container = parser.parseMediaInfo(commandResponse);
	}
	
	/**
	 * @param params
	 * @return
	 */
	private static CommandRequest buildCommand(String filePathWithFileName) {
		String ffmpegPath = "Z:/VmShare/Win7x64/Technology/ffmpeg/release_0.8_love/ffmpeg-git-78accb8-win32-static/bin/";
		
		String command = ffmpegPath + "ffmpeg -i " + filePathWithFileName;
		
		LOG.debug("buildCommand::built command::{}", command);
		
		return new CommandRequest().addCommand(command, 1);
	}


}
