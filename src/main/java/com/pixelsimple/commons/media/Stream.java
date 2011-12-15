/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media;

import java.util.Map;

/**
 * 
 *
 * @author Akshay Sharma
 * Dec 16, 2011
 */
public class Stream {
	private Map<String, String> streamAttributes;
	private Container.StreamType streamType;
	
	/**
	 * @return the streamType
	 */
	public Container.StreamType getStreamType() {
		return streamType;
	}

	/**
	 * @param streamType the streamType to set
	 */
	public void setStreamType(Container.StreamType streamType) {
		this.streamType = streamType;
	}

	public Stream(Container.StreamType streamType, Map<String, String> streamAttributes) {
		this.streamAttributes = streamAttributes;
	}
	

	public String toString() {
		return this.getStreamType() + "::" + this.streamAttributes; 
	}
}
