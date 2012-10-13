package au.com.dius.resilience.model;

import android.net.Uri;

public class Photo {
  
  private Uri uri;

  public Photo(Uri uri) {
    this.uri = uri;
  }
  
  public Uri getUri() {
    return uri;
  }
}
