package au.com.dius.resilience;

import android.app.Application;
import android.os.StrictMode;

import com.parse.Parse;

public class ResilienceApplication extends Application {

  @Override
  public void onCreate() {
    
    String appKey = null;
    String clientKey = null;
    if (RuntimeProperties.useLiveDb()) {
      throw new RuntimeException("We don't have production Parse keys yet.");
    }
    else {
      appKey = getResources().getString(R.string.key_parse_application);
      clientKey = getResources().getString(R.string.key_parse_client);
    }
    
    Parse.initialize(this, appKey, clientKey);
   
//    setStrictMode();
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
