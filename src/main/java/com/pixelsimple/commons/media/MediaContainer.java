/**
 * � PixelSimple 2011-2012.
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
	private Map<String, String> metadata;
	
	/**
	 * 
	 */
	public MediaContainer() {
		super();
	}

	
	public Container addStreams(Container.StreamType streamType, Map<String, String> streamAttributes,
			Map<String, String> streamMetadata) {
		if (streams.containsKey(streamType)) {
			return this;
		}
		Stream stream = new Stream(streamType, streamAttributes);
		stream.addMetadata(streamMetadata);
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
		return this.metadata;
	}
	
	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getContainerAttribute(com.pixelsimple.commons.media.Container.CONTAINER_FORMAT_ATTRIBUTES)
	 */
	@Override
	public String getContainerAttribute(CONTAINER_FORMAT_ATTRIBUTES attribute) {
		if (attribute == null) {
			return null;
		}
		return this.containerAttributes.get(attribute.name());
	}

	/**
	 * @param containerAttributes the containerAttributes to set
	 */
	public MediaContainer addContainerAttributes(Map<String, String> containerAttributes) {
		this.containerAttributes = containerAttributes;
		return this;
	}

	public void addMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public String toString() {
		return this.getMediaType() + "::" + this.containerAttributes + "::\nContainer Metadata::" + this.metadata 
				+ "\n stream data ::\n" + streams; 
	}

}