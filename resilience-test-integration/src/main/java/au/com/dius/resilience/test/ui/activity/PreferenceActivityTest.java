package au.com.dius.resilience.test.ui.activity;

import au.com.dius.resilience.R;
import au.com.dius.resilience.test.AbstractResilienceActivityTestCase;
import au.com.dius.resilience.ui.activity.ManageProfileActivity;
import au.com.dius.resilience.ui.activity.PreferenceActivity;

public class PreferenceActivityTest extends AbstractResilienceActivityTestCase<PreferenceActivity> {
  public PreferenceActivityTest() {
    super(PreferenceActivity.class);
  }

  public void testSwitchProfileUpdatesUserSettings() {
    solo.clickOnText(getString(R.string.current_profile));
    solo.clickOnText(getString(R.string.create_plus));

    solo.assertCurrentActivity("Switching to profile manager failed.", ManageProfileActivity.class);

    solo.clickOnEditText(0);
    solo.typeText(0, "Brand spankin' new profile");
    solo.clickOnButton(getString(R.string.save));

    solo.assertCurrentActivity("Switching back to preferences failed.", PreferenceActivity.class);

//    solo.getV

    solo.sleep(10000);
  }
}