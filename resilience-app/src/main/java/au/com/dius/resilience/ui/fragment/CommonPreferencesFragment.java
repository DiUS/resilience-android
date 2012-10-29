package au.com.dius.resilience.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import au.com.dius.resilience.R;

import static au.com.dius.resilience.Constants.PREFERENCES_FILE_COMMON;

public class CommonPreferencesFragment extends PreferenceFragment {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getPreferenceManager().setSharedPreferencesName(PREFERENCES_FILE_COMMON);
    addPreferencesFromResource(R.xml.common_preferences);
  }
}
