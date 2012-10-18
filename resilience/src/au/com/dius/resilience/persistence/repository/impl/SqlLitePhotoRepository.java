package au.com.dius.resilience.persistence.repository.impl;

import javax.inject.Provider;

import roboguice.inject.ContextSingleton;
import android.content.Context;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.persistence.repository.PhotoRepository;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;

import com.google.inject.Inject;

@ContextSingleton
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
  public void save(RepositoryCommandResultListener<Incident> listener, Photo object, Incident incident) {
    throw new UnsupportedOperationException("Not implemented.");
  }

  @Override
  public void findByIncident(RepositoryCommandResultListener<Photo> listener, Incident incident) {
    throw new UnsupportedOperationException("Not implemented.");
  }
}
