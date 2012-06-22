/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.parser;

import com.pixelsimple.appcore.Resource;
import com.pixelsimple.commons.command.CommandRequest;
import com.pixelsimple.commons.command.CommandResponse;
import com.pixelsimple.commons.media.Container;
import com.pixelsimple.commons.media.exception.MediaException;

/**
 *
 * @author Akshay Sharma
 * Dec 11, 2011
 */
public interface Parser {

	public Container parseMediaInspectedData(Resource mediaResource, CommandRequest commandRequest, CommandResponse commandResponse)
		throws MediaException;
	
}
