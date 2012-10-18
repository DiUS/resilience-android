package au.com.dius.resilience;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import au.com.dius.resilience.test.util.ParseTestUtils;

import com.jayway.android.robotium.solo.Solo;

/**
 * @author georgepapas
 */
public abstract class AbstractResilienceActivityTestCase<T extends android.app.Activity> extends ActivityInstrumentationTestCase2<T> {

  public static final String LOG_TAG = AbstractResilienceActivityTestCase.class.getName();

  protected Solo solo;

  protected void beforeTest() { }
  protected void afterTest() { }

  public AbstractResilienceActivityTestCase(Class<T> activityClass) {
    super(activityClass);
  }

  @Override
  protected void setUp() throws Exception {
    Log.d(LOG_TAG, "Preparing for test " + Thread.currentThread().getName());

    getInstrumentation().waitForIdleSync();
    solo = new Solo(getInstrumentation(), getActivity());

    ParseTestUtils.setUp(getActivity());
    ParseTestUtils.dropAll(getInstrumentation());
  }

  @Override
  protected void tearDown() throws Exception {
  }
}
