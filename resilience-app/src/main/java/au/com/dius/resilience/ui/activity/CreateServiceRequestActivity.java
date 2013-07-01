package au.com.dius.resilience.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import au.com.dius.resilience.R;
import au.com.dius.resilience.actionbar.ActionBarHandler;
import au.com.dius.resilience.factory.MediaFileFactory;
import au.com.dius.resilience.location.LocationBroadcaster;
import au.com.dius.resilience.location.event.LocationUpdatedEvent;
import au.com.dius.resilience.model.MediaType;
import au.com.dius.resilience.model.ServiceListDefaults;
import au.com.dius.resilience.ui.adapter.ServiceListSpinnerAdapter;
import au.com.dius.resilience.ui.fragment.LocationResolverFragment;
import au.com.dius.resilience.util.Logger;
import au.com.justinb.open311.GenericRequestAdapter;
import au.com.justinb.open311.Open311Exception;
import au.com.justinb.open311.model.ServiceList;
import au.com.justinb.open311.model.ServiceRequest;
import com.google.inject.Inject;
import com.squareup.otto.Subscribe;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectFragment;
import roboguice.inject.InjectView;

import java.io.File;

@ContentView(R.layout.activity_create_service_request)
public class CreateServiceRequestActivity extends RoboFragmentActivity {

  public static final int CAPTURE_PHOTO_REQUEST_CODE = 100;

  @Inject
  private ActionBarHandler actionBarHandler;

  @InjectView(R.id.service_spinner)
  private Spinner serviceSpinner;

  @InjectView(R.id.photo_preview_image)
  private ImageView photoPreview;

  @InjectView(R.id.description_field)
  private EditText descriptionField;

  @InjectFragment(R.id.location_resolver_fragment)
  private LocationResolverFragment locationResolverFragment;

  @Inject
  private LocationBroadcaster locationBroadcaster;

  @Inject
  private MediaFileFactory mediaFileFactory;

  public GenericRequestAdapter<ServiceRequest> requestAdapter;

  private ServiceListSpinnerAdapter serviceListSpinnerAdapter;

  private Uri cachedPhotoUri;

  private Location lastKnownLocation;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupAdapter();

    locationBroadcaster.subscribe(this);
    locationBroadcaster.subscribe(locationResolverFragment);

    requestAdapter = new GenericRequestAdapter<ServiceRequest>(ServiceRequest.class);
  }

  @Subscribe
  public void onLocationUpdatedEvent(LocationUpdatedEvent event) {
    lastKnownLocation = event.getLocation();
  }

  private void setupAdapter() {
    serviceListSpinnerAdapter = new ServiceListSpinnerAdapter(this);
    serviceListSpinnerAdapter.setData(ServiceListDefaults.DEFAULT_SERVICES);
    serviceSpinner.setAdapter(serviceListSpinnerAdapter);
  }

  public void onCameraButtonClick(View button) {
    File mediaFile = mediaFileFactory.createMediaFile(MediaType.PHOTO);

    if (mediaFile == null) {
      Toast.makeText(this, "Couldn't save photo to storage!", Toast.LENGTH_LONG).show();
      return;
    }

    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, cachePhotoFilename(mediaFile));
    startActivityForResult(intent, CAPTURE_PHOTO_REQUEST_CODE);
  }

  private Uri cachePhotoFilename(File mediaFile) {
    cachedPhotoUri = Uri.fromFile(mediaFile);
    return cachedPhotoUri;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.action_bar_minimal, menu);

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    return actionBarHandler.handleMenuItemSelected(item);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode,
                                  Intent data) {

    if (requestCode == CAPTURE_PHOTO_REQUEST_CODE) {
      if (resultCode == RESULT_OK) {
        photoPreview.setImageURI(cachedPhotoUri);
      }
    }
  }

  public void onSubmitClick(final View button) {

    button.setEnabled(false);
    if (lastKnownLocation == null) {
      Toast.makeText(this, "Can't resolve your location, please check your location services.", Toast.LENGTH_SHORT).show();

      button.setEnabled(true);
      return;
    }

    final ServiceRequest.Builder builder = new ServiceRequest.Builder();

    ServiceList serviceList = (ServiceList) serviceSpinner.getSelectedItem();
    builder.serviceCode(serviceList.getServiceCode())
      .serviceName(serviceList.getServiceName())
      .description(descriptionField.getText().toString())
      .latitude(lastKnownLocation.getLatitude())
      .longtitude(lastKnownLocation.getLongitude());

    // Need to upload the photo before I can add this URL.
//    builder.mediaUrl(cachedPhotoUri.toString());

    // FIXME - 4** (and probably other error codes) seem to be ignored by my
    // FIXME - Open311 library. Gotta fix.
    final Activity finalThis = this;
    AsyncTask.execute(new Runnable() {
      @Override
      public void run() {
        try {
          requestAdapter.create(builder.createServiceRequest());
          finalThis.finish();
        } catch (Open311Exception e) {
          Toast.makeText(finalThis, "Failed to create Service Request", Toast.LENGTH_LONG).show();
          button.setEnabled(true);
          Logger.e(this, "Exception while creating service request: " + e.getMessage());
        }
      }
    });
  }
}