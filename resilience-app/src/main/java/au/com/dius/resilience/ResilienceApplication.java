package au.com.dius.resilience;

import android.app.Application;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import com.parse.Parse;

import static au.com.dius.resilience.Constants.PREFERENCES_FILE_COMMON;

public class ResilienceApplication extends Application {

  private static final boolean SHOULD_ALWAYS_LOAD_DEFAULT_PREFS = false;

  @Override
  public void onCreate() {
    PreferenceManager.setDefaultValues(this, R.xml.user_preferences, SHOULD_ALWAYS_LOAD_DEFAULT_PREFS);
    PreferenceManager.setDefaultValues(this, R.xml.common_preferences, SHOULD_ALWAYS_LOAD_DEFAULT_PREFS);

    String appKey = getResources().getString(R.string.key_parse_application);
    String clientKey = getResources().getString(R.string.key_parse_client);
    Parse.initialize(this, appKey, clientKey);
    // TODO - set strict mode in properties file?
    setStrictMode();
  }

  private void setStrictMode() {
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
