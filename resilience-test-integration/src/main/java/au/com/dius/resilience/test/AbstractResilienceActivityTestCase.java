package au.com.dius.resilience.test;

import android.app.Application;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.SeekBar;
import au.com.dius.resilience.R;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import au.com.dius.resilience.test.util.ParseTestUtils;
import com.jayway.android.robotium.solo.Solo;

import java.util.concurrent.CountDownLatch;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author georgepapas
 */
public abstract class AbstractResilienceActivityTestCase<T extends android.app.Activity> extends ActivityInstrumentationTestCase2<T> {

  public static final String LOG_TAG = AbstractResilienceActivityTestCase.class.getName();

  protected Solo solo;

  private ResilienceTestActions testActions;

  protected void beforeTest() {
  }

  protected void afterTest() {
  }

  public AbstractResilienceActivityTestCase(Class<T> activityClass) {
    super(activityClass);
    testActions = new ResilienceTestActions(this);
  }

  protected String getString(int id) {
    return testActions.getString(id);
  }

  protected void setProgressBar(final SeekBar seekbar, final int progress) {
    final CountDownLatch latch = new CountDownLatch(1);
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        seekbar.setProgress(progress);
        latch.countDown();
      }
    });

    try {
      latch.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void setUp() {
    testActions.setUp();
    solo = new Solo(getInstrumentation(), getActivity());
  }

  @Override
  protected void tearDown() throws Exception {
    solo.finishOpenedActivities();
  }
}
