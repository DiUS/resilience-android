package au.com.dius.resilience.test;

import android.content.Context;
import android.test.InstrumentationTestCase;

/**
 * For running instrumentation tests which do not have an activity bound to them.
 */
public class AbstractResilienceTestCase extends InstrumentationTestCase {

  private ResilienceTestActions testActions;

  public AbstractResilienceTestCase() {
    testActions = new ResilienceTestActions(this);
  }

  @Override
  public void setUp() {
    testActions.setUp();
  }

  public Context getContext() {
    return getInstrumentation().getContext();
  }

  protected String getString(int id) {
    return testActions.getString(id);
  }
}
