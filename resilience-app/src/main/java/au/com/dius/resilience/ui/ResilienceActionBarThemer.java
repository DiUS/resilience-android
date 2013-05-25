package au.com.dius.resilience.ui;

import android.app.Activity;
import android.util.Log;
import au.com.dius.resilience.util.Logger;
import com.google.inject.Inject;

public class ResilienceActionBarThemer {

  private static final String ACTION_BAR_TITLE = "RESILIENCE.";

  @Inject
  public ResilienceActionBarThemer(Activity context) {

    if (context.getActionBar() == null) {
      Logger.w(this, "Warning: Action bar is null!");
      return;
    }

    context.getActionBar().setDisplayShowTitleEnabled(true);
    context.getActionBar().setTitle(ACTION_BAR_TITLE);
  }
}
