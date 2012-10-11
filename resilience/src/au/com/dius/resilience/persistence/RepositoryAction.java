package au.com.dius.resilience.persistence;

import au.com.dius.resilience.model.Incident;

import java.util.List;

/**
 * @author georgepapas
 */
public interface RepositoryAction {

    List<Incident> perform(Repository repository);
}
