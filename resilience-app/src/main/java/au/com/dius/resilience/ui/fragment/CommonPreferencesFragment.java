package au.com.dius.resilience.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import au.com.dius.resilience.R;

import static au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter.PREFERENCES_FILE_COMMON;

public class CommonPreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getPreferenceManager().setSharedPreferencesName(PREFERENCES_FILE_COMMON);
    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    addPreferencesFromResource(R.xml.common_preferences);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    // TODO - Add confirmation dialog.
    String useLightThemeKey = getString(R.string.use_light_theme_key);
    if (key.equals(useLightThemeKey)) {
      Intent i = getActivity().getBaseContext().getPackageManager()
        .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(i);
    }
  }
}