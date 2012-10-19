package au.com.dius.resilience.test.unit.persistence.repository.impl;

import android.content.Context;
import au.com.dius.resilience.model.Impact;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import au.com.dius.resilience.persistence.repository.impl.SqlLiteIncidentRepository;
import au.com.dius.resilience.test.shared.utils.MutableBoolean;
import au.com.dius.resilience.test.shared.utils.TestHelper;
import au.com.dius.resilience.test.unit.utils.SameDatabaseCustomTestRunner;
import com.google.inject.Provider;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.util.DatabaseConfig;
import com.xtremelabs.robolectric.util.SQLiteMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

@DatabaseConfig.UsingDatabaseMap(SQLiteMap.class)
@RunWith(SameDatabaseCustomTestRunner.class)
public class SqlLiteIncidentRepositoryTest {

  @Mock
  private Context mockContext;
  private Provider<Context> contextProvider;

  private SqlLiteIncidentRepository repository;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    contextProvider = TestHelper.createContextProvider(mockContext);
    repository = new SqlLiteIncidentRepository(contextProvider);
  }

  @Test
  public void testShouldSaveAndLoad() {

    final MutableBoolean saved = new MutableBoolean(false);
    RepositoryCommandResultListener<Incident> saveListener = TestHelper.createIncidentListener(
            saved,
            new ArrayList<Incident>());

    repository.save(
            saveListener,
            new Incident("Test1", Long.valueOf(999), "Hello World", "Cat1", "Subcat1", Impact.LOW));

    Robolectric.runBackgroundTasks();

    assertTrue("save of data failed", saved.isTrue());

//    repository.save(new Incident("Test2", Long.valueOf(888), "Goodbye World", "Cat2", "Subcat2", ImpactScale.MEDIUM));
//    repository.save(new Incident("Test3", Long.valueOf(777), "Sup, World?", "Cat3", "Subcat3", ImpactScale.HIGH));

    final MutableBoolean found = new MutableBoolean(false);
    List<Incident> results = new ArrayList<Incident>();
    RepositoryCommandResultListener<Incident> findListener = TestHelper.createIncidentListener(found, results);
    repository.findAll(findListener);

    Robolectric.runBackgroundTasks();
    assertTrue("expected to find results", found.isTrue());

    assertEquals(1, results.size());

    assertNotNull(results.get(0).getId());
    assertTrue("Test1".equals(results.get(0).getName()));
    assertTrue(999 == results.get(0).getDateCreated());
    assertTrue("Hello World".equals(results.get(0).getNote()));
  }

}
