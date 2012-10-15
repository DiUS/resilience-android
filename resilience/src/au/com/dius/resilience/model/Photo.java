package au.com.dius.resilience.model;

import android.net.Uri;

public class Photo {
  
  private Uri uri;
  private byte[] data;

  public Photo(Uri uri) {
    this.uri = uri;
  }
  
  public Uri getUri() {
    return uri;
  }
  
  public byte[] data() {
    return data;
  }
}
