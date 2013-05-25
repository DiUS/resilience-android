package au.com.dius.resilience.util;

import android.util.Log;
import au.com.dius.resilience.BuildConfig;

public class Logger {

  private static final String TAG = "au.com.dius.resilience";

  public static void d(Object caller, String message) {

    if (BuildConfig.DEBUG) {
      Log.d(caller.getClass().getName(), message);
    }
  }
}
