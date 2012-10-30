package au.com.dius.resilience.test.unit.persistence.repository.impl;

import android.app.Activity;
import android.content.SharedPreferences;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Profile;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.MODE_PRIVATE;
import static au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter.DEFAULT_USER;
import static au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter.PREFERENCES_FILE_PREFIX;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class PreferenceAdapterTest {

  private PreferenceAdapter preferenceAdapter;
  private Activity context;

  @Before
  public void setUp() {
    context = new Activity();
    preferenceAdapter = new PreferenceAdapter(context);
  }

  @Test
  public void shouldSelectDefaultUserProfileWhenNoUserSpecified() {
    Profile currentProfile = preferenceAdapter.getCurrentProfile();
    assertThat(currentProfile.getName(), is(PreferenceAdapter.DEFAULT_USER));
  }

  @Test
  public void shouldUpdateCurrentUserOnCurrentUserPreferenceChange() {
    setCurrentUser("new_user");
    // Preference save is usually done via Preference framework
    assertThat(preferenceAdapter.getCurrentProfile().getName(), is("new_user"));

    setCurrentUser("another_user");
    assertThat(preferenceAdapter.getCurrentProfile().getName(), is("another_user"));
  }

  private void setCurrentUser(String user) {
    SharedPreferences.Editor editor = context.getSharedPreferences(PreferenceAdapter.PREFERENCES_FILE_COMMON, MODE_PRIVATE).edit();
    editor.putString(context.getString(R.string.current_profile_key), user);
    editor.commit();
  }

  @Test
  public void shouldUpdatePreferenceFileOnUserChange() {
    assertThat(preferenceAdapter.getCurrentUserPreferenceFile(), is(PREFERENCES_FILE_PREFIX + DEFAULT_USER));

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
}
