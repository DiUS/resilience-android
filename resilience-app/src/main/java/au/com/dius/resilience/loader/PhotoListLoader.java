package au.com.dius.resilience.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.observer.PhotoObserver;
import au.com.dius.resilience.persistence.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class PhotoListLoader extends AbstractAsyncListLoader<Photo> {

  public static final String PHOTO_LOADED = "INTENT_PHOTO_LOADED";

  private String incidentId;
  public static int PHOTO_LIST_LOADER = 1;

  public PhotoListLoader(Context context, Repository repository, String incidentId) {
    super(context, repository);
    this.incidentId = incidentId;
  }

  @Override
  protected BroadcastReceiver createBroadcastReceiver() {
    return new PhotoObserver(this);
  }

  @Override
  public List<Photo> loadInBackground() {
    List<Photo> photos = new ArrayList<Photo>(1);

    final Photo photo = repository.findPhotoByIncident(incidentId);
    if (photo != null) {
      photos.add(photo);
    }

    return photos;
  }
}
