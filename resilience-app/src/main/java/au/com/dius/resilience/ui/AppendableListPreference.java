package au.com.dius.resilience.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.preference.ListPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Profile;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import au.com.dius.resilience.persistence.repository.impl.ProfileRepository;
import au.com.dius.resilience.ui.activity.ManageProfileActivity;

import java.util.*;

import static au.com.dius.resilience.Constants.DEFAULT_USERNAME;

public class AppendableListPreference extends ListPreference implements Preference.OnPreferenceChangeListener {

  public static final String CREATE_NEW = "create_new";
  public static final boolean DO_UPDATE_VALUE = true;

  private ProfileRepository repository;

  private static final Profile DEFAULT_PROFILE = new Profile(DEFAULT_USERNAME);

  public AppendableListPreference(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
    repository = new ProfileRepository(context);
  }

  @Override
  public void onPrepareDialogBuilder(AlertDialog.Builder builder) {
    addDisplayEntries(repository.findAll());

    setOnPreferenceChangeListener(this);
    super.onPrepareDialogBuilder(builder);
  }

  public void addDisplayEntries(Set<Profile> profiles) {
    List<CharSequence> displayEntryList = new ArrayList<CharSequence>();
    List<CharSequence> displayValueList = new ArrayList<CharSequence>();

    displayValueList.add(DEFAULT_PROFILE.getId());
    displayEntryList.add(DEFAULT_PROFILE.getName());

    for (Profile profile : profiles) {
      displayValueList.add(profile.getId());
      displayEntryList.add(profile.getName());
    }

    displayValueList.add(CREATE_NEW);
    displayEntryList.add(getContext().getString(R.string.create_plus));

    setEntryValues(displayValueList.toArray(new CharSequence[displayValueList.size()]));
    setEntries(displayEntryList.toArray(new CharSequence[displayEntryList.size()]));
  }

  @Override
  public boolean onPreferenceChange(Preference preference, Object newValue) {
    if (CREATE_NEW.equals(newValue)) {
      Intent intent = new Intent(getContext(), ManageProfileActivity.class);
      getContext().startActivity(intent);

      return false;
    }

    return DO_UPDATE_VALUE;
  }
}
