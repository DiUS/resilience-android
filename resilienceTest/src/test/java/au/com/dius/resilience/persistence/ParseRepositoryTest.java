package au.com.dius.resilience.persistence;

import static au.com.dius.resilience.test.util.TestUtils.sleep;

import java.util.Date;
import java.util.List;

import android.test.InstrumentationTestCase;
import au.com.dius.resilience.model.ImpactScale;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.IncidentFactory;
import au.com.dius.resilience.test.util.ParseTestUtils;

public class ParseRepositoryTest extends InstrumentationTestCase {

  private ParseIncidentRepository repository;

  public void setUp() throws Exception {
    super.setUp();
    ParseTestUtils.setUp(getInstrumentation().getTargetContext());
    repository = new ParseIncidentRepository();
  }
  
  public void testSave() {
    Incident incident1 = IncidentFactory.createIncident("Foo", new Date().getTime(), "FooNote", "Wind", "SubC1", ImpactScale.MEDIUM);
    repository.save(incident1);
    
    Incident incident2 = IncidentFactory.createIncident("Bar", new Date().getTime(), "BarNote", "Water", "SubC2", ImpactScale.LOW);
    repository.save(incident2);
    
    Incident incident3 = IncidentFactory.createIncident("Wha", new Date().getTime(), "WhaNote", "Fire", "SubC3", ImpactScale.HIGH);
    repository.save(incident3);
    
    sleep(2000);
    
    List<Incident> retrievedIncidents = repository.findAll();
    
    assertEquals(3, retrievedIncidents.size());
  }
}
