/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media;

import java.util.Map;

import com.pixelsimple.appcore.media.MediaType;
import com.pixelsimple.appcore.media.StreamType;

/**
 * TODO: Figure out the return types etc eventually 
 * 
 * @author Akshay Sharma
 * Dec 9, 2011
 */
public interface Container {
	
	// These container attributes are based off of FFProbe. Ensure other libraries used to inspect media data adheres to these attribute names.
	public static enum CONTAINER_FORMAT_ATTRIBUTES {filename, nb_streams, format_name, format_long_name, start_time, duration, size, bit_rate, metadata};
	
	MediaType getMediaType();
	
	Map<StreamType, Stream> getStreams();
	
	int getStreamCount();
	
	String getBitRateBps();
	
	String getDuration();

	String getFileSize();

	String getFilePathWithName();

	String getContainerFormat();

	Map<String, String> getMetaData();
	
	String getContainerAttribute(CONTAINER_FORMAT_ATTRIBUTES attribute);
	
}
