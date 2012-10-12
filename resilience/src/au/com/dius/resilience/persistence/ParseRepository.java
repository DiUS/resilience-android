package au.com.dius.resilience.persistence;

import java.util.List;

import au.com.dius.resilience.model.Incident;

public class ParseRepository implements Repository<Incident> {

  @Override
  public void save(Incident incident) {
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
