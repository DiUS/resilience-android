package au.com.dius.resilience.ui.activity;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import au.com.dius.resilience.Constants;
import au.com.dius.resilience.R;
import au.com.dius.resilience.loader.PhotoListLoader;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.ui.Themer;

import com.google.inject.Inject;

public class ViewIncidentActivity extends RoboActivity implements LoaderManager.LoaderCallbacks<List<Photo>> {

  private static final String LOG_TAG = ViewIncidentActivity.class.getName();

  @InjectView(R.id.activity_view_item_name)
  private TextView name;
  
  @InjectView(R.id.activity_view_item_time_reported)
  private TextView time;
  
  @InjectView(R.id.activity_view_item_location)
  private TextView location;

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
  
  private Bitmap photoBitmap;

  private Incident incident;
 
  @InjectView(R.id.email_btn)
  private Button emailButton;
  
  @InjectView(R.id.tweet_btn)
  private Button tweetButton;
  

  @Override
  public void onCreate(Bundle savedInstanceState) {
    Themer.applyCurrentTheme(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_incident);

    incident = (Incident) getIntent().getSerializableExtra("incident");
    Log.d(LOG_TAG, "Incident retrieved with name " + incident.getName());
    
    
    
    name.setText(incident.getName());
    time.setText(DateUtils.getRelativeDateTimeString(
            this,
            incident.getDateCreated().longValue(),
            DateUtils.SECOND_IN_MILLIS,
            DateUtils.YEAR_IN_MILLIS, 0));
    
    location.setText("Long :"+incident.getPoint().getLatitude()+"  Lat :"+ incident.getPoint().getLatitude());
    
    note.setText(incident.getNote());

    getLoaderManager().initLoader(PhotoListLoader.PHOTO_LIST_LOADER, null, this);
    
    emailButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Log.i("email", "Click the email button");
			
		}
	});
    
    tweetButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Log.i("email", "Click the tweet button");
			
		}
	});
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

}