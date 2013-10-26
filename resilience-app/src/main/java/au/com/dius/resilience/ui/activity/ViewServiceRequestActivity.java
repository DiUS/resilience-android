package au.com.dius.resilience.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.inject.Inject;

import au.com.dius.resilience.R;
import au.com.dius.resilience.actionbar.ActionBarHandler;
import au.com.dius.resilience.factory.SerializableExtraFactory;
import au.com.dius.resilience.intent.Extras;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.loader.ImageLoader;
import au.com.dius.resilience.ui.ResilienceActionBarThemer;
import au.com.dius.resilience.util.ResilienceDateUtils;
import au.com.justinb.open311.GenericRequestAdapter;
import au.com.justinb.open311.Open311Exception;
import au.com.justinb.open311.model.ServiceRequest;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

//@ContentView(R.layout.activity_view_service_request)
public class ViewServiceRequestActivity extends RoboActivity {

  public static final int ZOOM_LEVEL = 12;

  @Inject
  private ResilienceActionBarThemer themer;

  @InjectView(R.id.title)
  private TextView title;

  @InjectView(R.id.time_reported)
  private TextView timeReported;

  @InjectView(R.id.description)
  private TextView description;

  @InjectView(R.id.view_service_request_preview_image)
  private ImageView previewImage;

  @Inject
  private ImageLoader imageLoader;

  @Inject
  private ResilienceDateUtils dateUtils;

  @Inject
  private SerializableExtraFactory extraFactory;

  @Inject
  private ActionBarHandler actionBarHandler;

  private ServiceRequest serviceRequest;

  private GoogleMap map;

  private AlertDialog.Builder imageAlert;

  private GenericRequestAdapter<ServiceRequest> requestAdapter;


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_view_service_request);

    serviceRequest = (ServiceRequest) extraFactory.createSerializable(this, Extras.SERVICE_REQUEST);

    if (map == null) {
      // TODO - should this be in onResume? Look at docs to verify.
      MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
      if (mapFragment != null) {
        map = mapFragment.getMap();

        if (map != null) {
          map.getUiSettings().setZoomControlsEnabled(false);
          map.getUiSettings().setAllGesturesEnabled(false);

          LatLng latLng = new LatLng(serviceRequest.getLat(), serviceRequest.getLong());
          CameraPosition cameraPosition = new CameraPosition.Builder()
                  .target(latLng)
                  .zoom(ZOOM_LEVEL)
                  .build();
          map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
          map.addMarker(new MarkerOptions().position(latLng));
        }
      }
    }

    imageAlert = new AlertDialog.Builder(this, android.R.style.Theme_Translucent_NoTitleBar);
    imageAlert.setNeutralButton(R.string.dismiss, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int buttonId) {
        dialog.dismiss();
      }
    });

    requestAdapter = new GenericRequestAdapter<ServiceRequest>(ServiceRequest.class);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.action_bar_resolve, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return actionBarHandler.handleMenuItemSelected(item);
  }

  @Override
  public void onResume() {

    title.setText(serviceRequest.getAddress());
    timeReported.setText(dateUtils.formatRelativeDate(serviceRequest.getRequestedDatetime()));
    description.setText(serviceRequest.getDescription());

    imageLoader.loadFullsizeImage(previewImage, serviceRequest.getMediaUrl());

    super.onResume();
  }

  public void onResolveClick(final View button) {

    final Activity finalActivity = this;
    AsyncTask.execute(new Runnable() {
      @Override
      public void run() {
        try {
          button.setClickable(false);
          serviceRequest.updateStatus("closed", "Solved it single-handedly using a toothpick and some twine.");
          requestAdapter.update(serviceRequest.getServiceRequestId(), serviceRequest);

          Intent refreshIntent = new Intent(Intents.RESILIENCE_INCIDENT_CREATED);
          finalActivity.sendBroadcast(refreshIntent);

          finalActivity.finish();
        } catch (Open311Exception e) {
          Crouton.showText(finalActivity, "Failed to resolve incident, try again later.", Style.ALERT);
        }
      }
    });
  }

  public void onImageClick(View image) {

    ImageView view = new ImageView(this);
    view.setScaleType(ImageView.ScaleType.CENTER_CROP);

    imageLoader.loadFullsizeImage(view, serviceRequest.getMediaUrl());

    imageAlert.setView(view);
    imageAlert.show();
  }
}