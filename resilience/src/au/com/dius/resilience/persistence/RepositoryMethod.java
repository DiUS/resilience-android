package au.com.dius.resilience.persistence;

import au.com.dius.resilience.model.Incident;

import java.util.List;

/**
 * @author georgepapas
 */
public interface RepositoryMethod {

  List<Incident> findAll(Repository repository);
}
