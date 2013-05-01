
package au.com.dius.resilience.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import au.com.dius.resilience.R;

import static au.com.dius.resilience.observer.PreferenceChangeBroadcastReceiver.PREFERENCES_UPDATED_FILTER;
import static au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter.PREFERENCES_FILE_COMMON;

public class CommonPreferencesFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

  private ListPreference switchProfilePreference;

  private Context appContext;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    appContext = getActivity().getApplicationContext();

    getPreferenceManager().setSharedPreferencesName(PREFERENCES_FILE_COMMON);
    addPreferencesFromResource(R.xml.common_preferences);
    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    switchProfilePreference = (ListPreference) findPreference(getString(R.string.current_profile_key));
    switchProfilePreference.setSummary(switchProfilePreference.getEntry());
  }

  @Override
  public boolean onPreferenceClick(Preference preference) {
    return true;
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    switchProfilePreference.setSummary(switchProfilePreference.getEntry());
    appContext.sendBroadcast(new Intent(PREFERENCES_UPDATED_FILTER));
  }
}