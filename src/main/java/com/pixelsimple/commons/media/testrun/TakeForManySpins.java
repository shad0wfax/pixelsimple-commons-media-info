/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media.testrun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.commons.media.MediaContainer;

/**
 *
 * @author Akshay Sharma
 * Nov 28, 2011
 */
public class TakeForManySpins implements Runnable {
	static final Logger LOG = LoggerFactory.getLogger(TakeForManySpins.class);
	static Map<String, String> overrideParams = null;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args != null && args.length > 0) {
			overrideParams = new HashMap<String, String>(8);
			
			for (String a : args) {
				int indexEquals = a.indexOf("=");
				
				if (indexEquals != -1) {
					overrideParams.put(a.substring(0, indexEquals), a.substring(indexEquals + 1));
				}
			}
		}
		LOG.debug("TakeForManySpinsg:Override params::{}", overrideParams);
		
		try {
			List<Thread> threads = new ArrayList<Thread>();
			
			for (int i  = 0; i < 8; i++) {
				Thread t = new Thread(new TakeForManySpins());
				threads.add(t);
			}
			
			for (Thread t : threads) {
				t.start();
			}
		} catch (Throwable t) {
			LOG.error("{}", t);
		}

	}
	
	private void getContainerInfo(Map<String, String> overrideParams) {
		Map<String, String> params = new HashMap<String, String>(8);
		
//		ResourceBundle bundle = ResourceBundle.getBundle("default_transcoder_settings");
//		Enumeration<String> keys = bundle.getKeys();
//		
//		while (keys.hasMoreElements()) {
//			String key = keys.nextElement();
//			String value = bundle.getString(key);
//			params.put(key, value);
//		}
//		
		if (overrideParams != null) {
			params.putAll(overrideParams);
		}
		LOG.debug("transcode::final params::{}", params);

		String filePathWithFileName = params.get("filePathWithFileName");
		MediaContainer.readContainerInfo(filePathWithFileName);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LOG.debug("run::Starting to get the container info - will block");
		this.getContainerInfo(overrideParams);
		LOG.debug("run::Finished to get the container info - after block");
	}

}
