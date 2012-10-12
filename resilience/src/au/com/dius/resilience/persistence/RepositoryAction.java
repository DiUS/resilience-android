package au.com.dius.resilience.persistence;

import java.util.List;

import au.com.dius.resilience.model.Incident;

/**
 * @author georgepapas
 */
public interface RepositoryAction {

    List<Incident> perform(Repository<Incident> repository);
}
