/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media;

import com.pixelsimple.appcore.Resource;
import com.pixelsimple.appcore.media.MediaType;


/**
 *
 * @author Akshay Sharma
 * Dec 10, 2011
 */
public final class Photo extends MediaContainer {

	/**
	 * @param mediaResource
	 */
	public Photo(Resource mediaResource) {
		super(mediaResource);
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getMediaType()
	 */
	@Override
	public MediaType getMediaType() {
		return MediaType.PHOTO;
	}

}
