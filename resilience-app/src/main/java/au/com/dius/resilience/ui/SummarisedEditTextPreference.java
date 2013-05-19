package au.com.dius.resilience.ui;

import android.content.Context;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Patterns;
import au.com.dius.resilience.R;
import roboguice.test.RobolectricRoboTestRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SummarisedEditTextPreference extends EditTextPreference implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

  private final Map<String, Pattern> KEY_TO_PATTERN = new HashMap<String, Pattern>();

  public SummarisedEditTextPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
    setOnPreferenceChangeListener(this);
    setOnPreferenceClickListener(this);

    KEY_TO_PATTERN.put(context.getString(R.string.profile_email_key), Patterns.EMAIL_ADDRESS);
    KEY_TO_PATTERN.put(context.getString(R.string.profile_phone_key), Patterns.PHONE);
  }

  @Override
  public void onSetInitialValue(boolean restoreValue, Object defaultValue) {
    super.onSetInitialValue(restoreValue, defaultValue);
    setSummary(restoreValue ? getPersistedString(getKey()) : (String) defaultValue);
  }

  @Override
  public boolean onPreferenceChange(Preference preference, Object newValue) {

    String asString = newValue.toString();

    Pattern verificationPattern = KEY_TO_PATTERN.get(preference.getKey());
    if (verificationPattern != null && !verificationPattern.matcher(asString).matches()) {
      getEditText().setError("Invalid");
      showDialog(null);
      return false;
    }

    setSummary(asString);
    return true;
  }

  @Override
  public boolean onPreferenceClick(Preference preference) {
    getEditText().setError(null);
    return true;
  }
}
