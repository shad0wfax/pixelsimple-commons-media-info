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
import com.pixelsimple.commons.media.probe.MediaProbe;
import com.pixelsimple.commons.media.probe.MediaProbeFactory;

/**
 *
 * @author Akshay Sharma
 * Dec 10, 2011
 */
public class MediaInspector {
	private static final Logger LOG = LoggerFactory.getLogger(MediaInspector.class);
	
	private MediaInspector() {}
	
	public static void readContainerInfo(String filePathWithFileName) {
		MediaProbe probe = MediaProbeFactory.createMediaProbe();
		CommandRequest commandRequest = probe.buildMediaProbeCommand(filePathWithFileName);
		
		// Use the blocking command since this is a fast call
		CommandRunner runner = CommandRunnerFactory.newBlockingCommandRunner();
		CommandResponse commandResponse = new CommandResponse();
		
		// Will block and run and fill the response out.
		runner.runCommand(commandRequest, commandResponse);
		
		LOG.debug("readContainerInfo::commandResponse::{}", commandResponse);
		
		Parser parser = ParserFactory.createParserForCommandRequest(commandRequest);
		Container container = parser.parseMediaInspectedData(null, commandResponse);

		LOG.debug("readContainerInfo::file size and bitrate are ::{}, and {} bits per second", container.getFileSize(), 
				container.getBitRateBps());
	}
}
