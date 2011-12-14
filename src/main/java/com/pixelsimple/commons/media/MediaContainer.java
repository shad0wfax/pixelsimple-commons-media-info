/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media;

import java.util.Map;

/**
 *
 * @author Akshay Sharma
 * Dec 13, 2011
 */
public abstract class MediaContainer implements Container {
	
	private Map<Container.StreamType, String> streams;
	private String bitRate;
	private String duration;
	private String fileSize;
	private String metaData;

	/**
	 * 
	 */
	public MediaContainer() {
		super();
	}

	
	public Container addStreams(Map<Container.StreamType, String> streams) {
		this.streams = streams;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getStreams()
	 */
	@Override
	public Map<Container.StreamType, String> getStreams() {
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

	/**
	 * @param bitRate
	 */
	public MediaContainer setBitRate(String bitRate) {
		this.bitRate = bitRate;
		return this;
	}


	/**
	 * @param duration
	 */
	public MediaContainer setDuration(String duration) {
		this.duration = duration;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getBitRate()
	 */
	@Override
	public String getBitRateKbps() {
		return this.bitRate;
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getDuration()
	 */
	@Override
	public String getDuration() {
		return this.duration;
	}
	
	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getFileSize()
	 */
	@Override
	public String getFileSize() {
		return this.fileSize;
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getMetaData()
	 */
	@Override
	public String getMetaData() {
		return this.metaData;
	}
	
	/**
	 * @param fileSize the fileSize to set
	 */
	public MediaContainer setFileSize(String fileSize) {
		this.fileSize = fileSize;
		return this;
	}

	/**
	 * @param metaData the metaData to set
	 */
	public MediaContainer setMetaData(String metaData) {
		this.metaData = metaData;
		return this;
	}

	public String toString() {
		return this.getMediaType() + "::" + this.bitRate + " kb/s::" + this.duration + "::" + this.streams;
	}

}