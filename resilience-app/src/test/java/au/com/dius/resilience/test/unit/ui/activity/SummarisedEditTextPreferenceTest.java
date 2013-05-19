package au.com.dius.resilience.test.unit.ui.activity;

import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Patterns;
import au.com.dius.resilience.R;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.dius.resilience.ui.SummarisedEditTextPreference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import roboguice.activity.RoboActivity;
import roboguice.test.RobolectricRoboTestRunner;

import java.util.regex.Pattern;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(ResilienceTestRunner.class)
public class SummarisedEditTextPreferenceTest {

  private SummarisedEditTextPreference summarisedEditTextPreferenceSpy;

  @Mock
  private AttributeSet attrs;

  @Mock
  private PreferenceManager preferenceManager;

  @Mock
  private Preference preference;

  @Mock
  private Object value;

  private RoboActivity context;

  @Before
  public void setup() throws NoSuchFieldException {
    context = new RoboActivity();

    shadowNativePatternsBehaviour();

    summarisedEditTextPreferenceSpy = spy(new SummarisedEditTextPreference(context, attrs));
  }

  @Test
  public void shouldSetSummaryOnPreferenceChangeAndFlagConsumed() {
    boolean updateFlag = summarisedEditTextPreferenceSpy.onPreferenceChange(preference, value);
    verify(summarisedEditTextPreferenceSpy).setSummary(anyString());
    assertTrue(updateFlag);
  }

  @Test
  public void shouldFailVerificationWhenEmailIsInvalid() {
    given(preference.getKey()).willReturn(context.getString(R.string.profile_email_key));
    boolean updateFlag = summarisedEditTextPreferenceSpy.onPreferenceChange(preference, "@bad@email.com");
    assertFalse(updateFlag);
  }

  @Test
  public void shouldPassVerificationWhenEmailIsValid() {
    given(preference.getKey()).willReturn(context.getString(R.string.profile_email_key));
    boolean updateFlag = summarisedEditTextPreferenceSpy.onPreferenceChange(preference, "good@email.com");
    assertTrue(updateFlag);
  }

  @Test
  public void shouldFailVerificationWhenPhoneNumberIsInvalid() {
    given(preference.getKey()).willReturn(context.getString(R.string.profile_phone_key));
    boolean updateFlag = summarisedEditTextPreferenceSpy.onPreferenceChange(preference, "04yourface");
    assertFalse(updateFlag);
  }


  @Test
  public void shouldPassVerificationWhenPhoneNumberIsValid() {
    given(preference.getKey()).willReturn(context.getString(R.string.profile_phone_key));
    boolean updateFlag = summarisedEditTextPreferenceSpy.onPreferenceChange(preference, "0404123456");
    assertTrue(updateFlag);
  }

  @Test
  public void shouldSetSummaryToDefaultValueWhenSettingInitialValue() {
    summarisedEditTextPreferenceSpy.onSetInitialValue(false, "defaultValue");
    verify(summarisedEditTextPreferenceSpy).setSummary("defaultValue");
  }

  @Test
  public void shouldSetSummaryToStoredValueWhenSettingInitialValue() {
    summarisedEditTextPreferenceSpy.onSetInitialValue(true, "defaultValue");
    // Can't figure out how to properly stub this out so that I can assert against
    // a value other than null.. bad but will do for now.
    verify(summarisedEditTextPreferenceSpy).setSummary(null);
  }

  private void shadowNativePatternsBehaviour() {
    RobolectricRoboTestRunner.setStaticValue(Patterns.class, "EMAIL_ADDRESS", Pattern.compile(
      "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
        "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+"
    ));

    RobolectricRoboTestRunner.setStaticValue(Patterns.class, "PHONE", Pattern.compile(                                  // sdd = space, dot, or dash
      "(\\+[0-9]+[\\- \\.]*)?"                    // +<digits><sdd>*
        + "(\\([0-9]+\\)[\\- \\.]*)?"               // (<digits>)<sdd>*
        + "([0-9][0-9\\- \\.][0-9\\- \\.]+[0-9])"));
  }

}
