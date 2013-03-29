package au.com.dius.resilience.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import au.com.dius.resilience.Constants;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;

public class PhotoViewActivity extends RoboActivity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Bitmap photoBitmap = (Bitmap) getIntent().getExtras().get(Constants.EXTRA_PHOTO);
    ImageView imageView = new ImageView(this);
    imageView.setImageBitmap(photoBitmap);
    setContentView(imageView);
  }

}