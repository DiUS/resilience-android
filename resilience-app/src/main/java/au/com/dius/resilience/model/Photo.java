package au.com.dius.resilience.model;

import java.io.File;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.net.Uri;

public class Photo implements Serializable {
  
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
