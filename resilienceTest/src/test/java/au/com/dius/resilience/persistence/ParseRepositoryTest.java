package au.com.dius.resilience.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import roboguice.RoboGuice;
import android.app.Application;
import android.content.Context;
import android.test.InstrumentationTestCase;
import au.com.dius.resilience.model.ImpactScale;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.IncidentRepository;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import au.com.dius.resilience.test.util.ParseTestUtils;

import com.google.inject.Injector;

public class ParseRepositoryTest extends InstrumentationTestCase {

  private IncidentRepository repository;
  
  final private CountDownLatch latch = new CountDownLatch(1);

  @Override
  public void setUp() throws Exception {
    getInstrumentation().waitForIdleSync();
    
    Context targetContext = getInstrumentation().getTargetContext();
    Injector applicationInjector = RoboGuice.getBaseApplicationInjector((Application) targetContext.getApplicationContext());
    repository = applicationInjector.getInstance(IncidentRepository.class);
    
    ParseTestUtils.setUp(targetContext);
    ParseTestUtils.dropAll(getInstrumentation());
  }
  
  public void testSave() throws Exception {
    final Long time = new Date().getTime();
    final Incident incident = new Incident("SomeName", time, "SomeNote", "Explosion", "Subby", ImpactScale.MEDIUM);
    
    final CountDownLatch saveLatch = new CountDownLatch(1);
    
    final List<String> saveIdArr = new ArrayList<String>();
    final RepositoryCommandResultListener<Incident> saveListener = new RepositoryCommandResultListener<Incident>() {
      @Override
      public void commandComplete(RepositoryCommandResult<Incident> result) {
        assertTrue(result.isSuccess());
        assertEquals(1, result.getResults().size());
        Incident savedIncident = result.getResults().get(0);
        
        String id = savedIncident.getId();
        assertNotNull(id);
        saveIdArr.add(id);
        
        saveLatch.countDown();
      }
    };
    
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
        assertEquals(saveIdArr.get(0), foundIncident.getId());
        assertEquals("SomeName", foundIncident.getName());
        assertEquals(time, foundIncident.getDateCreated());
        assertEquals("SomeNote", foundIncident.getNote());
        assertEquals("Explosion", foundIncident.getCategory());
        assertEquals("Subby", foundIncident.getSubCategory());
        assertEquals(ImpactScale.MEDIUM, foundIncident.getImpact());
        
        latch.countDown();
      }
    };
    
    getInstrumentation().runOnMainSync(new Runnable() {
      @Override
      public void run() {
        repository.findById(foundIncidentListener, saveIdArr.get(0));
      }
    });
    
    latch.await(30, TimeUnit.SECONDS);
  }
  
//  public void testUpdate() {
//      final Long time = new Date().getTime();
//      Incident incident = new Incident("OrigName", time, "OrigNote", "OrigCat", "OrigSubCat", ImpactScale.MEDIUM);
//      repository.save(stubListener, incident);
//      final String id = incident.getId();
//      
//      incident.setName("NewName");
//      incident.setNote("NewNote");
//      incident.setCategory("NewCat");
//      incident.setSubCategory("NewSubCat");
//      incident.setScale(ImpactScale.LOW);
//      repository.save(stubListener, incident);
//      
//      RepositoryCommandResultListener<Incident> foundIncidentListener = new RepositoryCommandResultListener<Incident>() {
//        @Override
//        public void commandComplete(RepositoryCommandResult<Incident> result) {
//          assertTrue(result.isSuccess());
//          Incident foundIncident = result.getResults().get(0);
//          assertNotNull(foundIncident);
//          assertEquals(id, foundIncident.getId());
//          assertEquals("NewName", foundIncident.getName());
//          assertEquals(time, foundIncident.getDateCreated());
//          assertEquals("NewNote", foundIncident.getNote());
//          assertEquals("NewCat", foundIncident.getCategory());
//          assertEquals("NewSubCat", foundIncident.getSubCategory());
//          assertEquals(ImpactScale.LOW, foundIncident.getImpact());
//        }
//      };
//      
//      repository.findById(foundIncidentListener, id);
//  }
//  
//  public void testSaveMultiple() {
//    final RepositoryCommandResultListener<Incident> findIncidentsListener = new RepositoryCommandResultListener<Incident>() {
//      @Override
//      public void commandComplete(RepositoryCommandResult<Incident> result) {
//        assertTrue(result.isSuccess());
//        assertEquals(3, result.getResults().size());
//      }
//    };
//    
//    RepositoryCommandResultListener<Incident> saveIncidentsListener = new RepositoryCommandResultListener<Incident>() {
//      @Override
//      public void commandComplete(RepositoryCommandResult<Incident> result) {
//        assertTrue(result.isSuccess());
//        repository.findAll(findIncidentsListener);
//      }
//    };
//
//    Incident incident1 = new Incident("Fooaaa", new Date().getTime(), "FooNote", "Wind", "SubC1", ImpactScale.MEDIUM);
//    Incident incident2 = new Incident("Bareee", new Date().getTime(), "BarNote", "Water", "SubC2", ImpactScale.LOW);
//    Incident incident3 = new Incident("Wharrr", new Date().getTime(), "WhaNote", "Fire", "SubC3", ImpactScale.HIGH);
  // TODO use latch instead
//    repository.saveAll(saveIncidentsListener, incident1, incident2, incident3);
//  }
}
