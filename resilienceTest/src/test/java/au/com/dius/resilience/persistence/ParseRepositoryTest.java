package au.com.dius.resilience.persistence;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import roboguice.RoboGuice;
import android.app.Application;
import android.content.Context;
import android.test.InstrumentationTestCase;
import au.com.dius.resilience.model.Impact;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.IncidentRepository;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import au.com.dius.resilience.test.util.ParseTestUtils;

import com.google.inject.Injector;

public class ParseRepositoryTest extends InstrumentationTestCase {

  private IncidentRepository repository;
  
  final private CountDownLatch latch = new CountDownLatch(1);

  private RepositoryCommandResultListener<Incident> saveListener;
  private CountDownLatch saveLatch;

  // A place to store variables across threads.
  final Map<String, String> testValues = new HashMap<String, String>();
  
  @Override
  public void setUp() throws Exception {
    getInstrumentation().waitForIdleSync();
    
    Context targetContext = getInstrumentation().getTargetContext();
    Injector applicationInjector = RoboGuice.getBaseApplicationInjector((Application) targetContext.getApplicationContext());
    repository = applicationInjector.getInstance(IncidentRepository.class);
    
    ParseTestUtils.setUp(targetContext);
    ParseTestUtils.dropAll(getInstrumentation());
    
    testValues.clear();
    saveLatch = new CountDownLatch(1);
    saveListener = new RepositoryCommandResultListener<Incident>() {
      @Override
      public void commandComplete(RepositoryCommandResult<Incident> result) {
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResults().size());
        Incident savedIncident = result.getResults().get(0);
        
        testValues.put("INCIDENT_ID",  savedIncident.getId());
        
        saveLatch.countDown();
      }
    };
  }
  
  public void testSave() throws Exception {
    final Long time = new Date().getTime();
    final Incident incident = new Incident("SomeName", time, "SomeNote", "Explosion", "Subby", Impact.MEDIUM);
    
    getInstrumentation().runOnMainSync(new Runnable() {
      @Override
      public void run() {
        repository.save(saveListener, incident);
      }
    });
    
    saveLatch.await(30, TimeUnit.SECONDS);
    
    final RepositoryCommandResultListener<Incident> foundIncidentListener = new RepositoryCommandResultListener<Incident>() {
      @Override
      public void commandComplete(RepositoryCommandResult<Incident> result) {
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResults().size());
        
        Incident foundIncident = result.getResults().get(0);
        assertNotNull(foundIncident);
        assertEquals(testValues.get("INCIDENT_ID"), foundIncident.getId());
        assertEquals("SomeName", foundIncident.getName());
        assertEquals(time, foundIncident.getDateCreated());
        assertEquals("SomeNote", foundIncident.getNote());
        assertEquals("Explosion", foundIncident.getCategory());
        assertEquals("Subby", foundIncident.getSubCategory());
        assertEquals(Impact.MEDIUM, foundIncident.getImpact());
        
        latch.countDown();
      }
    };
    
    getInstrumentation().runOnMainSync(new Runnable() {
      @Override
      public void run() {
        repository.findById(foundIncidentListener, testValues.get("INCIDENT_ID"));
      }
    });
    
    latch.await(30, TimeUnit.SECONDS);
  }
  
  public void testUpdate() throws Exception {
      final Long time = new Date().getTime();
      Incident incident = new Incident("OrigName", time, "OrigNote", "OrigCat", "OrigSubCat", Impact.MEDIUM);
      repository.save(saveListener, incident);
      final String id = incident.getId();
      
      saveLatch.await();
      saveLatch = new CountDownLatch(1);
      
      incident.setName("NewName");
      incident.setNote("NewNote");
      incident.setCategory("NewCat");
      incident.setSubCategory("NewSubCat");
      incident.setScale(Impact.LOW);
      repository.save(saveListener, incident);
      
      saveLatch.await();
      final CountDownLatch findLatch = new CountDownLatch(1);
      
      RepositoryCommandResultListener<Incident> foundIncidentListener = new RepositoryCommandResultListener<Incident>() {
        @Override
        public void commandComplete(RepositoryCommandResult<Incident> result) {
          assertTrue(result.isSuccess());
          Incident foundIncident = result.getResults().get(0);
          assertNotNull(foundIncident);
          assertEquals(id, foundIncident.getId());
          assertEquals("NewName", foundIncident.getName());
          assertEquals(time, foundIncident.getDateCreated());
          assertEquals("NewNote", foundIncident.getNote());
          assertEquals("NewCat", foundIncident.getCategory());
          assertEquals("NewSubCat", foundIncident.getSubCategory());
          assertEquals(Impact.LOW, foundIncident.getImpact());
          
          findLatch.countDown();
        }
      };
      
      repository.findById(foundIncidentListener, id);
      findLatch.await();
  }
  
  public void testFindAll() throws Exception {
    saveLatch = new CountDownLatch(3);
    Incident incident1 = new Incident("Fooaaa", new Date().getTime(), "FooNote", "Wind", "SubC1", Impact.MEDIUM);
    Incident incident2 = new Incident("Bareee", new Date().getTime(), "BarNote", "Water", "SubC2", Impact.LOW);
    Incident incident3 = new Incident("Wharrr", new Date().getTime(), "WhaNote", "Fire", "SubC3", Impact.HIGH);
    repository.save(saveListener, incident1);
    repository.save(saveListener, incident2);
    repository.save(saveListener, incident3);
    
    saveLatch.await();
    
    final CountDownLatch findLatch = new CountDownLatch(1);
    final RepositoryCommandResultListener<Incident> findIncidentsListener = new RepositoryCommandResultListener<Incident>() {
      @Override
      public void commandComplete(RepositoryCommandResult<Incident> result) {
        assertTrue(result.isSuccess());
        assertEquals(3, result.getResults().size());
        
        findLatch.countDown();
      }
    };
    
    repository.findAll(findIncidentsListener);
    
    findLatch.await();
  }
}
