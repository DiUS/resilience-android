package au.com.dius.resilience.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import au.com.dius.resilience.R;

import static au.com.dius.resilience.Constants.PREFERENCES_FILE_COMMON;
import static au.com.dius.resilience.Constants.PREFERENCES_FILE_PREFIX;

public class UserPreferencesFragment extends PreferenceFragment {

  public static final String DEFAULT_USER = "default_user";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFERENCES_FILE_COMMON, Context.MODE_PRIVATE);
    String profile = sharedPreferences.getString("current_profile", DEFAULT_USER);

    getPreferenceManager().setSharedPreferencesName(PREFERENCES_FILE_PREFIX + profile);
    addPreferencesFromResource(R.xml.user_preferences);
  }
}