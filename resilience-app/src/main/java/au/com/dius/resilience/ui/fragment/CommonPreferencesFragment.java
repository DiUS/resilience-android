package au.com.dius.resilience.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import au.com.dius.resilience.R;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter.PREFERENCES_FILE_COMMON;

public class CommonPreferencesFragment extends PreferenceFragment implements DialogInterface.OnClickListener
                                                                           , Preference.OnPreferenceClickListener {

  private PreferenceAdapter preferenceAdapter;

  private CheckBoxPreference themeCheckboxPreference;
  private ListPreference listPreference;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    getPreferenceManager().setSharedPreferencesName(PREFERENCES_FILE_COMMON);
    addPreferencesFromResource(R.xml.common_preferences);

    preferenceAdapter = new PreferenceAdapter(getActivity());

    listPreference = (ListPreference) findPreference(getString(R.string.current_profile_key));

    CharSequence[] entries = listPreference.getEntries() == null ? new CharSequence[0] : listPreference.getEntries();
    CharSequence[] entryValues = listPreference.getEntryValues() == null ? new CharSequence[0] : listPreference.getEntryValues();

    List<CharSequence> newEntryList = new ArrayList<CharSequence>();
    Collections.addAll(newEntryList, entries);
    newEntryList.add("Create +");

    List<CharSequence> newValueList = new ArrayList<CharSequence>();
    Collections.addAll(newValueList, entryValues);
    newValueList.add("create_new");

    listPreference.setEntries(newEntryList.toArray(new CharSequence[newEntryList.size()]));
    listPreference.setEntryValues(newValueList.toArray(new CharSequence[newValueList.size()]));

    themeCheckboxPreference = (CheckBoxPreference) findPreference(getString(R.string.use_light_theme_key));
    themeCheckboxPreference.setOnPreferenceClickListener(this);
  }

  @Override
  public void onClick(DialogInterface dialog, int which) {
    if (DialogInterface.BUTTON_POSITIVE == which) {
      Intent i = getActivity().getBaseContext().getPackageManager()
        .getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      startActivity(i);
    }
    else {
      boolean checkboxState = themeCheckboxPreference.isChecked();
      themeCheckboxPreference.setChecked(!checkboxState);
      preferenceAdapter.save(preferenceAdapter.getCommonPreferences(), R.string.use_light_theme_key, !checkboxState);
    }
  }

  @Override
  public boolean onPreferenceClick(Preference preference) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage(R.string.theme_change_warning);
    builder.setPositiveButton(R.string.restart, this);
    builder.setNegativeButton(android.R.string.cancel, this);
    builder.show();

    return true;
  }
}