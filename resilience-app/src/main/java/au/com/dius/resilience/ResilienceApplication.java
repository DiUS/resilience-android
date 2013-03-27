package au.com.dius.resilience;

import android.app.Application;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import au.com.dius.resilience.persistence.repository.impl.PreferenceAdapter;
import au.com.justinb.open311.Open311;
import com.parse.Parse;
import org.restlet.engine.Engine;
import org.restlet.ext.jackson.JacksonConverter;

public class ResilienceApplication extends Application {

  private static final String LOG_TAG = "ResilienceApplication";

  private static final boolean SHOULD_ALWAYS_LOAD_DEFAULT_PREFS = false;

  @Override
  public void onCreate() {
    setDefaultPreferences();
    initialiseOpen311();
    setStrictMode();
  }

  private void initialiseOpen311() {
    Open311.setBaseUrl(getApplicationContext().getString(R.string.open_311_base_url));
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
