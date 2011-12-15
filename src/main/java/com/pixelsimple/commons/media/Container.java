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
	
	// These container attributes are based off of FFProbe. Ensure other libraries used to inspect media data adheres to these attribute names.
	public static enum CONTAINER_FORMAT_ATTRIBUTES {filename, nb_streams, format_name, format_long_name, start_time, duration, size, bit_rate, metadata};
	public static enum CONTAINER_AUDIO_STREAM_ATTRIBUTES {codec_name, codec_long_name, codec_type, codec_time_base, codec_tag_string, codec_tag, 
		sample_rate, channels, bits_per_sample, r_frame_rate, avg_frame_rate, time_base, start_time, duration, nb_frames, metadata};
	public static enum CONTAINER_VIDEO_STREAM_ATTRIBUTES {codec_name, codec_long_name, codec_type, codec_time_base, codec_tag_string, codec_tag, 
		width, height, has_b_frames, sample_aspect_ratio, display_aspect_ratio, pix_fmt, r_frame_rate, avg_frame_rate, time_base, start_time,
		duration, nb_frames, metadata};
	
	MediaType getMediaType();
	
	 Map<Container.StreamType, Stream> getStreams();
	
	int getStreamCount();
	
	String getBitRateBps();
	
	String getDuration();

	String getFileSize();

	String getMetaData();
	
}
