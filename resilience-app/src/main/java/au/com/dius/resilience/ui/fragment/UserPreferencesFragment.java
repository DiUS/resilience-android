package au.com.dius.resilience.ui.fragment;

import android.content.Context;
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
  private PreferenceChangeBroadcastReceiver broadcastReceiver;
  private Context context;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    context = getActivity().getApplicationContext();

    preferenceAdapter = new PreferenceAdapter(getActivity());
    getPreferenceManager().setSharedPreferencesName(preferenceAdapter.getCurrentUserPreferenceFile());
    addPreferencesFromResource(R.xml.user_preferences);

    broadcastReceiver = new PreferenceChangeBroadcastReceiver(this);
    context.registerReceiver(broadcastReceiver, new IntentFilter(PREFERENCES_UPDATED_FILTER));
  }

  @Override
  public void onDestroy() {
    context.unregisterReceiver(broadcastReceiver);
    super.onDestroy();
  }

  @Override
  public void onPreferenceChange() {
    getPreferenceManager().setSharedPreferencesName(preferenceAdapter.getCurrentUserPreferenceFile());
    // Refresh preferences
    setPreferenceScreen(null);
    addPreferencesFromResource(R.xml.user_preferences);
  }
}