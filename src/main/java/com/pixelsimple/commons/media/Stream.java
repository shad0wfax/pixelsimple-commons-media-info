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
	private Map<String, String> metadata;
	
	/**
	 * @return the streamType
	 */
	public Container.StreamType getStreamType() {
		return streamType;
	}

	public Stream(Container.StreamType streamType, Map<String, String> streamAttributes) {
		this.streamAttributes = streamAttributes;
	}
	
	public void addMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public String toString() {
		return this.getStreamType() + "::" + this.streamAttributes + "::\nStream Metadata::" + this.metadata; 
	}
}
