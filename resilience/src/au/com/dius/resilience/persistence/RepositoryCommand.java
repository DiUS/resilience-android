package au.com.dius.resilience.persistence;

import java.util.List;

import au.com.dius.resilience.model.Incident;

/**
 * @author georgepapas
 */
public interface RepositoryCommand<T> {

  RepositoryCommandResult<T> perform();

}
