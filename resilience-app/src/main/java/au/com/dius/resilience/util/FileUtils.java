package au.com.dius.resilience.util;

import roboguice.inject.ContextSingleton;

import java.io.File;

@ContextSingleton
public class FileUtils {
  public boolean exists(String path) {
    return new File(path).exists();
  }
}
