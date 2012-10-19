package au.com.dius.resilience.test.unit.facade;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import au.com.dius.resilience.facade.CameraFacade;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.os.Environment.DIRECTORY_PICTURES;
import static au.com.dius.resilience.facade.CameraFacade.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
import static junit.framework.Assert.fail;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Environment.class, Uri.class, CameraFacade.class})
public class CameraFacadeTest {

  private CameraFacade cameraFacade;
  private Activity activity;
  private Intent intent;

  @Before
  public void setUp() throws Exception {
    PowerMockito.mockStatic(Environment.class);
    Mockito.when(Environment.getExternalStorageState()).thenReturn(
        Environment.MEDIA_MOUNTED);
    Mockito.when(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES))
                .thenReturn(new File("test"));
    
    Uri uri = PowerMockito.mock(Uri.class);
    PowerMockito.mockStatic(Uri.class);
    Mockito.when(Uri.fromFile((File)Matchers.any())).thenReturn(uri);
    
    intent = Mockito.mock(Intent.class);
    PowerMockito.whenNew(Intent.class).withArguments(Matchers.anyString()).thenReturn(intent);
    
    activity = Mockito.mock(Activity.class);
    cameraFacade = new CameraFacade(activity);
  }

  @Test
  public void testCaptureAndReturn() {
    cameraFacade.captureImage();
    cameraFacade.processPhoto(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, RESULT_OK);
    Mockito.verify(activity).startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    
    Assert.assertEquals(1, cameraFacade.getPhotos().size());
  }
  
  @Test
  public void testMultipleCaptures() {
    cameraFacade.captureImage();
    cameraFacade.processPhoto(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, RESULT_OK);
    cameraFacade.captureImage();
    cameraFacade.processPhoto(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, RESULT_OK);
    cameraFacade.captureImage();
    cameraFacade.processPhoto(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, RESULT_OK);
    
    Assert.assertEquals(3, cameraFacade.getPhotos().size());
  }
  
  @Test
  public void testMultipleProcess() {
    cameraFacade.captureImage();
    
    // What if the client tries to process the result multiple times?
    cameraFacade.processPhoto(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, RESULT_OK);
    cameraFacade.processPhoto(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, RESULT_OK);
    cameraFacade.processPhoto(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, RESULT_OK);
    
    Assert.assertEquals(1, cameraFacade.getPhotos().size());
  }
  
  @Test
  public void testReturnCodes() {
    cameraFacade.captureImage();
    
    // Args are the wrong way
    cameraFacade.processPhoto(RESULT_OK, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    Assert.assertEquals(0, cameraFacade.getPhotos().size());
    
    // User canceled
    cameraFacade.processPhoto(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, RESULT_CANCELED);
    Assert.assertEquals(0, cameraFacade.getPhotos().size());
    
    cameraFacade.processPhoto(CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE, RESULT_OK);
    Assert.assertEquals(1, cameraFacade.getPhotos().size());
  }
}
