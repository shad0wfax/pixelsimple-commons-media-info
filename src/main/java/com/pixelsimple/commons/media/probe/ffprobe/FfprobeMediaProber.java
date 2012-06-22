/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.probe.ffprobe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.appcore.ApiConfig;
import com.pixelsimple.appcore.Resource;
import com.pixelsimple.appcore.registry.RegistryService;
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
	public CommandRequest buildMediaProbeCommand(Resource mediaResource) {
		ApiConfig apiConfig = RegistryService.getRegisteredApiConfig();
		String ffprobePath = apiConfig.getFfprobeConfig().getExecutablePath();
		
		CommandRequest request = new CommandRequest();
		request.addCommand(ffprobePath, 0);
		
		// Note: not validating the resource, (calling isValid()), since it can avoid an expensive call. 
		// If needed we can add it, but since ffmpeg returns an error anyway, for now ignoring it.
		String mediaPath = mediaResource.getResourceAsString();
		
		request.addArgument("-i").addArgument(mediaPath).addArgument("-show_format")
			.addArgument("-show_streams").addArgument("-sexagesimal");
		
		LOG.debug("buildMediaProbeCommand::built command::{}", request.getCommandAsString());
		return request;
	}

}
