package au.com.dius.resilience.persistence.repository.impl;

import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;

/**
 * @author georgepapas
 */
public interface RepositoryCommand<T> {

  RepositoryCommandResult<T> perform();

}
