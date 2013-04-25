package au.com.dius.resilience.observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import au.com.dius.resilience.intent.Extras;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

// TODO - test.
public class LocationUpdatedMapBroadcastReceiver extends BroadcastReceiver {

  private final GoogleMap map;

  public LocationUpdatedMapBroadcastReceiver(GoogleMap aMap) {
    map = aMap;
  }

  @Override
  public void onReceive(Context context, Intent intent) {

    Location location = (Location) intent.getExtras().get(Extras.LOCATION);
    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
  }
}
