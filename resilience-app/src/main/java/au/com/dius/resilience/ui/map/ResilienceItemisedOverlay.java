package au.com.dius.resilience.ui.map;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class ResilienceItemisedOverlay extends ItemizedOverlay {

  private List<OverlayItem> overlays = new ArrayList<OverlayItem>();
  private Context context;

  public ResilienceItemisedOverlay(Context context, Drawable defaultMarker) {
    super(boundCenterBottom(defaultMarker));
    this.context = context;
  }

  public void addOverlay(OverlayItem overlayItem) {
    overlays.add(overlayItem);
    populate();
  }

  @Override
  protected OverlayItem createItem(int i) {
    return overlays.get(i);
  }

  @Override
  public int size() {
    return overlays.size();
  }

  @Override
  protected boolean onTap(int index) {
    OverlayItem item = overlays.get(index);
    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
    dialog.setTitle(item.getTitle());
    dialog.setMessage(item.getSnippet());
    dialog.show();
    return true;
  }
}