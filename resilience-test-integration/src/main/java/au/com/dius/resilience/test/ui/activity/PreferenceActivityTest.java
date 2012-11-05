package au.com.dius.resilience.test.ui.activity;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.widget.EditText;
import au.com.dius.resilience.R;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import au.com.dius.resilience.test.AbstractResilienceActivityTestCase;
import au.com.dius.resilience.ui.activity.ManageProfileActivity;
import au.com.dius.resilience.ui.activity.PreferenceActivity;
import au.com.dius.resilience.ui.activity.ResilienceActivity;

import java.util.LinkedHashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceActivityTest extends AbstractResilienceActivityTestCase<PreferenceActivity> {
  public PreferenceActivityTest() {
    super(PreferenceActivity.class);
  }

  private void createTestUsers() {
    Application application = getActivity().getApplication();
    SharedPreferences.Editor editor = application.getSharedPreferences(PreferenceAdapter.PREFERENCES_FILE_COMMON, MODE_PRIVATE).edit();
    Set<String> profiles = new LinkedHashSet<String>();
    profiles.add("Default User");
    profiles.add("John Doe");
    profiles.add("Jayne Doe");
    editor.putStringSet(getString(R.string.profile_entries), profiles);
    editor.commit();
  }

  public void testSwitchingProfileUpdatesUserSettings() {
    createTestUsers();

    solo.clickOnText(getString(R.string.response_radius));

    solo.clearEditText(0);
    solo.typeText(0, "999");
    solo.clickOnText(getString(android.R.string.ok));

    solo.clickOnText(getString(R.string.current_profile));
    solo.clickOnText("John Doe");
    solo.clickOnText(getString(R.string.response_radius));

    EditText editText = solo.getEditText(0);

    assertEquals("10", editText.getText().toString());
  }

  public void testCreateProfile() {
    solo.clickOnText(getString(R.string.current_profile));
    solo.clickOnText(getString(R.string.create_plus));

    solo.assertCurrentActivity("Switching to profile manager failed.", ManageProfileActivity.class);

    solo.clickOnEditText(0);
    solo.typeText(0, "Brand spankin' new profile");
    solo.clickOnButton(getString(R.string.save));

    solo.assertCurrentActivity("Switching back to preferences failed.", PreferenceActivity.class);

    solo.clickOnText(getString(R.string.current_profile));
    assertTrue(solo.searchText("Brand spankin' new profile"));
  }

  public void testRestartOnThemeChange() {
    solo.clickOnText(getString(R.string.use_light_theme));
    solo.clickOnButton(getString(R.string.restart));
    solo.assertCurrentActivity("App restart failed.", ResilienceActivity.class);
  }

  public void testCheckboxRevertsOnThemeChangeCancel() {

    boolean startValue = solo.isCheckBoxChecked(getString(R.string.use_light_theme));
    solo.clickOnText(getString(R.string.use_light_theme));
    solo.searchText(getString(R.string.theme_change_warning));
    solo.clickOnButton(getString(android.R.string.cancel));

    boolean endValue = solo.isCheckBoxChecked(getString(R.string.use_light_theme));

    assertEquals(startValue, endValue);
  }
}