package au.com.dius.resilience.ui.activity;

import android.os.Bundle;
import au.com.dius.resilience.R;
import au.com.dius.resilience.ui.ResilienceActionBarThemer;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_preference)
public class PreferenceActivity extends RoboActivity {

  @Inject
  private ResilienceActionBarThemer actionBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }
}