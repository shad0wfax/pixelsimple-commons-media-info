/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.probe;

import com.pixelsimple.appcore.Resource;
import com.pixelsimple.commons.command.CommandRequest;

/**
 *
 * @author Akshay Sharma
 * Dec 17, 2011
 */
public interface MediaProbe {

	CommandRequest buildMediaProbeCommand(Resource mediaResource);
	
}
