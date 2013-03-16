package au.com.dius.resilience.persistence.repository;

import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;

@Deprecated
public interface PhotoRepository {
  void save(RepositoryCommandResultListener<Incident> listener, Photo object, Incident incident);

  @Deprecated
  void findByIncident(RepositoryCommandResultListener<Photo> listener, Incident incident);
}
