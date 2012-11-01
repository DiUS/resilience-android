package au.com.dius.resilience.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import au.com.dius.resilience.R;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import au.com.dius.resilience.ui.Themer;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.util.Set;

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

    if (getIntent().getExtras() == null) {
      profileName.setText(defaultProfileName);
    }

    preferenceAdapter = new PreferenceAdapter(this);
  }

  public void onSaveProfileClick(View button) {
    String profileName = this.profileName.getText().toString();

    if (profileName.length() == 0) {
      return;
    }

    String profileKey = profileName.toLowerCase().replaceAll(" ", "_");
    Set<String> profileEntries = (Set<String>) preferenceAdapter.getCommonPreference(R.string.profile_entries);

    if (profileEntries.contains(profileName) || profileEntries.contains(profileKey)) {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setPositiveButton(android.R.string.ok, null);
      builder.setMessage(R.string.profile_exists_error);
      builder.show();
    } else {
      profileEntries.add(profileName);

      preferenceAdapter.save(preferenceAdapter.getCommonPreferences(), R.string.profile_entries, profileEntries);

      Intent preferencesIntent = new Intent(this, ResiliencePreferenceActivity.class);
      startActivity(preferencesIntent);
    }
  }
}