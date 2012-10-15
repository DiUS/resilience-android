package au.com.dius.resilience.persistence;

/**
 * @author georgepapas
 */
public interface RepositoryCommand<T> {

  RepositoryCommandResult<T> perform();

}
