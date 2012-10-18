package au.com.dius.resilience.model;

import java.io.File;

public class Photo {
  
  private File path;

  public Photo(File path) {
    this.path = path;
  }
  
  public File getPath() {
    return path;
  }
}
