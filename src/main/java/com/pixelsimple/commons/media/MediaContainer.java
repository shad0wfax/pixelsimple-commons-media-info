/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Akshay Sharma
 * Dec 13, 2011
 */
public abstract class MediaContainer implements Container {
	
	private Map<Container.StreamType, Stream> streams = new HashMap<Container.StreamType, Stream>(4);
	private Map<String, String> containerAttributes;
	
	/**
	 * 
	 */
	public MediaContainer() {
		super();
	}

	
	public Container addStreams(Container.StreamType streamType, Map<String, String> streamAttributes) {
		if (streams.containsKey(streamType)) {
			return this;
		}
		
		Stream stream = new Stream(streamType, streamAttributes);
		this.streams.put(streamType, stream);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getStreams()
	 */
	@Override
	public Map<Container.StreamType, Stream> getStreams() {
		return this.streams;
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getStreamCount()
	 */
	@Override
	public int getStreamCount() {
		if (this.streams == null) {
			return 0;
		}
		return streams.size();
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getBitRate()
	 */
	@Override
	public String getBitRateBps() {
		return this.containerAttributes.get(Container.CONTAINER_FORMAT_ATTRIBUTES.bit_rate.name());
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getDuration()
	 */
	@Override
	public String getDuration() {
		return this.containerAttributes.get(Container.CONTAINER_FORMAT_ATTRIBUTES.duration.name());
	}
	
	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getFileSize()
	 */
	@Override
	public String getFileSize() {
		return this.containerAttributes.get(Container.CONTAINER_FORMAT_ATTRIBUTES.size.name());
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getMetaData()
	 */
	@Override
	public Map<String, String> getMetaData() {
		//return this.containerAttributes.get("duration");
		return null;
	}
	
	/**
	 * @param containerAttributes the containerAttributes to set
	 */
	public void addContainerAttributes(Map<String, String> containerAttributes) {
		this.containerAttributes = containerAttributes;
	}

	public String toString() {
		return this.getMediaType() + "::" + this.containerAttributes + "\n stream data ::\n" + streams; 
	}

}