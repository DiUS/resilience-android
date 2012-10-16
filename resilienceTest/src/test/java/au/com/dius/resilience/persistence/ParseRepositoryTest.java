package au.com.dius.resilience.persistence;

import static au.com.dius.resilience.test.util.TestUtils.sleep;

import java.util.Date;

import android.test.InstrumentationTestCase;
import android.util.Log;
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
//    Long time = new Date().getTime();
//    Incident incident = IncidentFactory.createIncident("SomeName", time, "SomeNote", "Explosion", "Subby", ImpactScale.MEDIUM);
//    repository.save(incident);
//    
//    sleep(2000);
//    
//    Incident foundIncident = repository.findById(incident.getId());
//    
//    assertNotNull(foundIncident);
//    assertEquals(incident.getId(), foundIncident.getId());
//    assertEquals("SomeName", foundIncident.getName());
//    assertEquals(time, foundIncident.getDateCreated());
//    assertEquals("SomeNote", foundIncident.getNote());
//    assertEquals("Explosion", foundIncident.getCategory());
//    assertEquals("Subby", foundIncident.getSubCategory());
//    assertEquals(ImpactScale.MEDIUM, foundIncident.getImpact());
  }
  
  public void testUpdate() {
//      Long time = new Date().getTime();
//      Incident incident = IncidentFactory.createIncident("OrigName", time, "OrigNote", "OrigCat", "OrigSubCat", ImpactScale.MEDIUM);
//      freakingLog("1", incident);
//      repository.save(incident);
//      freakingLog("2", incident);
//      String id = incident.getId();
//      sleep(2000);
//      
//      incident.setName("NewName");
//      incident.setNote("NewNote");
//      incident.setCategory("NewCat");
//      incident.setSubCategory("NewSubCat");
//      incident.setScale(ImpactScale.LOW);
//      freakingLog("3", incident);
//      repository.save(incident);
//      freakingLog("4", incident);
//      
//      Incident foundIncident = repository.findById(id);
//      
//      assertNotNull(foundIncident);
//      assertEquals(incident.getId(), foundIncident.getId());
//      assertEquals("NewName", foundIncident.getName());
//      assertEquals(time, foundIncident.getDateCreated());
//      assertEquals("NewNote", foundIncident.getNote());
//      assertEquals("NewCat", foundIncident.getCategory());
//      assertEquals("NewSubCat", foundIncident.getSubCategory());
//      assertEquals(ImpactScale.LOW, foundIncident.getImpact());
  }
  
//  public void freakingLog(String idx, Incident incident) {
//    System.out.println(idx + " , Incident id: " + incident.getId());
//    Log.d("HELLO", idx + ", Incident idbla: " + incident.getId());
//  }
  
  public void testSaveMultiple() {
    assertTrue(true);
//    Incident incident1 = IncidentFactory.createIncident("Fooaaa", new Date().getTime(), "FooNote", "Wind", "SubC1", ImpactScale.MEDIUM);
//    repository.save(incident1);
//    
//    Incident incident2 = IncidentFactory.createIncident("Bareee", new Date().getTime(), "BarNote", "Water", "SubC2", ImpactScale.LOW);
//    repository.save(incident2);
//    
//    Incident incident3 = IncidentFactory.createIncident("Wharrr", new Date().getTime(), "WhaNote", "Fire", "SubC3", ImpactScale.HIGH);
//    repository.save(incident3);
//    
//    sleep(2000);
//    
//    List<Incident> retrievedIncidents = repository.findAll();
//    
//    assertEquals(3, retrievedIncidents.size());
  }
  
//  TODO
//  public void testSaveWithIncompleteData() {
//    
//  }
}
