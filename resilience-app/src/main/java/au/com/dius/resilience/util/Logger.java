package au.com.dius.resilience.util;

import android.util.Log;
import au.com.dius.resilience.BuildConfig;

public class Logger {

  private static final String TAG = "au.com.dius.resilience";

  public static void i(Object caller, Object... messages) {
    if (BuildConfig.DEBUG) {
      Log.i(TAG, buildMessage(caller, messages));
    }
  }

  public static void d(Object caller, Object... messages) {

    if (BuildConfig.DEBUG) {
      Log.d(TAG, buildMessage(caller, messages));
    }
  }

  public static void w(Object caller, Object... messages) {
    if (BuildConfig.DEBUG) {
      Log.w(TAG, buildMessage(caller, messages));
    }
  }

  public static void e(Object caller, Object... messages) {
    if (BuildConfig.DEBUG) {
      Log.e(TAG, buildMessage(caller, messages));
    }
  }

  private static String prefix(Object caller) {
    return caller.getClass().getName() + ": ";
  }

  private static String buildMessage(Object caller, Object[] messages) {
    StringBuilder builder = new StringBuilder(prefix(caller));
    for (int i = 0; i < messages.length; ++i) {
      builder.append(messages[i]).append(" ");
    }
    return builder.toString();
  }
}
