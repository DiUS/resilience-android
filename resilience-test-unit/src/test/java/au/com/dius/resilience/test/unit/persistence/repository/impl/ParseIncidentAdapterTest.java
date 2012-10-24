package au.com.dius.resilience.test.unit.persistence.repository.impl;

import au.com.dius.resilience.Constants;
import au.com.dius.resilience.model.Impact;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Point;
import au.com.dius.resilience.persistence.repository.impl.ParseIncidentAdapter;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import roboguice.inject.ContextSingleton;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ParseIncidentAdapterTest {

  private static final String EXPECTED_NAME = "name";
  private static final String EXPECTED_NOTE = "note";
  private static final String EXPECTED_CATEGORY = "category";
  private static final String EXPECTED_SUB_CATEGORY = "subcategory";
  private static final Impact EXPECTED_IMPACT = Impact.LOW;

  private ParseIncidentAdapter adapter;
  private ParseObject parse;

  @Before
  public void setup() {
    adapter = new ParseIncidentAdapter();
    parse = createStubParseObject();
  }

  @Test
  public void shouldBeASingleton() {
    assertNotNull(adapter.getClass().getAnnotation(ContextSingleton.class));
  }

  @Test
  public void shouldAdaptFromDomain() {
    Incident incident = createStubIncident();

    parse = adapter.serialise(parse, incident);

    assertThat(parse.getString(Constants.COL_INCIDENT_NAME), equalTo(EXPECTED_NAME));
    assertThat(parse.getString(Constants.COL_INCIDENT_NOTE), equalTo(EXPECTED_NOTE));
    assertThat(parse.getString(Constants.COL_INCIDENT_CATEGORY), equalTo(EXPECTED_CATEGORY));
    assertThat(parse.getString(Constants.COL_INCIDENT_SUBCATEGORY), equalTo(EXPECTED_SUB_CATEGORY));
    assertThat(parse.getString(Constants.COL_INCIDENT_IMPACT), equalTo(EXPECTED_IMPACT.name()));
  }

  @Test
  public void shouldSerializeLocation() {

    double latitude = -30.00;
    double longitude = 20.00;

    Incident incident = createStubIncident();
    incident.setPoint(new Point(latitude, longitude));

    parse = adapter.serialise(parse, incident);

    final ParseGeoPoint geoPoint = (ParseGeoPoint) parse.get("location");
    assertThat(geoPoint, notNullValue());
    assertThat(geoPoint, notNullValue());
    assertThat(geoPoint.getLatitude(), equalTo(latitude));
    assertThat(geoPoint.getLongitude(), equalTo(longitude));
  }

  @Test
  public void shouldHandleNullLocationWhenSerialising() {
    parse = adapter.serialise(parse, createStubIncident());
    assertThat(parse.get(Constants.COL_INCIDENT_LOCATION), nullValue());
  }

  @Test
  public void shouldHandleNullLocationWhenDeSerialising() {
    final Incident incident = adapter.deserialise(parse);
    assertThat(incident.getPoint(), nullValue());
  }

  @Test
  public void shouldDeserialiseLocation() {
    final double expectedLatitude = -10.00;
    final double expectedLongitude = 10.00;
    ParseGeoPoint geoPoint = new ParseGeoPoint(expectedLatitude, expectedLongitude);
    parse.put(Constants.COL_INCIDENT_LOCATION, geoPoint);

    final Incident incident = adapter.deserialise(parse);
    final Point point = incident.getPoint();
    assertThat(point, notNullValue());
    assertThat(point.getLatitude(), equalTo(expectedLatitude));
    assertThat(point.getLongitude(), equalTo(expectedLongitude));
  }

  @Test
  public void shouldAdaptFromParseObject() {
    parse = createStubParseObject();

    Incident incident = adapter.deserialise(parse);
    assertDeserialisedStub(incident);
  }

  @Test
  public void shouldAdaptListOfPersistables() {
    List<ParseObject> persistables = new ArrayList<ParseObject>();
    persistables.add(createStubParseObject());
    persistables.add(createStubParseObject());
    persistables.add(createStubParseObject());

    List<Incident> incidents = adapter.deserialise(persistables);
    assertThat(incidents.size(), is(persistables.size()));
    for(Incident incident : incidents) {
      assertDeserialisedStub(incident);
    }
  }

  private ParseObject createStubParseObject() {
    ParseObject stub = new ParseObject("incident");

    stub.put(Constants.COL_INCIDENT_NAME, EXPECTED_NAME);
    stub.put(Constants.COL_INCIDENT_NOTE, EXPECTED_NOTE);
    stub.put(Constants.COL_INCIDENT_CATEGORY, EXPECTED_CATEGORY);
    stub.put(Constants.COL_INCIDENT_SUBCATEGORY, EXPECTED_SUB_CATEGORY);
    stub.put(Constants.COL_INCIDENT_IMPACT, EXPECTED_IMPACT.name());

    return stub;
  }


  private void assertDeserialisedStub(Incident incident) {
    assertThat(incident.getName(), equalTo(EXPECTED_NAME));
    assertThat(incident.getCategory(), equalTo(EXPECTED_CATEGORY));
    assertThat(incident.getSubCategory(), equalTo(EXPECTED_SUB_CATEGORY));
    assertThat(incident.getImpact(), equalTo(EXPECTED_IMPACT));
    assertThat(incident.getNote(), equalTo(EXPECTED_NOTE));
  }

  private Incident createStubIncident() {
    return new Incident(
              "id",
              EXPECTED_NAME,
              100L,
              EXPECTED_NOTE,
              EXPECTED_CATEGORY,
              EXPECTED_SUB_CATEGORY,
              EXPECTED_IMPACT);
  }
}
