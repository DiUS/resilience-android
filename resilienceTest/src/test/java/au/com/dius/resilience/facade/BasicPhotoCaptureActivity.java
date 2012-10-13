package au.com.dius.resilience.facade;

import android.app.Activity;
import android.content.Intent;

public class BasicPhotoCaptureActivity extends Activity {

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    System.out.println("Hello world.");
  }
}
