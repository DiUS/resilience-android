package au.com.dius.resilience.facade;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

public class CameraFacadeTest extends ActivityInstrumentationTestCase2<BasicPhotoCaptureActivity> {
  
  private CameraFacade cameraFacade;
  
  public CameraFacadeTest() {
    super("au.com.dius.resilience", BasicPhotoCaptureActivity.class);
  }
  
  public void setUp() {
    Activity activity = new BasicPhotoCaptureActivity();
    cameraFacade = new CameraFacade(activity);
  }
  
  public void testCaptureAndReturn() {
    cameraFacade.captureImage();
  }
}
