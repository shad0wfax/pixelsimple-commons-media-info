/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.probe.ffprobe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.command.CommandRequest;
import com.pixelsimple.commons.media.probe.MediaProbe;

/**
 *
 * @author Akshay Sharma
 * Dec 17, 2011
 */
public class FfprobeMediaProber implements MediaProbe {
	private static final Logger LOG = LoggerFactory.getLogger(FfprobeMediaProber.class);

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.inspect.MediaProbe#buildMediaProbeCommand()
	 */
	@Override
	public CommandRequest buildMediaProbeCommand(String filePathWithFileName) {
		String ffmpegPath = "Z:/VmShare/Win7x64/Technology/ffmpeg/release_0.8_love/ffmpeg-git-78accb8-win32-static/bin/";
		String command = ffmpegPath + "ffprobe -i " + filePathWithFileName + " -show_format -show_streams -sexagesimal ";
		
		LOG.debug("buildMediaProbeCommand::built command::{}", command);
		return new CommandRequest().addCommand(command, 0);
	}

}
