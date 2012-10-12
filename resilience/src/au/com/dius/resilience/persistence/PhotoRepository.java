package au.com.dius.resilience.persistence;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import au.com.dius.resilience.model.Photo;

public class PhotoRepository implements Repository<Photo> {

  private Context context;

  public PhotoRepository(Context context) {
    this.context = context;
  }

  @Override
  public void save(Photo incident) {

    Uri filesystemDestination = Photo.getOutputMediaFile();
  }

  @Override
  public Photo findById(long id) {
    return null;
  }

  @Override
  public List<Photo> findAll() {
    return null;
  }
}