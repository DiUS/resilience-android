package au.com.dius.resilience.persistence.repository.impl;

import android.content.Context;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Profile;

import java.util.LinkedHashSet;
import java.util.Set;

public class ProfileRepository {
  PreferenceAdapter preferenceAdapter;

  public ProfileRepository(Context context) {
    preferenceAdapter = new PreferenceAdapter(context);
  }

  public Set<Profile> findAll() {
    Set<String> profileEntries = (Set<String>) preferenceAdapter.getCommonPreference(R.string.profile_entries);

    Set<Profile> profiles = new LinkedHashSet<Profile>();
    if (profileEntries == null) {
      return profiles;
    }

    for (String entryNames : profileEntries) {
      profiles.add(new Profile(entryNames));
    }

    return profiles;
  }
}
