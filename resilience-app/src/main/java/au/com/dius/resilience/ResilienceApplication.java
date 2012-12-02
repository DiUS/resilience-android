package au.com.dius.resilience;

import android.app.Application;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import com.parse.Parse;

public class ResilienceApplication extends Application {

  private static final String LOG_TAG = "ResilienceApplication";

  private static final boolean SHOULD_ALWAYS_LOAD_DEFAULT_PREFS = false;

  @Override
  public void onCreate() {
    setDefaultPreferences();
    initialiseParse();
    setStrictMode();
  }

  private void initialiseParse() {
    String appKey = getResources().getString(R.string.key_parse_application);
    String clientKey = getResources().getString(R.string.key_parse_client);

    Log.d(LOG_TAG, "Connecting to parse using " + appKey + " application key and " + clientKey + " client key");

    Parse.initialize(this, appKey, clientKey);
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
