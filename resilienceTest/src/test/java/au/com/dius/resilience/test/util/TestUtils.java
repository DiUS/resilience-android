package au.com.dius.resilience.test.util;

import android.util.Log;

public class TestUtils {

  public static void sleep(long milliseconds) {
    Log.d(TestUtils.class.getName(), "Thread sleeping for " + milliseconds + " milliseconds.");
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      throw new RuntimeException("Exception during sleep: ", e);
    }
  }
}
