package au.com.dius.resilience.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import au.com.dius.resilience.R;
import au.com.dius.resilience.ui.Themer;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

import static au.com.dius.resilience.observer.PreferenceChangeBroadcastReceiver.PREFERENCES_UPDATED_FILTER;

@ContentView(R.layout.activity_preference)
public class ResiliencePreferenceActivity extends RoboActivity implements Preference.OnPreferenceChangeListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Themer.applyCurrentTheme(this);
    super.onCreate(savedInstanceState);
  }

  @Override
  public boolean onPreferenceChange(Preference preference, Object newValue) {
    sendBroadcast(new Intent(PREFERENCES_UPDATED_FILTER));

    return true;
  }
}