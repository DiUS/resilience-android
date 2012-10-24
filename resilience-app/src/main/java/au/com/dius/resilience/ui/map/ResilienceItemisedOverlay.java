package au.com.dius.resilience.ui.map;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

import java.util.ArrayList;
import java.util.List;

public class ResilienceItemisedOverlay extends BalloonItemizedOverlay {

  private List<OverlayItem> overlays = new ArrayList<OverlayItem>();
  private Context context;

  public ResilienceItemisedOverlay(Drawable defaultMarker, MapView mapView) {
    super(boundCenter(defaultMarker), mapView);
    context = mapView.getContext();
  }

  public void addOverlay(OverlayItem overlayItem) {
    overlays.add(overlayItem);
    populate();
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