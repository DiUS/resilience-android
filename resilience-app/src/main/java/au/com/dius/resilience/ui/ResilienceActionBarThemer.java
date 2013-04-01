package au.com.dius.resilience.ui;

import android.app.Activity;
import com.google.inject.Inject;

public class ResilienceActionBarThemer {

  private static final String ACTION_BAR_TITLE = "RESILIENCE.";

  @Inject
  public ResilienceActionBarThemer(Activity context) {
    context.getActionBar().setDisplayShowTitleEnabled(true);
    context.getActionBar().setTitle(ACTION_BAR_TITLE);
  }
}
