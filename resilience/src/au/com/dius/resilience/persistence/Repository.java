package au.com.dius.resilience.persistence;

import java.util.List;

import au.com.dius.resilience.model.Incident;

public interface Repository {

  void save(Incident incident);
  Incident findById(long id);
  List<Incident> findAll();
}
