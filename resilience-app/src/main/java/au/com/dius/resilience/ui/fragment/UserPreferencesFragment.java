package au.com.dius.resilience.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import au.com.dius.resilience.R;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;

public class UserPreferencesFragment extends PreferenceFragment {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    PreferenceAdapter preferenceAdapter = new PreferenceAdapter(getActivity());
    getPreferenceManager().setSharedPreferencesName(preferenceAdapter.getCurrentUserPreferenceFile());
    addPreferencesFromResource(R.xml.user_preferences);
  }
}