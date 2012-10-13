package au.com.dius.resilience.persistence;

import java.util.List;

import au.com.dius.resilience.model.Photo;

public class PhotoRepository implements Repository<Photo> {

  public PhotoRepository() {
  }

  @Override
  public boolean save(Photo photo) {
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
