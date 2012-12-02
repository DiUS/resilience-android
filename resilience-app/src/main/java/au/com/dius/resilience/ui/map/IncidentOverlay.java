package au.com.dius.resilience.ui.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Point;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.ui.activity.ViewIncidentActivity;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

import java.util.ArrayList;
import java.util.List;

import static au.com.dius.resilience.Constants.EXTRA_INCIDENT;

public class IncidentOverlay extends BalloonItemizedOverlay {

  private List<OverlayItem> overlays = new ArrayList<OverlayItem>();
  private List<Incident> incidents = new ArrayList<Incident>();
  private Context context;
  private Incident tappedIncident;

  private Repository repository;

  // TODO - hackhackhack.
  private ImageBalloonOverlayView<OverlayItem> overlayItemImageBalloonOverlayView;

  public IncidentOverlay(Drawable defaultMarker, MapView mapView, Repository repository) {
    super(boundCenterBottom(defaultMarker), mapView);
    context = mapView.getContext();
    this.repository = repository;
  }

  public void populateWith(List<Incident> data) {
    for (Incident incident : data) {
      Point point = incident.getPoint();
      if (point != null) {
        GeoPoint geoPoint = new GeoPoint((int) (point.getLatitude() * 1E6), (int) (point.getLongitude() * 1E6));
        OverlayItem overlayItem = new OverlayItem(geoPoint, incident.getCategory(), incident.getNote());
        addOverlay(overlayItem);
        incidents.add(incident);
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
  public boolean onTap(int index) {
    tappedIncident = incidents.get(index);

    if (overlayItemImageBalloonOverlayView != null) {
      overlayItemImageBalloonOverlayView.setTappedIncident(tappedIncident);
    }

    return super.onTap(index);
  }

  @Override
  protected boolean onBalloonTap(int index, OverlayItem item) {
    Intent intent = new Intent(context, ViewIncidentActivity.class);
    intent.putExtra(EXTRA_INCIDENT, incidents.get(index));
    context.startActivity(intent);

    return true;
  }

  @Override
  protected BalloonOverlayView<OverlayItem> createBalloonOverlayView() {
  overlayItemImageBalloonOverlayView = new ImageBalloonOverlayView<OverlayItem>(getMapView().getContext(), getBalloonBottomOffset()
      , tappedIncident, repository);
    return overlayItemImageBalloonOverlayView;
  }

  @Override
  public OverlayItem createItem(int i) {
    return overlays.get(i);
  }

  @Override
  public int size() {
    return overlays.size();
  }
}