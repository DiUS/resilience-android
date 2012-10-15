package au.com.dius.resilience.test.util;

public class TestUtils {

  public static void sleep(long milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      throw new RuntimeException("Exception during sleep: ", e);
    }
  }
}
