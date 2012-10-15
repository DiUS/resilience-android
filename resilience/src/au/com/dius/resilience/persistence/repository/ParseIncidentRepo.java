package au.com.dius.resilience.persistence.repository;

import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.RepositoryCommandResultListener;
import roboguice.inject.ContextSingleton;

/**
 * @author georgepapas
 */
@ContextSingleton
public class ParseIncidentRepo implements IncidentRepository {

  @Override
  public void findById(RepositoryCommandResultListener<Incident> listener, long id) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public void save(RepositoryCommandResultListener<Incident> listener, Incident incident) {

  }

  @Override
  public void findAll(RepositoryCommandResultListener<Incident> listener) {

  }

  @Override
  public void findClosest(RepositoryCommandResultListener<Incident> listener) {

  }

  @Override
  public void findTracked(RepositoryCommandResultListener<Incident> listener) {

  }

}
