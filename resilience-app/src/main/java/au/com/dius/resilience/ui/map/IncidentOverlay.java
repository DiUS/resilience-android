package au.com.dius.resilience.ui.map;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Point;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

import java.util.ArrayList;
import java.util.List;

public class IncidentOverlay extends BalloonItemizedOverlay {

  private List<OverlayItem> overlays = new ArrayList<OverlayItem>();
  private Context context;

  public IncidentOverlay(Drawable defaultMarker, MapView mapView) {
    super(boundCenterBottom(defaultMarker), mapView);
    context = mapView.getContext();
  }

  public void populateWith(List<Incident> data) {
    for (Incident incident : data) {
      Point point = incident.getPoint();
      if (point != null) {
        GeoPoint geoPoint = new GeoPoint((int) (point.getLatitude() * 1E6), (int) (point.getLongitude() * 1E6));
        OverlayItem overlayItem = new OverlayItem(geoPoint, incident.getCategory(), incident.getNote());
        addOverlay(overlayItem);
      }
    }
    populate();
  }

  public void addOverlay(OverlayItem overlayItem) {
    overlays.add(overlayItem);
  }

  public boolean hasItems() {
    return size() > 0;
  }

  @Override
  public OverlayItem createItem(int i) {
    return overlays.get(i);
  }

  @Override
  public int size() {
    return overlays.size();
  }

  @Override
  protected boolean onBalloonTap(int index, OverlayItem item) {
    return true;
  }
}