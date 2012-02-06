/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media;

import com.pixelsimple.appcore.media.MediaType;


/**
 *
 * @author Akshay Sharma
 * Dec 9, 2011
 */
public final class Audio extends MediaContainer {

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getMediaType()
	 */
	@Override
	public MediaType getMediaType() {
		return MediaType.AUDIO;
	}

}
