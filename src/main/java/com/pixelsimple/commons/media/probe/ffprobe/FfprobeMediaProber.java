/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.probe.ffprobe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.appcore.ApiConfig;
import com.pixelsimple.appcore.RegistryService;
import com.pixelsimple.commons.command.CommandRequest;
import com.pixelsimple.commons.media.probe.MediaProbe;

/**
 *
 * @author Akshay Sharma
 * Dec 17, 2011
 */
public final class FfprobeMediaProber implements MediaProbe {
	private static final Logger LOG = LoggerFactory.getLogger(FfprobeMediaProber.class);

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.inspect.MediaProbe#buildMediaProbeCommand()
	 */
	@Override
	public CommandRequest buildMediaProbeCommand(String filePathWithFileName) {
		ApiConfig apiConfig = RegistryService.getRegisteredApiConfig();
		String ffprobePath = apiConfig.getFfprobeConfig().getExecutablePath();
		
		String command = ffprobePath + " -i " + filePathWithFileName + " -show_format -show_streams -sexagesimal ";
		
		LOG.debug("buildMediaProbeCommand::built command::{}", command);
		return new CommandRequest().addCommand(command, 0);
	}

}
