package au.com.dius.resilience.persistence.repository;

import android.content.Context;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.persistence.RepositoryCommandResultListener;
import com.google.inject.Inject;

import javax.inject.Provider;

public class SqlLitePhotoRepository extends AbstractSqlLiteRepository<Photo> implements PhotoRepository {

  @Inject
  public SqlLitePhotoRepository(Provider<Context> contextProvider) {
    super(contextProvider.get());
  }

  public boolean save(Photo object) {
    throw new UnsupportedOperationException("will be removed, call save(listener, photo) instead");
  }

  public Photo findById(long id) {
    throw new UnsupportedOperationException("will be removed, call findById(listner, id) instead");
  }

  @Override
  public void save(RepositoryCommandResultListener<Incident> listener, Photo object) {

  }

  @Override
  public void findById(RepositoryCommandResultListener<Incident> listener, long id) {

  }
}
