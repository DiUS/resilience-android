package au.com.dius.resilience.test.persistence;

import au.com.dius.resilience.model.Feedback;
import au.com.dius.resilience.model.Impact;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Point;
import au.com.dius.resilience.persistence.Columns;
import au.com.dius.resilience.persistence.repository.impl.ParseIncidentAdapter;
import au.com.dius.resilience.persistence.repository.impl.ParseRepository;
import au.com.dius.resilience.test.AbstractResilienceTestCase;
import au.com.dius.resilience.test.util.ParseTestUtils;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.List;

import static au.com.dius.resilience.Constants.*;

public class ParseRepositoryTest extends AbstractResilienceTestCase {

  public static final Point SOURCE_POINT = new Point(-37.82097184843051, 144.95498657226562);
  public static final Point POINT_WITHIN_1KM = new Point(-37.818141132093125, 144.96485710144043); // Approx 900m away
  public static final Point POINT_WITHIN_15KM = new Point(-37.705281862026936, 144.8894762992859); // Approx 14km away

  public static final String INCIDENT_1 = "Incident1";
  public static final String INCIDENT_2 = "Incident2";
  public static final String INCIDENT_3 = "Incident3";

  private ParseRepository repository;

  @Override
  public void setUp() {
    super.setUp();

    repository = new ParseRepository(new ParseIncidentAdapter());

    ParseGeoPoint sourcePoint = new ParseGeoPoint(SOURCE_POINT.getLatitude(), SOURCE_POINT.getLongitude());
    ParseGeoPoint within1KM = new ParseGeoPoint(POINT_WITHIN_1KM.getLatitude(), POINT_WITHIN_1KM.getLongitude());
    ParseGeoPoint within15KM = new ParseGeoPoint(POINT_WITHIN_15KM.getLatitude(), POINT_WITHIN_15KM.getLongitude());

    double distance1 = sourcePoint.distanceInKilometersTo(within1KM);
    assertTrue(distance1 > 0.9 && distance1 < 1.0);
    double distance2 = sourcePoint.distanceInKilometersTo(within15KM);
    assertTrue(distance2 > 14.0 && distance2 < 15.0);

    final ParseObject incident = new ParseObject(Columns.Incident.TABLE_NAME);
    incident.put(Columns.Incident.NAME, INCIDENT_1);
    incident.put(Columns.Incident.LOCATION, sourcePoint);
    incident.put(Columns.Incident.IMPACT, Impact.LOW.name());

    final ParseObject incident2 = new ParseObject(Columns.Incident.TABLE_NAME);
    incident2.put(Columns.Incident.NAME, INCIDENT_2);
    incident2.put(Columns.Incident.LOCATION, within1KM);
    incident2.put(Columns.Incident.IMPACT, Impact.MEDIUM.name());

    final ParseObject incident3 = new ParseObject(Columns.Incident.TABLE_NAME);
    incident3.put(Columns.Incident.NAME, INCIDENT_3);
    incident3.put(Columns.Incident.LOCATION, within15KM);
    incident3.put(Columns.Incident.IMPACT, Impact.HIGH.name());

    ParseTestUtils.saveAll(incident, incident2, incident3);
  }

  public void testFindAllIncidents() {
    List<Incident> incidents = repository.findIncidents();
    assertEquals(3, incidents.size());
  }

  public void testFindWithinDistanceOfPoint() throws ParseException {

    List<Incident> incidentsWithin1KM = repository.findIncidentsWithinDistanceKM(SOURCE_POINT, 1);

    assertEquals(2, incidentsWithin1KM.size());

    assertEquals(SOURCE_POINT.getLatitude(), incidentsWithin1KM.get(0).getPoint().getLatitude());
    assertEquals(SOURCE_POINT.getLongitude(), incidentsWithin1KM.get(0).getPoint().getLongitude());

    assertEquals(POINT_WITHIN_1KM.getLatitude(), incidentsWithin1KM.get(1).getPoint().getLatitude());
    assertEquals(POINT_WITHIN_1KM.getLongitude(), incidentsWithin1KM.get(1).getPoint().getLongitude());

    // Point outside of 1km radius is excluded.
  }

  public void testFindWithinBoundingBox() {

    // Contains the Melbourne CBD
    Point bbSouthWest = new Point(-37.82497195707115, 144.94528770446777);
    Point bbNorthEast = new Point(-37.80435890857138, 144.97687339782715);

    List<Incident> incidentsWithinBoundingBox = repository.findIncidentsWithinBoundingBox(bbSouthWest, bbNorthEast);

    assertEquals(2, incidentsWithinBoundingBox.size());

    for (Incident i : incidentsWithinBoundingBox) {
      if (INCIDENT_1.equals(i.getName())) {
        assertEquals(SOURCE_POINT.getLatitude(), i.getPoint().getLatitude());
        assertEquals(SOURCE_POINT.getLongitude(), i.getPoint().getLongitude());
      } else if (INCIDENT_2.equals(i.getName())) {
        assertEquals(POINT_WITHIN_1KM.getLatitude(), i.getPoint().getLatitude());
        assertEquals(POINT_WITHIN_1KM.getLongitude(), i.getPoint().getLongitude());
      } else {
        fail("Unexpected incident " + i.getName() + " returned.");
      }
    }
  }

  public void testSendFeedback() {
    // TODO
//    repository.sendFeedback(new Feedback("test feedback", "dev-id123"));
  }
}
