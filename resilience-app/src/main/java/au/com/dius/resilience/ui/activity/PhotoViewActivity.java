package au.com.dius.resilience.ui.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.ui.Themer;

public class PhotoViewActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Themer.applyCurrentTheme(this);
    super.onCreate(savedInstanceState);

    Bitmap photoBitmap = (Bitmap) getIntent().getExtras().get(Constants.EXTRA_PHOTO);
    ImageView imageView = new ImageView(this);
    imageView.setImageBitmap(photoBitmap);
    setContentView(imageView);
  }

}