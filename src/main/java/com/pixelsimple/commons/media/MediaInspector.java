/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media;

import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.appcore.media.StreamType;
import com.pixelsimple.commons.command.CommandRequest;
import com.pixelsimple.commons.command.CommandResponse;
import com.pixelsimple.commons.command.CommandRunner;
import com.pixelsimple.commons.command.CommandRunnerFactory;
import com.pixelsimple.commons.media.exception.MediaException;
import com.pixelsimple.commons.media.parser.Parser;
import com.pixelsimple.commons.media.parser.ParserFactory;
import com.pixelsimple.commons.media.probe.MediaProbe;
import com.pixelsimple.commons.media.probe.MediaProbeFactory;

/**
 * NOT THREAD SAFE right now (because of the mediaprobe/commandrunner initialization via getters). Invoke one per thread.
 * 
 * @author Akshay Sharma
 * Dec 10, 2011
 */
public final class MediaInspector {
	private static final Logger LOG = LoggerFactory.getLogger(MediaInspector.class);

	// Using instance attributes so that they can be injected at some point.
	private MediaProbe mediaProbe;
	private CommandRunner commandRunner;
	
	public MediaInspector() {}
	
	public Container createMediaContainer(String filePathWithFileName) throws MediaException {
		CommandRequest commandRequest = this.getMediaProbe().buildMediaProbeCommand(filePathWithFileName);
		CommandResponse commandResponse = new CommandResponse();
		
		// Will block and run and fill the response out.
		this.getCommandRunner().runCommand(commandRequest, commandResponse);
		
		LOG.debug("readContainerInfo::commandResponse::{}", commandResponse);
		
		if (commandResponse.hasCommandFailed()) {
			throw new MediaException("Inspecting the media failed. The associated error message is: \n" 
					+ commandResponse.getFailureResponse(), commandResponse.getFailureCause());
		}
		
		Parser parser = ParserFactory.createParserForCommandRequest(commandRequest);
		Container container = parser.parseMediaInspectedData(commandRequest, commandResponse);

		if (LOG.isDebugEnabled()) {
			Map<StreamType, Stream> streams = container.getStreams();
			LOG.debug("readContainerInfo::file format:: {}", container.getContainerFormat());
			LOG.debug("readContainerInfo::file size and bitrate are ::{}, and {} bits per second", container.getFileSize(), 
					container.getBitRateBps());
			
			Iterator<Map.Entry<StreamType, Stream>> it = streams.entrySet().iterator(); 
			while (it.hasNext()) {
				Map.Entry<StreamType, Stream> entry = it.next(); 
				Stream stream = entry.getValue();
	
				LOG.debug("readContainerInfo::stream info::Stream type::{}, and codec used is::{} ", stream.getStreamType(),
					((StreamType.VIDEO == stream.getStreamType()) ? stream.getStreamAttribute(Stream.VIDEO_STREAM_ATTRIBUTES.codec_name)
							: stream.getStreamAttribute(Stream.AUDIO_STREAM_ATTRIBUTES.codec_name)));
			}
		}
		
		return container;
	}

	/**
	 * @return the mediaProbe
	 */
	public MediaProbe getMediaProbe() {
		if (this.mediaProbe == null) {
			this.mediaProbe = MediaProbeFactory.createMediaProbe();	
		}
		
		return this.mediaProbe;
	}

	/**
	 * @return the commandRunner
	 */
	public CommandRunner getCommandRunner() {
		if (this.commandRunner == null) {
			// Use the blocking command since this is a fast call
			this.commandRunner = CommandRunnerFactory.newBlockingCommandRunner();
		}
		return this.commandRunner;
	}

}
