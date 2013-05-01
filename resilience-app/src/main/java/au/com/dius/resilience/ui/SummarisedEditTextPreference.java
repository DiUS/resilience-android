package au.com.dius.resilience.ui;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.util.AttributeSet;

public class SummarisedEditTextPreference extends EditTextPreference implements Preference.OnPreferenceChangeListener{

  public SummarisedEditTextPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
    setOnPreferenceChangeListener(this);
  }

  @Override
  public void onSetInitialValue(boolean restoreValue, Object defaultValue) {
    super.onSetInitialValue(restoreValue, defaultValue);
    setSummary(restoreValue ? getPersistedString(getKey()) : (String) defaultValue);
  }

  @Override
  public boolean onPreferenceChange(Preference preference, Object newValue) {
    setSummary(newValue.toString());
    return false;
  }
}
