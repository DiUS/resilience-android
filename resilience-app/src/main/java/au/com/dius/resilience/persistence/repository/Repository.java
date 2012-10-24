package au.com.dius.resilience.persistence.repository;

import au.com.dius.resilience.model.Incident;

import java.util.List;

public interface Repository {

  List<Incident> findIncidents();
}
