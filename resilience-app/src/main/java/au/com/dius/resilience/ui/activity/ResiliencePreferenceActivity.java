package au.com.dius.resilience.ui.activity;

import android.os.Bundle;
import au.com.dius.resilience.R;
import au.com.dius.resilience.ui.Themer;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_preference)
public class ResiliencePreferenceActivity extends RoboActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Themer.applyCurrentTheme(this);
    super.onCreate(savedInstanceState);
  }
}