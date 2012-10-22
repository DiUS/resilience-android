package au.com.dius.resilience.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.persistence.repository.PhotoRepository;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import au.com.dius.resilience.ui.dialog.FullscreenImageDialog;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class ViewIncidentActivity extends RoboActivity implements RepositoryCommandResultListener<Photo> {

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
  private PhotoRepository photoRepository;

  private Bitmap photoBitmap;

  private Incident incident;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_incident);

    incident = (Incident) getIntent().getSerializableExtra("incident");
    Log.d(LOG_TAG, "Incident retrieved with name " + incident.getName());

    name.setText(incident.getName());
    note.setText(incident.getNote());

    photoRepository.findByIncident(this, incident);
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
    getMenuInflater().inflate(R.menu.activity_view_incident, menu);
    return true;
  }

  @Override
  public void commandComplete(RepositoryCommandResult<Photo> result) {
    if (result.getResults().size() == 0) {
      noImageLabel.setVisibility(View.VISIBLE);
    }
    else {
      Photo photo = result.getResults().get(0);
      photoBitmap = photo.getBitmap();
      incidentPhoto.setImageBitmap(photoBitmap);
      incidentPhoto.setVisibility(View.VISIBLE);
    }

    progressBar.setVisibility(View.GONE);
  }
}