package au.com.dius.resilience.ui;

import android.content.Context;
import au.com.dius.resilience.R;

import static android.content.Context.MODE_PRIVATE;
import static au.com.dius.resilience.Constants.PREFERENCES_FILE_COMMON;

public class Themer {
  public static void applyCurrentTheme(Context context) {
    Context appContext = context.getApplicationContext();
    boolean useLightTheme = appContext.getSharedPreferences(PREFERENCES_FILE_COMMON, MODE_PRIVATE).getBoolean("use_light_theme", true);

    if (useLightTheme) {
      context.setTheme(R.style.light_theme);
    } else {
      context.setTheme(R.style.dark_theme);
    }
  }
}
