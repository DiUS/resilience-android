package au.com.dius.resilience.persistence;

/**
 * @author georgepapas
 */
public interface RepositoryCommandResultListener<T> {

  void commandComplete(RepositoryCommandResult<T> result);

}
