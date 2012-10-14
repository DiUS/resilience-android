package au.com.dius.resilience.persistence;

import au.com.dius.resilience.model.Incident;

import java.util.List;

public class ParseIncidentRepository implements Repository<Incident> {

  @Override
  public boolean save(Incident incident) {
    throw new RuntimeException("Not implemented.");
  }

  @Override
  public Incident findById(long id) {
    throw new RuntimeException("Not implemented.");
  }

  @Override
  public List<Incident> findAll() {
    throw new RuntimeException("Not implemented.");
  }
}
