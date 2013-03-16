package au.com.dius.resilience.persistence.repository;

/**
 * @author georgepapas
 */
public interface RepositoryCommandResultListener<T> {

  /**
   * Call back from the background data operation.  Always happens on the UI thread.
   * @param result the result object from the background data operation
   */
  void commandComplete(RepositoryCommandResult<T> result);

}
