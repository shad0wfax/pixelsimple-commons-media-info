/**
 * © PixelSimple 2011-2012.
 */
package com.pixelsimple.commons.media;

import static org.junit.Assert.fail;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixelsimple.appcore.media.MediaType;
import com.pixelsimple.commons.media.exception.MediaException;
import com.pixelsimple.commons.test.appcore.init.TestAppInitializer;
import com.pixelsimple.commons.test.util.TestUtil;

/**
 *
 * @author Akshay Sharma
 * Mar 7, 2012
 */
public class MediaInspectorTest {
	private static final Logger LOG = LoggerFactory.getLogger(MediaInspectorTest.class);

	@Before
	public void setUp() throws Exception {
		TestAppInitializer.bootStrapRegistryForTesting();
	}
	
	/**
	 * Test method for {@link com.pixelsimple.commons.media.MediaInspector#createMediaContainer(java.lang.String)}.
	 */
	@Test
	public void inspectValidMediaContainer() {
		String mediaPath = TestAppInitializer.TEST_ARTIFACT_DIR + "video1.mov";
		
		if (TestUtil.fileExists(mediaPath)) {
			MediaInspector inspector = new MediaInspector();
			try {
				Container container = inspector.createMediaContainer(mediaPath);
				
				Assert.assertEquals(container.getMediaType(), MediaType.VIDEO);
				Assert.assertEquals(container.getFormatFromFileExtension(), "mov");
			} catch (MediaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail();
			}
			
		} else {
			LOG.error("inspectValidMediaContainer::Did not find the media to test with, logging and passing the test.");
			Assert.assertTrue(true);
		}
		
	}

	/**
	 * Test method for {@link com.pixelsimple.commons.media.MediaInspector#createMediaContainer(java.lang.String)}.
	 */
	@Test
	public void inspectValidMediaContainerWithSpace() {
		String mediaPath = TestUtil.getTestConfig().get(TestUtil.TEST_ARTIFACT_DIR_CONFIG) + "with space in folder name/video1.mov";
		
		if (TestUtil.fileExists(mediaPath)) {
			MediaInspector inspector = new MediaInspector();
			try {
				Container container = inspector.createMediaContainer(mediaPath);
				
				Assert.assertEquals(container.getMediaType(), MediaType.VIDEO);
				Assert.assertEquals(container.getFormatFromFileExtension(), "mov");
			} catch (MediaException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail();
			}
		} else {
			LOG.error("inspectValidMediaContainerWithSpace::Did not find the media to test with, logging and passing the test.");
			Assert.assertTrue(true);
		}

	}
	
	
}
