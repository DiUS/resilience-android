package au.com.dius.resilience.persistence;

import java.util.List;

import au.com.dius.resilience.model.Photo;

public class PhotoRepository implements Repository<Photo> {

  @Override
  public void save(Photo incident) {

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
