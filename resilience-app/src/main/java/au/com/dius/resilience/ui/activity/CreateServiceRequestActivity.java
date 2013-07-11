package au.com.dius.resilience.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import au.com.dius.resilience.R;
import au.com.dius.resilience.actionbar.ActionBarHandler;
import au.com.dius.resilience.factory.MediaFileFactory;
import au.com.dius.resilience.loader.ImageLoader;
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
import com.cloudinary.Cloudinary;
import com.google.inject.Inject;
import com.squareup.otto.Subscribe;
import org.apache.commons.lang.StringUtils;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectFragment;
import roboguice.inject.InjectView;

import java.io.File;
import java.util.Map;

@ContentView(R.layout.activity_create_service_request)
public class CreateServiceRequestActivity extends RoboFragmentActivity {

  public static final int CAPTURE_PHOTO_REQUEST_CODE = 100;
  public static final String API_KEY = "api_key";
  public static final String API_SECRET = "api_secret";
  public static final String PUBLIC_ID = "public_id";
  public static final String JPG = ".jpg";

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

  @InjectView(R.id.submit_button)
  private Button submitButton;

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

    descriptionField.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        descriptionField.setError(null);
        return false;
      }
    });

    requestAdapter = new GenericRequestAdapter<ServiceRequest>(ServiceRequest.class);
  }

  @Subscribe
  public void onLocationUpdatedEvent(LocationUpdatedEvent event) {
    lastKnownLocation = event.getLocation();
  }

  @Override
  public void onResume() {
    super.onResume();
    setStateEnabled();
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

  // TODO - clean up.
  public void onSubmitClick(final View view) {

    if (StringUtils.isEmpty(descriptionField.getText().toString())) {
      descriptionField.setError("Can't be blank");
      return;
    }

    if (cachedPhotoUri == null) {
      Toast.makeText(this, "You need to attach a photo to the issue.", Toast.LENGTH_SHORT).show();
      return;
    }

    if (lastKnownLocation == null) {
      Toast.makeText(this, "Can't resolve your location, please check your location services.", Toast.LENGTH_SHORT).show();
      setStateEnabled();
      return;
    }

    setStateUploading();

    final Activity finalThis = this;
    AsyncTask.execute(new Runnable() {
      @Override
      public void run() {
        Cloudinary cloudinary = new Cloudinary();
        cloudinary.setConfig(ImageLoader.CLOUD_NAME, getString(R.string.cloudinary_cloud_name));
        cloudinary.setConfig(API_KEY, getString(R.string.cloudinary_api_key));
        cloudinary.setConfig(API_SECRET, getString(R.string.cloudinary_api_secret));

        try {
          Map result = cloudinary.uploader().upload(new File(cachedPhotoUri.getPath()), Cloudinary.emptyMap());
          String publicId = (String) result.get(PUBLIC_ID);

          submitServiceRequest(cloudinary.url().generate(publicId + JPG));

        } catch (Exception e) {
          Toast.makeText(finalThis, "Photo upload failed.", Toast.LENGTH_LONG).show();
        }
      }
    });
  }

  private void setStateUploading() {
    submitButton.setText(R.string.uploading);
    submitButton.setEnabled(false);
    descriptionField.setEnabled(false);
    photoPreview.setEnabled(false);
    serviceSpinner.setEnabled(false);

  }

  private void setStateEnabled() {
    submitButton.setText(R.string.submit);
    serviceSpinner.setEnabled(true);
    submitButton.setEnabled(true);
    descriptionField.setEnabled(true);
    photoPreview.setEnabled(true);
  }

  private void submitServiceRequest(String imageUrl) {
    final ServiceRequest.Builder builder = new ServiceRequest.Builder();

    ServiceList serviceList = (ServiceList) serviceSpinner.getSelectedItem();
    builder.serviceCode(serviceList.getServiceCode())
      .serviceName(serviceList.getServiceName())
      .description(descriptionField.getText().toString())
      .latitude(lastKnownLocation.getLatitude())
      .longtitude(lastKnownLocation.getLongitude())
      .mediaUrl(imageUrl);

    final Activity finalThis = this;
    AsyncTask.execute(new Runnable() {
      @Override
      public void run() {
        try {
          requestAdapter.create(builder.createServiceRequest());
          finalThis.finish();
        } catch (Open311Exception e) {
          Toast.makeText(finalThis, "Failed to create Service Request", Toast.LENGTH_LONG).show();
          setStateEnabled();
          Logger.e(this, "Exception while creating service request: " + e.getMessage());
        }
      }
    });
  }
}