package au.com.dius.resilience.test.unit.ui.activity;

import android.app.Activity;
import au.com.dius.resilience.model.Profile;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.dius.resilience.ui.AppendableListPreference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(ResilienceTestRunner.class)
public class AppendableListPreferenceTest {

  private AppendableListPreference appendableListPreference;

  @Before
  public void setUp() {
    appendableListPreference = new AppendableListPreference(new Activity(), null);
  }

  @Test
  public void onPreferenceChangePreferenceShouldBeFlaggedForUpdate() {
    boolean updateFlag = appendableListPreference.onPreferenceChange(null, "SomeValue");
    assertThat(updateFlag, is(true));
  }

  @Test
  public void onCreateNewPreferenceShouldNotBeFlaggedForUpdate() {
    boolean updateFlag = appendableListPreference.onPreferenceChange(null, AppendableListPreference.CREATE_NEW);
    assertThat(updateFlag, is(false));
  }

  @Test
  public void profileEntriesShouldCorrespondingEntryAndEntryValuesForList() {
    HashSet<Profile> profiles = new HashSet<Profile>();
    profiles.add(new Profile("Profile1"));

    appendableListPreference.addDisplayEntries(profiles);
  }
}
