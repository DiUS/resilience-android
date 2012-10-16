package au.com.dius.resilience.persistence.repository;

import java.util.List;

/**
 * @author georgepapas
 */
public class RepositoryCommandResult<T> {

  private boolean success = false;
  private List<T> results;

  public RepositoryCommandResult(boolean success, List<T> results) {
    this.success = success;
    this.results = results;
  }

  public boolean isSuccess() {
    return success;
  }

  public boolean hasResults() {
    return results != null && results.size() > 0;
  }

  public List<T> getResults() {
    return results;
  }
}
