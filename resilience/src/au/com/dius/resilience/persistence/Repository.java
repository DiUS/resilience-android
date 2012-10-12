package au.com.dius.resilience.persistence;

import java.util.List;

import au.com.dius.resilience.model.Incident;

public interface Repository<T> {

  void save(T incident);
  Incident findById(long id);
  List<T> findAll();
}
