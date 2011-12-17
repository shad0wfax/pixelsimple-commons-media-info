/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.probe;

import com.pixelsimple.commons.media.probe.ffprobe.FfprobeMediaProber;

/**
 *
 * @author Akshay Sharma
 * Dec 17, 2011
 */
public class MediaProbeFactory {
	// For performance - cache a copy, since the probe is state-less. 
	// Eventually make it configurable (spring like)
	private static MediaProbe instance = new FfprobeMediaProber();

	public MediaProbeFactory() {}
	
	public static MediaProbe createMediaProbe() {
		return instance;
	}
	
}
