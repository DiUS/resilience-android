package au.com.dius.resilience.test;

import android.test.ActivityInstrumentationTestCase2;
import au.com.dius.resilience.ResilienceActivity;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class au.com.dius.resilience.ResilienceActivityTest \
 * au.com.dius.resilience.tests/android.test.InstrumentationTestRunner
 */
public class ResilienceActivityTest extends ActivityInstrumentationTestCase2<ResilienceActivity> {

    public ResilienceActivityTest() {
        super("au.com.dius.resilience", ResilienceActivity.class);
    }

    public void testThingsAreOk() {
      
      assertTrue(true);
    }
}
