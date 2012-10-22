package au.com.dius.resilience.model;

import java.io.File;

import android.graphics.Bitmap;
import android.net.Uri;

public class Photo {
  
  private File path;
  private Uri uri;
  private Bitmap bitmap;

  public Photo(Uri uri, Bitmap bitmap) {
    this.uri = uri;
    this.bitmap = bitmap;
  }
  
  public Photo(File path) {
    this.path = path;
  }
  
  public File getPath() {
    return path;
  }
  
  public Uri getUri() {
    return this.uri;
  }

  public Bitmap getBitmap()  {
    return this.bitmap;
  }
}
