package au.com.dius.resilience;

import android.app.Application;

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
      appKey = Constants.TEST_APP_KEY;
      clientKey = Constants.TEST_CLIENT_KEY;
    }
    
    Parse.initialize(this, appKey, clientKey);
  }
}
