package au.com.dius.resilience.ui.activity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.R;
import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.loader.PhotoListLoader;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.receiver.IncidentTrackedBroadcastReceiver;
import au.com.dius.resilience.service.TrackIncidentService;
import au.com.dius.resilience.ui.Themer;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

import java.util.List;

public class ViewIncidentActivity extends RoboActivity implements LoaderManager.LoaderCallbacks<List<Photo>> {

  private static final String LOG_TAG = ViewIncidentActivity.class.getName();

  @InjectView(R.id.incident_name)
  private TextView name;

  @InjectView(R.id.incident_note)
  private TextView note;

  @InjectView(R.id.no_image_label)
  private TextView noImageLabel;

  @InjectView(R.id.incident_photo)
  private ImageView incidentPhoto;

  @InjectView(R.id.image_load_progress_bar)
  private ProgressBar progressBar;

  @Inject
  private Repository repository;

  private IncidentTrackedBroadcastReceiver incidentTrackedBroadcastReceiver;

  private Bitmap photoBitmap;

  private Incident incident;
  private Menu menu;

  @Override
  protected void onResume() {
    Log.d(LOG_TAG, "Registering broadcast receiver");
    this.registerReceiver(
            incidentTrackedBroadcastReceiver,
            new IntentFilter(Intents.RESILIENCE_INCIDENT_TRACKED));
    super.onRestart();
  }

  @Override
  protected void onPause() {
    Log.d(LOG_TAG, "Unregistering broadcast receiver");
    this.unregisterReceiver(incidentTrackedBroadcastReceiver);

    super.onPause();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Themer.applyCurrentTheme(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_incident);

    incident = (Incident) getIntent().getSerializableExtra("incident");
    Log.d(LOG_TAG, "Incident retrieved with name " + incident.getName());

    name.setText(incident.getName());
    note.setText(incident.getNote());

    getLoaderManager().initLoader(PhotoListLoader.PHOTO_LIST_LOADER, null, this);
    incidentTrackedBroadcastReceiver = new IncidentTrackedBroadcastReceiver(this);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.track_incident:
        trackIncident();
        break;
      case R.id.untrack_incident:
        Log.d(LOG_TAG, "Untrack incident requested");
        break;
      default:
        super.onOptionsItemSelected(item);
    }

    return true;
  }

  private void trackIncident() {
    startService(TrackIncidentService.createTrackingIntent(this, incident));
  }

  public void onImageClick(View view) {
    Intent intent = new Intent(this, PhotoViewActivity.class);
    intent.putExtra(Constants.EXTRA_PHOTO, photoBitmap);
    startActivity(intent);
  }

  public void onMapClick(View view) {
    Intent intent = new Intent(this, MapViewActivity.class);
    intent.putExtra(Constants.INCIDENT_POINT, incident.getPoint());
    startActivity(intent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    this.menu = menu;
    getMenuInflater().inflate(R.menu.activity_view_incident, menu);
    getActionBar().setDisplayShowTitleEnabled(false);
    updateTrackingMenu();
    return true;
  }

  @Override
  public Loader<List<Photo>> onCreateLoader(int i, Bundle bundle) {
    return new PhotoListLoader(this, repository, incident.getId());
  }

  @Override
  public void onLoadFinished(Loader<List<Photo>> listLoader, List<Photo> photos) {
    Log.d(LOG_TAG, "Photo loader finished");
    displayPhotos(photos);
  }

  @Override
  public void onLoaderReset(Loader<List<Photo>> listLoader) {
    noPhoto();
  }

  public void displayPhotos(List<Photo> photos) {
    Log.d(LOG_TAG, "Displaying photos");
    if (photos.size() == 0) {
      noPhoto();
    } else {
      Photo photo = photos.get(0);
      photoBitmap = photo.getBitmap();
      incidentPhoto.setImageBitmap(photoBitmap);
      incidentPhoto.setVisibility(View.VISIBLE);
    }

    progressBar.setVisibility(View.GONE);
  }

  private void noPhoto() {
    noImageLabel.setVisibility(View.VISIBLE);
  }


  public void incidentTracked(String incidentId) {
    Toast.makeText(this, "Incident being tracked", Toast.LENGTH_LONG).show();
    if (incident.getId().equals(incidentId)) {
      // HACK ALERT TODO, We need to use a loader to get a nice refresh/data load work
      incident.addTracker(getCurrentUserId());
    }
    updateTrackingMenu();
  }

  private void updateTrackingMenu() {
    if (incident.isTrackedBy(getCurrentUserId())) {
      menu.getItem(0).setEnabled(false);
      menu.getItem(1).setEnabled(true);
    } else {
      menu.getItem(0).setEnabled(true);
      menu.getItem(1).setEnabled(false);
    }
  }

  private String getCurrentUserId() {
    return ((TelephonyManager)this.getSystemService(TELEPHONY_SERVICE)).getDeviceId();
  }

}