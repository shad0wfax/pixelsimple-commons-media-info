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
	private Map<String, String> streams;
	private String bitRate;
	private String duration;

	/**
	 * 
	 */
	public MediaContainer() {
		super();
	}

	
	public Container addStreams(Map<String, String> streams) {
		this.streams = streams;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getStreams()
	 */
	@Override
	public Map<String, String> getStreams() {
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
	public Container setBitRate(String bitRate) {
		this.bitRate = bitRate;
		return this;
	}


	/**
	 * @param duration
	 */
	public Container setDuration(String duration) {
		this.duration = duration;
		return this;
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getBitRate()
	 */
	@Override
	public String getBitRate() {
		return this.bitRate;
	}

	/* (non-Javadoc)
	 * @see com.pixelsimple.commons.media.Container#getDuration()
	 */
	@Override
	public String getDuration() {
		return this.duration;
	}
	
	public String toString() {
		return this.getMediaType() + "::" + this.bitRate + " kb/s::" + this.duration + "::" + this.streams;
	}

}