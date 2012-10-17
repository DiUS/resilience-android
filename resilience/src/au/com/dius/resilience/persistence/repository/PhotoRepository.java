package au.com.dius.resilience.persistence.repository;

import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;

/**
 * @author georgepapas
 */
public interface PhotoRepository {
  void save(RepositoryCommandResultListener<Incident> listener, Photo object);

  void findById(RepositoryCommandResultListener<Incident> listener, long id);
}