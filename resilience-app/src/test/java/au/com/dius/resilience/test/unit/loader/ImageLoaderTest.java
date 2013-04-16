package au.com.dius.resilience.test.unit.loader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;
import au.com.dius.resilience.factory.ImageManagerFactory;
import au.com.dius.resilience.loader.ImageLoader;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.dius.resilience.test.unit.utils.TestHelper;
import com.cloudinary.Cloudinary;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.loader.Loader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(ResilienceTestRunner.class)
public class ImageLoaderTest {

  public static final String EXPECTED_CLOUD_NAME = "resilience";

  private ImageLoader imageLoader;

  private Cloudinary cloudinary;

  @Mock
  private ImageView view;

  @Mock
  private ImageManagerFactory imageManagerFactory;

  @Mock
  private ImageManager imageManager;

  @Mock
  private Loader loader;

  @Before
  public void setup() throws NoSuchFieldException {
    cloudinary = new Cloudinary();
    Activity context = new Activity();
    when(imageManager.getLoader()).thenReturn(loader);
    when(imageManagerFactory.createImageManager(context)).thenReturn(imageManager);
    imageLoader = new ImageLoader(context, cloudinary, imageManagerFactory);
  }

  @Test
  public void shouldConfigureCloudinary() throws NoSuchFieldException {
    Map config = (Map) TestHelper.getField(cloudinary, "config");
    assertThat((String) config.get(ImageLoader.CLOUD_NAME), is(EXPECTED_CLOUD_NAME));
  }

  @Test
  public void shouldLoadImageWhenUrlValid() {
    imageLoader.loadThumbnailImage(view, "");
    verify(loader).load(view);
  }

  // TODO - add tests around when load fails.
}