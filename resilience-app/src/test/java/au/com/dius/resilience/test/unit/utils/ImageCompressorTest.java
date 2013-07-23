package au.com.dius.resilience.test.unit.utils;

import android.graphics.BitmapFactory;
import au.com.dius.resilience.util.ImageCompressor;
import au.com.dius.resilience.util.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static org.junit.Assert.assertNull;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({BitmapFactory.class, Logger.class})
public class ImageCompressorTest {

  private ImageCompressor imageCompressor;

  @Before
  public void setup() {
    imageCompressor = new ImageCompressor();

    mockStatic(BitmapFactory.class);
    mockStatic(Logger.class);
    PowerMockito.doThrow(new Exception("An error occurred.")).when(BitmapFactory.class);
  }

  @Test
  public void shouldReturnNullOnException() {
    File path = imageCompressor.compress("path", 100);
    assertNull(path);
  }
}
