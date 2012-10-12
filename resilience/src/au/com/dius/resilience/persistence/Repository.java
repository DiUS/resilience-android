package au.com.dius.resilience.persistence;

import java.util.List;

public interface Repository<T> {

  void save(T object);
  T findById(long id);
  List<T> findAll();
}
