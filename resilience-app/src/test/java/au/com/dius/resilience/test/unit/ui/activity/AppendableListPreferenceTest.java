package au.com.dius.resilience.test.unit.ui.activity;

import android.app.Activity;
import au.com.dius.resilience.model.Profile;
import au.com.dius.resilience.ui.AppendableListPreference;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedHashSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class AppendableListPreferenceTest {

  private AppendableListPreference appendableListPreference;
  private LinkedHashSet<Profile> profilesList;

  @Before
  public void setUp() {
    appendableListPreference = new AppendableListPreference(new Activity(), null);

    profilesList = new LinkedHashSet<Profile>();
    profilesList.add(new Profile("Profile 1"));
    profilesList.add(new Profile("Profile 2"));
    profilesList.add(new Profile("Profile 3"));
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
  public void shouldHaveOneDefaultUserByDefaultAndOneCreateItem() {
    appendableListPreference.addDisplayEntries(new LinkedHashSet<Profile>());

    CharSequence[] entries = appendableListPreference.getEntries();
    assertThat(entries.length, is(2));

    assertThat(entries[0].toString(), is("Default User"));
    assertThat(entries[1].toString(), is("Create +"));

    CharSequence[] entryValues = appendableListPreference.getEntryValues();
    assertThat(entryValues.length, is(2));

    assertThat(entryValues[0].toString(), is("default_user"));
    assertThat(entryValues[1].toString(), is("create_new"));
  }

  @Test
  public void shouldPreserveCreateItemOnAdd() {
    appendableListPreference.addDisplayEntries(profilesList);

    CharSequence[] entries = appendableListPreference.getEntries();
    assertThat(entries[entries.length-1].toString(), is("Create +"));

    CharSequence[] entryValues = appendableListPreference.getEntryValues();
    assertThat(entryValues[entryValues.length-1].toString(), is("create_new"));
  }

  @Test
  public void profileEntriesShouldHaveCorrespondingEntryValuesForList() {
    appendableListPreference.addDisplayEntries(profilesList);

    CharSequence[] entries = appendableListPreference.getEntries();

    assertThat(entries[1].toString(), is("Profile 1"));
    assertThat(entries[2].toString(), is("Profile 2"));
    assertThat(entries[3].toString(), is("Profile 3"));

    CharSequence[] entryValues = appendableListPreference.getEntryValues();

    assertThat(entryValues[1].toString(), is("profile_1"));
    assertThat(entryValues[2].toString(), is("profile_2"));
    assertThat(entryValues[3].toString(), is("profile_3"));
  }
}
