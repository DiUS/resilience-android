package au.com.dius.resilience.persistence.repository.impl;

import android.content.Context;
import android.content.SharedPreferences;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Profile;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceAdapter {

  public static final String DEFAULT_USER = "default_user";

  public static final String PREFERENCES_FILE_PREFIX = "au.com.dius.resilience.preference.";
  public static final String PREFERENCES_FILE_COMMON = PREFERENCES_FILE_PREFIX + "common";
  public static final String PREFERENCES_FILE_DEFAULT = PREFERENCES_FILE_PREFIX + DEFAULT_USER;

  private Context context;

  public PreferenceAdapter(Context context) {
    this.context = context;
  }

  private SharedPreferences getCommonPreferences() {
    return context.getSharedPreferences(PREFERENCES_FILE_COMMON, MODE_PRIVATE);
  }

  private SharedPreferences getCurrentUserPreferences() {
    return context.getSharedPreferences(getCurrentUserPreferenceFile(), MODE_PRIVATE);
  }

  public Object getCommonPreference(int key) {
    return getCommonPreferences().getAll().get(context.getString(key));
  }

  public Object getUserPreference(int key) {
    return getCurrentUserPreferences().getAll().get(context.getString(key));
  }

  public Profile getCurrentProfile() {
    String profile = getCommonPreferences().getString(context.getString(R.string.current_profile_key), DEFAULT_USER);
    return new Profile(profile);
  }

  public String getCurrentUserPreferenceFile() {
    return PREFERENCES_FILE_PREFIX + getCurrentProfile().getName();
  }
}