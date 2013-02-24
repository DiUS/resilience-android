package au.com.dius.resilience.test.unit.map

import android.content.Context
import android.graphics.drawable.Drawable
import au.com.dius.resilience.ui.map.IncidentOverlay
import com.google.android.maps.GeoPoint
import com.google.android.maps.MapView
import com.google.android.maps.OverlayItem
import pl.polidea.robospock.RoboSpecification
import spock.lang.Shared

class IncidentOverlaySpockTest extends RoboSpecification {

  def overlayItem = new OverlayItem(new GeoPoint(0, 0), "", "");
  def mapView = new MapView(Mock(Context), "")
  def drawable = Mock(Drawable)
  def overlay = new IncidentOverlay(drawable, mapView, null)

  def "should be able to add overlays"() {
    // Implicit setup block here:
    overlay.size() == 0
    when:
      overlay.addOverlay(overlayItem);
    then:
      overlay.size() == 1
      overlay.createItem(0) == overlayItem
  }

  def "overlay should indicate it has items"() {
    // Example of helper method
    doesNot(overlay.hasItems())
    when:
      overlay.addOverlay(overlayItem)
    then:
      overlay.hasItems()
  }

  def doesNot(val) {
    !val
  }
}
