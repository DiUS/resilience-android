package au.com.dius.resilience.model;

import java.io.File;

import android.net.Uri;

public class Photo {
  
  private File path;
  private Uri uri;

  public Photo(Uri uri) {
    this.uri = uri;
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
}
