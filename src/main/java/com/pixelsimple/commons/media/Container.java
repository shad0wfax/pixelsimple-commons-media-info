/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media;

import java.util.Map;

/**
 * TODO: Figure out the return types etc eventually 
 * 
 * @author Akshay Sharma
 * Dec 9, 2011
 */
public interface Container {
	
	public static enum MediaType {VIDEO, AUDIO, PHOTO};
	public static enum StreamType {VIDEO, AUDIO};
	
	MediaType getMediaType();
	
	Map<Container.StreamType, String> getStreams();
	
	int getStreamCount();
	
	String getBitRateKbps();
	
	String getDuration();

	String getFileSize();

	String getMetaData();
	
}
