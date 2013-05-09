package au.com.dius.resilience.test.unit.ui.activity;

import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.dius.resilience.ui.SummarisedEditTextPreference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import roboguice.activity.RoboActivity;

import static org.junit.Assert.assertTrue;

@RunWith(ResilienceTestRunner.class)
public class SummarisedEditTextPreferenceTest {

  private SummarisedEditTextPreference summarisedEditTextPreference;

  @Mock
  private AttributeSet attrs;

  @Mock
  private PreferenceManager preferenceManager;

  @Mock
  private Preference preference;

  private Object value;

  @Before
  public void setup() throws NoSuchFieldException {
    RoboActivity context = new RoboActivity();
    summarisedEditTextPreference = new SummarisedEditTextPreference(context, attrs);
  }

  @Test
  public void shouldSetSummaryOnPreferenceChangeAndFlagConsumed() {
    boolean updateFlag = summarisedEditTextPreference.onPreferenceChange(preference, value);
    assertTrue(updateFlag);
  }

  // TODO - Testing this is a PITA. Need to revisit.
  @Test
  public void shouldSetSummaryWhenSettingInitialValue() {
    summarisedEditTextPreference.onSetInitialValue(true, "defaultValue");
//    verify(summarisedEditTextPreference).setSummary("someValue");
  }
}
