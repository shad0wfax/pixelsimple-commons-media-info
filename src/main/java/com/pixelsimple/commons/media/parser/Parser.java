/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.parser;

import com.pixelsimple.commons.command.CommandResponse;
import com.pixelsimple.commons.media.Container;

/**
 *
 * @author Akshay Sharma
 * Dec 11, 2011
 */
public interface Parser {

	public Container parseMediaInfo(CommandResponse commandResponse);
	
	public Container parseTranscodingInfo(CommandResponse commandResponse);
	
}
