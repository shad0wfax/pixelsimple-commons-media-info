/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.parser.ffmpeg;

import com.pixelsimple.commons.command.CommandResponse;
import com.pixelsimple.commons.media.Container;
import com.pixelsimple.commons.media.parser.Parser;

/**
 *
 * @author Akshay Sharma
 * Dec 14, 2011
 */
public class FfprobeOutputParser implements Parser {

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.parser.Parser#parseMediaInfo(com.pixelsimple.commons.command.CommandResponse)
	 */
	@Override
	public Container parseMediaInfo(CommandResponse commandResponse) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.parser.Parser#parseTranscodingInfo(com.pixelsimple.commons.command.CommandResponse)
	 */
	@Override
	public Container parseTranscodingInfo(CommandResponse commandResponse) {
		throw new IllegalStateException("FFprobe does not support transcoding. Use ffmpeg instead. Use FFmpegOutputParser.");
	}

}
