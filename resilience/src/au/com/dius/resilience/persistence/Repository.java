package au.com.dius.resilience.persistence;

import java.util.List;

@Deprecated
public interface Repository<T> {

  boolean save(T object);
  T findById(Object id);
  List<T> findAll();
}
