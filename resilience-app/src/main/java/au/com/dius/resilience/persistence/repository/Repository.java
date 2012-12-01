package au.com.dius.resilience.persistence.repository;

import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.model.Point;

import java.util.List;

public interface Repository {

  /**
   * Executed on the callers current thread.
   * Finds all incidents without any filtering.
   *
   * @return
   */
  List<Incident> findIncidents();

  /**
   * Executed on the callers current thread.
   * Finds all incidents within a given distance around a point.
   * Ordered from closest to farthest (see
   * @{link https://parse.com/questions/geopoint-query-order})
   *
   * @param point    The center point to query from.
   * @param distance Distance in KM around the point.
   * @return
   */
  List<Incident> findIncidentsWithinDistanceKM(Point point, int distance);

  /**
   * Executed on the callers current thread.
   * Finds all incidents within a given bounding box.
   *
   * @param southWest The bottom left of the bounding box.
   * @param northEast The top right of the bounding box.
   * @return
   */
  List<Incident> findIncidentsWithinBoundingBox(Point southWest, Point northEast);

  /**
   * Executed in a background thread
   *
   * @param incidentId Find photos for the given incidentId.
   * @return
   */
  Photo findPhotoByIncident(String incidentId);

  boolean createIncident(Incident incident);
}
