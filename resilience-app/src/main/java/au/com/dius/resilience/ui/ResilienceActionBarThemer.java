package au.com.dius.resilience.ui;

import android.app.Activity;
import android.util.Log;
import com.google.inject.Inject;

public class ResilienceActionBarThemer {

  private static final String ACTION_BAR_TITLE = "RESILIENCE.";

  public static final String LOG_TAG = ResilienceActionBarThemer.class.getName();

  @Inject
  public ResilienceActionBarThemer(Activity context) {

    if (context.getActionBar() == null) {
      Log.w(LOG_TAG, "Warning: Action bar is null!");
      return;
    }

    context.getActionBar().setDisplayShowTitleEnabled(true);
    context.getActionBar().setTitle(ACTION_BAR_TITLE);
  }
}
