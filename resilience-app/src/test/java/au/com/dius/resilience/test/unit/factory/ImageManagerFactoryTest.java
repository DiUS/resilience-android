package au.com.dius.resilience.test.unit.factory;

import android.app.Activity;
import android.content.Context;
import au.com.dius.resilience.factory.ImageManagerFactory;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import com.novoda.imageloader.core.ImageManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(ResilienceTestRunner.class)
public class ImageManagerFactoryTest {

  private ImageManagerFactory imageManagerFactory;

  @Before
  public void setup() {
    imageManagerFactory = new ImageManagerFactory();
  }

  @Test
  public void shouldCreateImageManager() {
    ImageManager imageManager = imageManagerFactory.createImageManager(new Activity());
    assertNotNull(imageManager);
  }
}
