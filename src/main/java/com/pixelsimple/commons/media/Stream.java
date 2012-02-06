/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media;

import java.util.Map;

import com.pixelsimple.appcore.media.StreamType;

/**
 * 
 *
 * @author Akshay Sharma
 * Dec 16, 2011
 */
public final class Stream {
	// A type for enums to generalize
	public static interface ENUM_STREAM_ATTRIBUTES {		
	}
	public static enum AUDIO_STREAM_ATTRIBUTES implements ENUM_STREAM_ATTRIBUTES {codec_name, codec_long_name, codec_type, codec_time_base, codec_tag_string, codec_tag, 
		sample_rate, channels, bits_per_sample, r_frame_rate, avg_frame_rate, time_base, start_time, duration, nb_frames, metadata};
	public static enum VIDEO_STREAM_ATTRIBUTES implements ENUM_STREAM_ATTRIBUTES {codec_name, codec_long_name, codec_type, codec_time_base, codec_tag_string, codec_tag, 
		width, height, has_b_frames, sample_aspect_ratio, display_aspect_ratio, pix_fmt, r_frame_rate, avg_frame_rate, time_base, start_time,
		duration, nb_frames, metadata};
	
	private Map<String, String> streamAttributes;
	private StreamType streamType;
	private Map<String, String> metadata;
	
	
	/**
	 * @return the streamType
	 */
	public StreamType getStreamType() {
		return this.streamType;
	}

	public Stream(StreamType streamType, Map<String, String> streamAttributes) {
		this.streamType = streamType;
		this.streamAttributes = streamAttributes;
	}
	
	public void addMetadata(Map<String, String> metadata) {
		this.metadata = metadata;
	}
	
	/**
	 * A BIG assumption here is that the attributes are in lower case - A very important requirement. 
	 * 
	 * @param attribute
	 * @return
	 */
	public String getStreamAttribute(ENUM_STREAM_ATTRIBUTES attribute) {
		if (attribute == null) {
			return null;
		}
		// Downcast to an Enum
		return this.streamAttributes.get(((Enum) attribute).name());
	}


	public String toString() {
		return this.getStreamType() + "::" + this.streamAttributes + "::\nStream Metadata::" + this.metadata; 
	}
}
