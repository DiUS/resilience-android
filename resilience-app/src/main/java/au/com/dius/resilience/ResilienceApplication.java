package au.com.dius.resilience;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import au.com.justinb.open311.Open311;

public class ResilienceApplication extends Application {

  private static final boolean SHOULD_ALWAYS_LOAD_DEFAULT_PREFS = false;

  @Override
  public void onCreate() {
    setDefaultPreferences();
    initialiseOpen311();
    setStrictMode();
  }

  private void initialiseOpen311() {
    Context applicationContext = getApplicationContext();
    Open311.setBaseUrl(applicationContext.getString(R.string.open_311_base_url));
    Open311.setBasicAuth(applicationContext.getString(R.string.open_311_username)
            , applicationContext.getString(R.string.open_311_password));
  }

  private void setDefaultPreferences() {
    PreferenceManager.setDefaultValues(this, PreferenceAdapter.PREFERENCES_FILE_COMMON, MODE_PRIVATE
      , R.xml.common_preferences, SHOULD_ALWAYS_LOAD_DEFAULT_PREFS);

    PreferenceManager.setDefaultValues(this, PreferenceAdapter.PREFERENCES_FILE_DEFAULT, MODE_PRIVATE
                                      , R.xml.user_preferences, SHOULD_ALWAYS_LOAD_DEFAULT_PREFS);
  }

  private void setStrictMode() {
    // TODO - set strict mode in properties file?
    if (RuntimeProperties.useStrictMode()) {
      StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
//            .detectNetwork()   // or .detectAll() for all detectable problems
        .detectAll()
        .penaltyLog()
        .build());

      StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectLeakedSqlLiteObjects()
        .detectLeakedClosableObjects()
        .penaltyLog()
        .penaltyDeath()
        .build());
    }
  }
}
