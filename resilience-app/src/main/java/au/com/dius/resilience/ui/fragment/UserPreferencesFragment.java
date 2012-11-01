package au.com.dius.resilience.ui.fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import au.com.dius.resilience.R;
import au.com.dius.resilience.observer.PreferenceChangeBroadcastReceiver;
import au.com.dius.resilience.observer.PreferenceChangeListener;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;

import static au.com.dius.resilience.observer.PreferenceChangeBroadcastReceiver.PREFERENCES_UPDATED_FILTER;

public class UserPreferencesFragment extends PreferenceFragment implements PreferenceChangeListener {

  private PreferenceAdapter preferenceAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    preferenceAdapter = new PreferenceAdapter(getActivity());
    getPreferenceManager().setSharedPreferencesName(preferenceAdapter.getCurrentUserPreferenceFile());
    addPreferencesFromResource(R.xml.user_preferences);

    getActivity().registerReceiver(new PreferenceChangeBroadcastReceiver(this), new IntentFilter(PREFERENCES_UPDATED_FILTER));
  }

  @Override
  public void onPreferenceChange() {
    getPreferenceManager().setSharedPreferencesName(preferenceAdapter.getCurrentUserPreferenceFile());
    // Refresh preferences
    setPreferenceScreen(null);
    addPreferencesFromResource(R.xml.user_preferences);
  }
}