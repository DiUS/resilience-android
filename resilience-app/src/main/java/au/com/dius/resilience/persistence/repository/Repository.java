package au.com.dius.resilience.persistence.repository;

import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;

import java.util.List;

public interface Repository {

  /**
   * Executed on the callers current thread
   * @return
   */
  List<Incident> findIncidents();

  /**
   * Executed in a background thread
   * @param incidentId
   * @return
   */
  Photo findPhotoByIncident(String incidentId);
}
