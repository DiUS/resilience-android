package au.com.dius.resilience.ui.activity;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import au.com.dius.resilience.R;
import au.com.justinb.open311.model.ServiceRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import java.util.List;

//@ContentView(R.layout.activity_map_view)
public class MapViewActivity extends Activity implements LoaderManager.LoaderCallbacks<List<ServiceRequest>> {

  private GoogleMap map;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_map_view);

    if (map == null) {
      map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

      map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }
  }

  @Override
  public Loader<List<ServiceRequest>> onCreateLoader(int id, Bundle args) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void onLoadFinished(Loader<List<ServiceRequest>> loader, List<ServiceRequest> data) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void onLoaderReset(Loader<List<ServiceRequest>> loader) {
    //To change body of implemented methods use File | Settings | File Templates.
  }
}
