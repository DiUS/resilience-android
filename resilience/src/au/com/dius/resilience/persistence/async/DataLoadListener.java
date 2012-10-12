package au.com.dius.resilience.persistence.async;

import java.util.List;

/**
 * @author georgepapas
 */
public interface DataLoadListener<T> {
  void itemsLoaded(List<T> items);
  void itemSaved();
}
