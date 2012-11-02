package au.com.dius.resilience.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Profile;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import au.com.dius.resilience.ui.Themer;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.util.LinkedHashSet;
import java.util.Set;

import static au.com.dius.resilience.observer.PreferenceChangeBroadcastReceiver.PREFERENCES_UPDATED_FILTER;

@ContentView(R.layout.activity_manage_profile)
public class ManageProfileActivity extends RoboActivity {

  @InjectView(R.id.profile_name)
  private EditText profileName;

  @InjectResource(R.string.default_new_profile)
  private String defaultProfileName;

  PreferenceAdapter preferenceAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Themer.applyCurrentTheme(this);
    super.onCreate(savedInstanceState);

    profileName.setText(defaultProfileName);
    preferenceAdapter = new PreferenceAdapter(this);
  }

  public void onSaveProfileClick(View button) {
    String profileName = this.profileName.getText().toString();

    Profile newProfile = new Profile(profileName);
    Set<String> profileEntries = (Set<String>) preferenceAdapter.getCommonPreference(R.string.profile_entries);

    if (profileEntries == null) {
      profileEntries = new LinkedHashSet<String>();
    }

    if (newProfile.getName().length() == 0 || profileEntries.contains(newProfile.getName())) {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setPositiveButton(android.R.string.ok, null);
      builder.setMessage(R.string.profile_exists_error);
      builder.show();
    } else {
      profileEntries.add(newProfile.getName());

      preferenceAdapter.save(preferenceAdapter.getCommonPreferences(), R.string.profile_entries, profileEntries);
      preferenceAdapter.save(preferenceAdapter.getCommonPreferences(), R.string.current_profile_key, newProfile.getId());

      sendBroadcast(new Intent(PREFERENCES_UPDATED_FILTER));
      Intent preferencesIntent = new Intent(this, ResiliencePreferenceActivity.class);
      startActivity(preferencesIntent);
    }
  }
}