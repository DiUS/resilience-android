package au.com.dius.resilience.ui;

import android.content.Context;
import au.com.dius.resilience.R;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import com.google.inject.Inject;

public class Themer {

  @Inject
  public Themer(Context context) {
    updateTheme(context);
  }

  private void updateTheme(Context context) {
    PreferenceAdapter preferenceAdapter = new PreferenceAdapter(context.getApplicationContext());
    Boolean useLightTheme = (Boolean) preferenceAdapter.getCommonPreference(R.string.use_light_theme_key);

    if (useLightTheme) {
      context.setTheme(R.style.light_theme);
    } else {
      context.setTheme(R.style.dark_theme);
    }
  }
}
