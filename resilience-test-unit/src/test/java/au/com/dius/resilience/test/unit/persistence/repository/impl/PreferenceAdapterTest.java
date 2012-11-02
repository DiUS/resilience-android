package au.com.dius.resilience.test.unit.persistence.repository.impl;

import android.app.Activity;
import android.content.SharedPreferences;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Profile;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter.PREFERENCES_FILE_PREFIX;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class PreferenceAdapterTest {

  public static final int ARBITRARY_RESOURCE_KEY = R.string.contribute;
  private PreferenceAdapter preferenceAdapter;
  private Activity context;
  private SharedPreferences commonSharedPreferences;

  @Before
  public void setUp() {
    context = new Activity();
    preferenceAdapter = new PreferenceAdapter(context);
    commonSharedPreferences = context.getSharedPreferences(PreferenceAdapter.PREFERENCES_FILE_COMMON, MODE_PRIVATE);
  }

  @Test
  public void shouldSelectDefaultUserProfileWhenNoUserSpecified() {
    Profile currentProfile = preferenceAdapter.getCurrentProfile();
    assertThat(currentProfile.getName(), is(Constants.DEFAULT_USER_KEY));
  }

  @Test
  public void shouldUpdateCurrentUserOnCurrentUserPreferenceChange() {
    setCurrentUser("new_user");
    assertThat(preferenceAdapter.getCurrentProfile().getName(), is("new_user"));

    setCurrentUser("another_user");
    assertThat(preferenceAdapter.getCurrentProfile().getName(), is("another_user"));
  }

  private void setCurrentUser(String user) {
    SharedPreferences.Editor editor = commonSharedPreferences.edit();
    editor.putString(context.getString(R.string.current_profile_key), user);
    editor.commit();
  }

  @Test
  public void shouldUpdatePreferenceFileOnUserChange() {
    assertThat(preferenceAdapter.getCurrentUserPreferenceFile(), is(PREFERENCES_FILE_PREFIX + Constants.DEFAULT_USER_KEY));

    setCurrentUser("john_doe");

    assertThat(preferenceAdapter.getCurrentUserPreferenceFile(), is(PREFERENCES_FILE_PREFIX + "john_doe"));
  }

  @Test
  public void shouldReturnCommonPrefKeyValue() {
    setCurrentUser("current user from common prefs");

    String commonPreference = (String) preferenceAdapter.getCommonPreference(R.string.current_profile_key);
    assertThat(commonPreference, is("current user from common prefs"));

    String userPreference = (String) preferenceAdapter.getUserPreference(R.string.current_profile_key);
    assertNull(userPreference);
  }

  @Test
  public void shouldReturnUserPrefKeyValue() {
    SharedPreferences.Editor editor = context.getSharedPreferences(preferenceAdapter.getCurrentUserPreferenceFile(), MODE_PRIVATE).edit();
    editor.putInt(context.getString(R.string.response_radius_key), 10);
    editor.commit();

    Integer responseRadius = (Integer) preferenceAdapter.getUserPreference(R.string.response_radius_key);
    assertThat(responseRadius, is(10));

    String commonPreference = (String) preferenceAdapter.getCommonPreference(R.string.response_radius_key);
    assertNull(commonPreference);
  }

  @Test
  public void shouldSaveString() {
    preferenceAdapter.save(commonSharedPreferences, ARBITRARY_RESOURCE_KEY, "Some String");
    String preferenceValue1 = (String) preferenceAdapter.getCommonPreference(ARBITRARY_RESOURCE_KEY);
    assertThat(preferenceValue1, is("Some String"));

    preferenceAdapter.save(commonSharedPreferences, ARBITRARY_RESOURCE_KEY, "Some Other String");
    String preferenceValue2 = (String) preferenceAdapter.getCommonPreference(ARBITRARY_RESOURCE_KEY);
    assertThat(preferenceValue2, is("Some Other String"));
  }

  @Test
  public void shouldSaveBoolean() {
    preferenceAdapter.save(commonSharedPreferences, ARBITRARY_RESOURCE_KEY, true);
    Boolean preferenceValue1 = (Boolean) preferenceAdapter.getCommonPreference(ARBITRARY_RESOURCE_KEY);
    assertThat(preferenceValue1, is(true));

    preferenceAdapter.save(commonSharedPreferences, ARBITRARY_RESOURCE_KEY, false);
    Boolean preferenceValue2 = (Boolean) preferenceAdapter.getCommonPreference(ARBITRARY_RESOURCE_KEY);
    assertThat(preferenceValue2, is(false));
  }

  @Test
  public void shouldSaveLong() {
    preferenceAdapter.save(commonSharedPreferences, ARBITRARY_RESOURCE_KEY, 50L);
    Long preferenceValue1 = (Long) preferenceAdapter.getCommonPreference(ARBITRARY_RESOURCE_KEY);
    assertThat(preferenceValue1, is(50L));

    preferenceAdapter.save(commonSharedPreferences, ARBITRARY_RESOURCE_KEY, 100L);
    Long preferenceValue2 = (Long) preferenceAdapter.getCommonPreference(ARBITRARY_RESOURCE_KEY);
    assertThat(preferenceValue2, is(100L));
  }

  @Test
  public void shouldSaveFloat() {
    preferenceAdapter.save(commonSharedPreferences, ARBITRARY_RESOURCE_KEY, 60F);
    Float preferenceValue1 = (Float) preferenceAdapter.getCommonPreference(ARBITRARY_RESOURCE_KEY);
    assertThat(preferenceValue1, is(60F));

    preferenceAdapter.save(commonSharedPreferences, ARBITRARY_RESOURCE_KEY, 120F);
    Float preferenceValue2 = (Float) preferenceAdapter.getCommonPreference(ARBITRARY_RESOURCE_KEY);
    assertThat(preferenceValue2, is(120F));
  }

  @Test
  public void shouldSaveInteger() {
    preferenceAdapter.save(commonSharedPreferences, ARBITRARY_RESOURCE_KEY, 1);
    Integer preferenceValue1 = (Integer) preferenceAdapter.getCommonPreference(ARBITRARY_RESOURCE_KEY);
    assertThat(preferenceValue1, is(1));

    preferenceAdapter.save(commonSharedPreferences, ARBITRARY_RESOURCE_KEY, 2);
    Integer preferenceValue2 = (Integer) preferenceAdapter.getCommonPreference(ARBITRARY_RESOURCE_KEY);
    assertThat(preferenceValue2, is(2));
  }

// TODO - There appears to be a bug with Roboguice and putStringSet.
// reading/writing works on the emulator but not here. Need to come back and fix this (hopefully they'll patch it for us!)
// @Test
//  public void shouldSaveStringSet() {
//    Set<String> stringSet1 = new LinkedHashSet<String>();
//    stringSet1.add("Value 1");
//    stringSet1.add("Value 2");
//    stringSet1.add("Value 3");
//
//    SharedPreferences.Editor editor = commonSharedPreferences.edit();
//    editor.putStringSet("Contribute", stringSet1);
//    editor.commit();
//
//    Set<String> preferenceValue1 = (Set<String>) preferenceAdapter.getCommonPreference(ARBITRARY_RESOURCE_KEY);
//    assertThat(preferenceValue1, hasItem("Value 1"));
//    assertThat(preferenceValue1, hasItem("Value 2"));
//    assertThat(preferenceValue1, hasItem("Value 3"));
//
//    Set<String> stringSet2 = new HashSet<String>();
//    stringSet2.add("Another Value 1");
//    stringSet2.add("Another Value 2");
//    stringSet2.add("Another Value 3");
//
//    preferenceAdapter.save(commonSharedPreferences, ARBITRARY_RESOURCE_KEY, stringSet2);
//    Set<String> preferenceValue2 = (Set<String>) preferenceAdapter.getCommonPreference(ARBITRARY_RESOURCE_KEY);
//    assertThat(preferenceValue2, hasItem("Another Value 1"));
//    assertThat(preferenceValue2, hasItem("Another Value 2"));
//    assertThat(preferenceValue2, hasItem("Another Value 3"));
//  }
}
