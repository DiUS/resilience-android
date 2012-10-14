  package au.com.dius.resilience.persistence;

import java.util.List;

import android.content.Context;
import au.com.dius.resilience.model.Photo;

public class SqlLitePhotoRepository extends AbstractSqlLiteRepository<Photo> implements Repository<Photo> {

  public SqlLitePhotoRepository(Context context) {
    super(context);
  }
  
  @Override
  public boolean save(Photo object) {
    return false;
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
