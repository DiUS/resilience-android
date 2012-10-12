package au.com.dius.resilience.persistence;

import java.util.List;

import au.com.dius.resilience.model.Incident;

/**
 * @author georgepapas
 * @deprecated
 */
public interface RepositoryMethod {

  List<Incident> findAll(Repository<Incident> repository);
}
