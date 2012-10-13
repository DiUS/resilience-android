package au.com.dius.resilience.persistence;

import android.content.Context;
import android.net.Uri;
import au.com.dius.resilience.model.Photo;

import java.util.List;

public class PhotoRepository implements Repository<Photo> {

  private Context context;

  public PhotoRepository(Context context) {
    this.context = context;
  }

  @Override
  public boolean save(Photo incident) {

    Uri filesystemDestination = Photo.getOutputMediaFile();
    return true; //TODO make this return the correct value...
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