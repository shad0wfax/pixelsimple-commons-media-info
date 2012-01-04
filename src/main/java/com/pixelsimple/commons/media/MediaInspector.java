/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.CommandRequest;
import com.pixelsimple.commons.command.CommandResponse;
import com.pixelsimple.commons.command.CommandRunner;
import com.pixelsimple.commons.command.CommandRunnerFactory;
import com.pixelsimple.commons.media.Container.StreamType;
import com.pixelsimple.commons.media.parser.Parser;
import com.pixelsimple.commons.media.parser.ParserFactory;
import com.pixelsimple.commons.media.probe.MediaProbe;
import com.pixelsimple.commons.media.probe.MediaProbeFactory;

/**
 *
 * @author Akshay Sharma
 * Dec 10, 2011
 */
public final class MediaInspector {
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
		Map<StreamType, Stream> streams = container.getStreams();
		
		LOG.debug("readContainerInfo::file size and bitrate are ::{}, and {} bits per second", container.getFileSize(), 
				container.getBitRateBps());
		
		Iterator<Map.Entry<StreamType, Stream>> it = streams.entrySet().iterator(); 
		while (it.hasNext()) {
			Map.Entry<StreamType, Stream> entry = it.next(); 
			Stream stream = entry.getValue();

			LOG.debug("readContainerInfo::stream info::Stream type::{}, and codec used is::{} ", stream.getStreamType(),
				((Container.StreamType.VIDEO == stream.getStreamType()) ? stream.getStreamAttribute(Stream.VIDEO_STREAM_ATTRIBUTES.codec_name)
						: stream.getStreamAttribute(Stream.AUDIO_STREAM_ATTRIBUTES.codec_name)));
		}
	}
}
