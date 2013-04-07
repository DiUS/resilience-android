package au.com.dius.resilience.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.TextView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.factory.SerializableExtraFactory;
import au.com.dius.resilience.intent.Extras;
import au.com.dius.resilience.ui.ResilienceActionBarThemer;
import au.com.dius.resilience.util.ResilienceDateUtils;
import au.com.justinb.open311.model.ServiceRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import java.io.Serializable;

@ContentView(R.layout.activity_view_service_request)
public class ViewServiceRequestActivity extends RoboActivity {

  @Inject
  private ResilienceActionBarThemer themer;

  @InjectView(R.id.title)
  private TextView title;

  @InjectView(R.id.time_reported)
  private TextView timeReported;

  @InjectView(R.id.description)
  private TextView description;

  private ServiceRequest serviceRequest;

  @Inject
  private ResilienceDateUtils dateUtils;

  @Inject
  private SerializableExtraFactory extraFactory;

  private GoogleMap map;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    serviceRequest = (ServiceRequest) extraFactory.createSerializable(this, Extras.SERVICE_REQUEST);

    if (map == null) {
      // TODO - should this be in onResume? Look at docs to verify.
      map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
      map.getUiSettings().setZoomControlsEnabled(false);
      map.getUiSettings().setAllGesturesEnabled(false);
    }
  }

  @Override
  public void onResume() {

    title.setText("Melbourne CBD");
    timeReported.setText(dateUtils.formatRelativeDate(serviceRequest.getRequestedDatetime()));
    description.setText(serviceRequest.getDescription());

    super.onResume();
  }
}