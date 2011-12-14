/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media;



/**
 *
 * @author Akshay Sharma
 * Dec 9, 2011
 */
public class Video extends MediaContainer {

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getMediaType()
	 */
	@Override
	public MediaType getMediaType() {
		return Container.MediaType.VIDEO;
	}

}
