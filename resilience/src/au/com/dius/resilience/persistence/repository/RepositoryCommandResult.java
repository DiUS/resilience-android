package au.com.dius.resilience.persistence.repository;

import java.util.ArrayList;
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
  
  public RepositoryCommandResult(boolean success, T result) {
    this.success = success;
    this.results = new ArrayList<T>();
    results.add(result);
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
