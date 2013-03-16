package au.com.dius.resilience.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationChangedReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    String locationKey = LocationManager.KEY_LOCATION_CHANGED;
    String providerEnabledKey = LocationManager.KEY_PROVIDER_ENABLED;
    if (intent.hasExtra(providerEnabledKey)) {
      if (!intent.getBooleanExtra(providerEnabledKey, true)) {
        Log.d("XXXX", "Provider disabled");
//        Intent providerDisabledIntent = new Intent(PlacesConstants.ACTIVE_LOCATION_UPDATE_PROVIDER_DISABLED);
//        context.sendBroadcast(providerDisabledIntent);
      } else {
        Log.d("XXXXX", "Provider enabled");
      }
    }

    if (intent.hasExtra(locationKey)) {
      Location location = (Location)intent.getExtras().get(locationKey);
      Log.d("XXXXXXXX", "location is " + location);
//      Log.d(TAG, "Actively Updating place list");
//      Intent updateServiceIntent = new Intent(context, PlacesConstants.SUPPORTS_ECLAIR ? EclairPlacesUpdateService.class : PlacesUpdateService.class);
//      updateServiceIntent.putExtra(PlacesConstants.EXTRA_KEY_LOCATION, location);
//      updateServiceIntent.putExtra(PlacesConstants.EXTRA_KEY_RADIUS, PlacesConstants.DEFAULT_RADIUS);
//      updateServiceIntent.putExtra(PlacesConstants.EXTRA_KEY_FORCEREFRESH, true);
//      context.startService(updateServiceIntent);
    }
  }
}
