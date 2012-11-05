package au.com.dius.resilience.test.persistence;

import au.com.dius.resilience.model.Impact;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Point;
import au.com.dius.resilience.persistence.repository.impl.ParseIncidentAdapter;
import au.com.dius.resilience.persistence.repository.impl.ParseRepository;
import au.com.dius.resilience.test.AbstractResilienceTestCase;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import static au.com.dius.resilience.Constants.*;

public class ParseRepositoryTest extends AbstractResilienceTestCase {

  public static final Point SOURCE_POINT = new Point(-37.82097184843051, 144.95498657226562);
  public static final Point POINT_WITHIN_1KM = new Point(-37.818141132093125, 144.96485710144043); // Approx 900m away
  public static final Point POINT_WITHIN_15KM = new Point(-37.705281862026936, 144.8894762992859); // Approx 14km away

  private ParseRepository repository;
  private ParseObject incident;
  private ParseObject incident2;
  private ParseObject incident3;

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

    incident = new ParseObject(TABLE_INCIDENT);
    incident.put(COL_INCIDENT_LOCATION, sourcePoint);
    incident.put(COL_INCIDENT_IMPACT, Impact.LOW.name());

    incident2 = new ParseObject(TABLE_INCIDENT);
    incident2.put(COL_INCIDENT_LOCATION, within1KM);
    incident2.put(COL_INCIDENT_IMPACT, Impact.MEDIUM.name());

    incident3 = new ParseObject(TABLE_INCIDENT);
    incident3.put(COL_INCIDENT_LOCATION, within15KM);
    incident3.put(COL_INCIDENT_IMPACT, Impact.HIGH.name());

    try {
      ParseObject.saveAll(
        new ArrayList<ParseObject>() {{
          add(incident);
          add(incident2);
          add(incident3);
        }});
    } catch (Exception e) {
      throw new RuntimeException("Failed to save incidents! ", e);
    }
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
}
