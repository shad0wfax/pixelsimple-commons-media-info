/**
 * � PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media;

/**
 *
 * @author Akshay Sharma
 * Dec 9, 2011
 */
public interface Container {
	
	public static final String MEDIA_TYPE_VIDEO = "VIDEO";
	public static final String MEDIA_TYPE_AUDIO = "AUDIO";
	public static final String MEDIA_TYPE_PHOTO = "PHOTO";
	
	String getMediaType();

}