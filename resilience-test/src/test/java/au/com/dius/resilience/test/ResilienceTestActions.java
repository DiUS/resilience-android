package au.com.dius.resilience.test;

import android.content.Context;
import android.preference.PreferenceManager;
import android.test.InstrumentationTestCase;
import android.util.Log;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import au.com.dius.resilience.test.util.ParseTestUtils;

import static android.content.Context.MODE_PRIVATE;

/**
 * For sharing test helper functionality across AbstractResilience*TestCases.
 */
public class ResilienceTestActions {

  private InstrumentationTestCase test;
  private static final String LOG_TAG = ResilienceTestActions.class.getName();

  public ResilienceTestActions(InstrumentationTestCase instrumentation) {
    this.test = instrumentation;
  }

  public void setUp() {
    Log.d(LOG_TAG, "Preparing for test " + Thread.currentThread().getName());
    test.getInstrumentation().waitForIdleSync();
    reinitialiseSharedPreferences();

//    ParseTestUtils.setUp(test.getInstrumentation().getTargetContext());
//    ParseTestUtils.dropAll(test.getInstrumentation());
  }

  private void reinitialiseSharedPreferences() {
    Context application = test.getInstrumentation().getTargetContext();
    application.getSharedPreferences(PreferenceAdapter.PREFERENCES_FILE_COMMON, MODE_PRIVATE)
      .edit().clear().commit();
    application.getSharedPreferences(PreferenceAdapter.PREFERENCES_FILE_DEFAULT, MODE_PRIVATE)
      .edit().clear().commit();
    PreferenceManager.setDefaultValues(application, PreferenceAdapter.PREFERENCES_FILE_COMMON,
      MODE_PRIVATE, au.com.dius.resilience.R.xml.common_preferences, true);
    PreferenceManager.setDefaultValues(application, PreferenceAdapter.PREFERENCES_FILE_DEFAULT, MODE_PRIVATE,
      au.com.dius.resilience.R.xml.user_preferences, true);
  }

  public String getString(int id) {
    return test.getInstrumentation().getContext().getString(id);
  }
}
