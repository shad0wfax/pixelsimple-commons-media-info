/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media;

/**
 *
 * @author Akshay Sharma
 * Dec 10, 2011
 */
public class Photo implements Container {

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getMediaType()
	 */
	@Override
	public String getMediaType() {
		return Container.MEDIA_TYPE_PHOTO;
	}

}
