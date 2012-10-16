package au.com.dius.resilience.persistence.repository;

import au.com.dius.resilience.model.Incident;

/**
 * @author georgepapas
 */
public interface IncidentRepository {

  void findById(RepositoryCommandResultListener<Incident> listener, String id);
  void save(RepositoryCommandResultListener<Incident> listener, Incident incident);
  void saveAll(RepositoryCommandResultListener<Incident> listener, Incident... incidents);
  void findAll(RepositoryCommandResultListener<Incident> listener);
  void findClosest(RepositoryCommandResultListener<Incident> listener);
  void findTracked(RepositoryCommandResultListener<Incident> listener);
}
