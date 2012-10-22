package au.com.dius.resilience.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import au.com.dius.resilience.model.Point;
import com.google.inject.Inject;
import com.google.inject.Provider;
import de.akquinet.android.androlog.Log;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class ResilienceLocationService extends IntentService {

  public ResilienceLocationService() {
    super(ResilienceLocationService.class.getName());
  }
//
//  public static final String TAG = ResilienceLocationService.class.getName();
//
//  @Override
//  public Point getMostRecentLocation() {
//    getSystemService(LOCATION_SERVICE)
//    throw new UnsupportedOperationException("not yet implemented");
//  }
//
//  @Override
//  protected void onHandleIntent(Intent intent) {
//    //To change body of implemented methods use File | Settings | File Templates.
//  }

  @Override
  protected void onHandleIntent(Intent intent) {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}
