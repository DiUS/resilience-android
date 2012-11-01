
package au.com.dius.resilience.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import au.com.dius.resilience.R;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;

import static au.com.dius.resilience.observer.PreferenceChangeBroadcastReceiver.PREFERENCES_UPDATED_FILTER;
import static au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter.PREFERENCES_FILE_COMMON;

public class CommonPreferencesFragment extends PreferenceFragment implements DialogInterface.OnClickListener
                                                                           , Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

  private PreferenceAdapter preferenceAdapter;
  private CheckBoxPreference themeCheckboxPreference;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getPreferenceManager().setSharedPreferencesName(PREFERENCES_FILE_COMMON);
    addPreferencesFromResource(R.xml.common_preferences);
    getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    preferenceAdapter = new PreferenceAdapter(getActivity());

    themeCheckboxPreference = (CheckBoxPreference) findPreference(getString(R.string.use_light_theme_key));
    themeCheckboxPreference.setOnPreferenceClickListener(this);
  }

  @Override
  public void onClick(DialogInterface dialog, int which) {
    if (DialogInterface.BUTTON_POSITIVE == which) {
      restartApp();
    }
    else {
      revertCheckboxState();
    }
  }

  @Override
  public boolean onPreferenceClick(Preference preference) {
    showThemeChangeWarning();
    return true;
  }

  private void revertCheckboxState() {
    boolean checkboxState = themeCheckboxPreference.isChecked();
    themeCheckboxPreference.setChecked(!checkboxState);
    preferenceAdapter.save(preferenceAdapter.getCommonPreferences(), R.string.use_light_theme_key, !checkboxState);
  }

  private void restartApp() {
    Intent i = getActivity().getBaseContext().getPackageManager()
      .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(i);
  }

  private void showThemeChangeWarning() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage(R.string.theme_change_warning);
    builder.setPositiveButton(R.string.restart, this);
    builder.setNegativeButton(android.R.string.cancel, this);
    builder.show();
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    getActivity().sendBroadcast(new Intent(PREFERENCES_UPDATED_FILTER));
  }
}