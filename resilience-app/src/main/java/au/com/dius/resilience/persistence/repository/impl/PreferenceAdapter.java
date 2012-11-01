package au.com.dius.resilience.persistence.repository.impl;

import android.content.Context;
import android.content.SharedPreferences;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Profile;

import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static au.com.dius.resilience.Constants.DEFAULT_USER_KEY;

public class PreferenceAdapter {

  public static final String PREFERENCES_FILE_PREFIX = "au.com.dius.resilience.preference.";
  public static final String PREFERENCES_FILE_COMMON = PREFERENCES_FILE_PREFIX + "common";
  public static final String PREFERENCES_FILE_DEFAULT = PREFERENCES_FILE_PREFIX + DEFAULT_USER_KEY;

  private Context context;
  private String defaultUser;

  public PreferenceAdapter(Context context) {
    this.context = context;
  }

  public SharedPreferences getCommonPreferences() {
    return context.getSharedPreferences(PREFERENCES_FILE_COMMON, MODE_PRIVATE);
  }

  public Profile getCurrentProfile() {
    String profile = getCommonPreferences().getString(context.getString(R.string.current_profile_key), DEFAULT_USER_KEY);
    return new Profile(profile);
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

  private SharedPreferences.Editor openEditor(SharedPreferences sharedPreferences) {
    return sharedPreferences.edit();
  }

  private void closeEditor(SharedPreferences.Editor editor) {
    editor.commit();
  }

  public String getCurrentUserPreferenceFile() {
    return PREFERENCES_FILE_PREFIX + getCurrentProfile().getPreferencesFilename();
  }

  // Saving preferences (TODO: I don't like this.. I'm sure there's a better way)
  public void save(SharedPreferences sharedPreferences, int key, String value) {
    SharedPreferences.Editor editor = openEditor(sharedPreferences);
    editor.putString(context.getString(key), value);
    closeEditor(editor);
  }

  public void save(SharedPreferences sharedPreferences, int key, Boolean value) {
    SharedPreferences.Editor editor = openEditor(sharedPreferences);
    editor.putBoolean(context.getString(key), value);
    closeEditor(editor);
  }

  public void save(SharedPreferences sharedPreferences, int key, Long value) {
    SharedPreferences.Editor editor = openEditor(sharedPreferences);
    editor.putLong(context.getString(key), value);
    closeEditor(editor);
  }

  public void save(SharedPreferences sharedPreferences, int key, Float value) {
    SharedPreferences.Editor editor = openEditor(sharedPreferences);
    editor.putFloat(context.getString(key), value);
    closeEditor(editor);
  }

  public void save(SharedPreferences sharedPreferences, int key, Integer value) {
    SharedPreferences.Editor editor = openEditor(sharedPreferences);
    editor.putInt(context.getString(key), value);
    closeEditor(editor);
  }

  public void save(SharedPreferences sharedPreferences, int key, Set<String> values) {
    SharedPreferences.Editor editor = openEditor(sharedPreferences);
    editor.putStringSet(context.getString(key), values);
    closeEditor(editor);
  }
}