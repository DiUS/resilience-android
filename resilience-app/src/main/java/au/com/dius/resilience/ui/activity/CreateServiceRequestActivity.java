package au.com.dius.resilience.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import au.com.dius.resilience.R;
import au.com.dius.resilience.actionbar.ActionBarHandler;
import au.com.dius.resilience.factory.MediaFileFactory;
import au.com.dius.resilience.loader.ImageLoader;
import au.com.dius.resilience.model.MediaType;
import au.com.dius.resilience.model.ServiceListDefaults;
import au.com.dius.resilience.ui.adapter.ServiceListSpinnerAdapter;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import java.io.File;

@ContentView(R.layout.activity_create_service_request)
public class CreateServiceRequestActivity extends RoboActivity {

  public static final int CAPTURE_PHOTO_REQUEST_CODE = 100;

  @Inject
  private ActionBarHandler actionBarHandler;

  @InjectView(R.id.service_spinner)
  private Spinner serviceSpinner;

  @InjectView(R.id.photo_preview_image)
  private ImageView photoPreview;

  @Inject
  private MediaFileFactory mediaFileFactory;

  @Inject
  private ImageLoader imageLoader;

  private ServiceListSpinnerAdapter serviceListSpinnerAdapter;

  private Uri cachedPhotoUri;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupAdapter();
  }

  private void setupAdapter() {
    serviceListSpinnerAdapter = new ServiceListSpinnerAdapter(this);
    serviceListSpinnerAdapter.setData(ServiceListDefaults.DEFAULT_SERVICES);
    serviceSpinner.setAdapter(serviceListSpinnerAdapter);
  }


  public void onCameraButtonClick(View button) {
    File mediaFile = mediaFileFactory.createMediaFile(MediaType.PHOTO);

    if (mediaFile == null) {
      Toast.makeText(this, "Couldn't save photo to storage!", Toast.LENGTH_LONG);
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
}