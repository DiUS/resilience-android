package au.com.dius.resilience.persistence.repository;

import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.model.Point;

import java.util.List;

public interface Repository {

  /**
   * Executed on the callers current thread
   * @return
   */
  List<Incident> findIncidents();

  /**
   * Executed on the callers current thread
   * @return
   */
  List<Incident> findIncidentsWithinDistanceKM(Point point, int distance);

  /**
   * Executed in a background thread
   * @param incidentId
   * @return
   */
  Photo findPhotoByIncident(String incidentId);
}
