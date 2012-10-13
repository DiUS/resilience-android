package au.com.dius.resilience.persistence;

import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author georgepapas
 */
@Singleton
public class RepositoryCommands {

  public final <T> RepositoryCommand<T> findAll(final Repository<T> repository) {
    return new RepositoryCommand<T>() {
      @Override
      public RepositoryCommandResult<T> perform() {
        return new RepositoryCommandResult<T>(true, repository.findAll());
      }
    };
  }

  public final <T> RepositoryCommand<T> save(final Repository<T> repository, final T item) {
    return new RepositoryCommand<T>() {
      @Override
      public RepositoryCommandResult<T> perform() {
        return new RepositoryCommandResult<T>(repository.save(item), Collections.EMPTY_LIST);
      }
    };
  }

  public final <T> RepositoryCommand<T> findById(final Repository<T> repository, final long identifier) {
    return new RepositoryCommand<T>() {
      @Override
      public RepositoryCommandResult<T> perform() {
        final T item = repository.findById(identifier);

        List<T> result;
        if (item != null) {
          result = new ArrayList<T>(1);
          result.add(item);
        } else {
          result = Collections.EMPTY_LIST;
        }

        return new RepositoryCommandResult<T>(item != null, result);
      }
    };
  }
}
