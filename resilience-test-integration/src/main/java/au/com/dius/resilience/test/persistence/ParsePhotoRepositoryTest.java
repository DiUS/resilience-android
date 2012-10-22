package au.com.dius.resilience.test.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.test.InstrumentationTestCase;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Impact;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.model.Photo;
import au.com.dius.resilience.persistence.repository.IncidentRepository;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResultListener;
import au.com.dius.resilience.persistence.repository.impl.ParseIncidentRepository;
import au.com.dius.resilience.persistence.repository.impl.ParsePhotoRepository;
import au.com.dius.resilience.test.util.ParseTestUtils;
import au.com.dius.resilience.test.shared.utils.MutableBoolean;
import au.com.dius.resilience.test.shared.utils.TestHelper;

public class ParsePhotoRepositoryTest extends InstrumentationTestCase {

  private IncidentRepository incidentRepository;
  private ParsePhotoRepository parsePhotoRepository;
  private Incident incident;

  public void setUp() throws Exception {
    getInstrumentation().waitForIdleSync();
    incident = new Incident("name", new Date().getTime(), "note", "cat", "subCat", Impact.MEDIUM);
    parsePhotoRepository = new ParsePhotoRepository();
    incidentRepository = new ParseIncidentRepository();

    Context targetContext = getInstrumentation().getTargetContext();
    ParseTestUtils.setUp(targetContext);
    ParseTestUtils.dropAll(getInstrumentation());
  }

  public void testSave() throws Exception {

    // Need an incident to save against
    final List<Incident> resultList1 = new ArrayList<Incident>();
    MutableBoolean mutableBoolean1 = new MutableBoolean(false);
    final CountDownLatch incidentSaveLatch = new CountDownLatch(1);
    RepositoryCommandResultListener<Incident> saveListener = TestHelper.createIncidentListener(mutableBoolean1, resultList1, incidentSaveLatch);

    incidentRepository.save(saveListener, incident);

    incidentSaveLatch.await();

    assertTrue(mutableBoolean1.isTrue());
    assertEquals(1, resultList1.size());

    // Generate test image and upload
    File photoFile = new File(Environment.getExternalStorageDirectory().getPath() + "/test_image.jpg");

    Bitmap bitmap = BitmapFactory.decodeResource(getInstrumentation().getTargetContext().getResources(), R.drawable.electricity);
    OutputStream out = new FileOutputStream(photoFile);
    bitmap.compress(CompressFormat.JPEG, 100, out);
    out.close();
    Photo photo = new Photo(photoFile);

    final List<Incident> resultList2 = new ArrayList<Incident>();
    MutableBoolean mutableBoolean2 = new MutableBoolean(false);
    final CountDownLatch photoSaveLatch = new CountDownLatch(1);
    parsePhotoRepository.save(TestHelper.createIncidentListener(mutableBoolean2, resultList2, photoSaveLatch), photo, incident);
    photoSaveLatch.await();

    assertTrue(mutableBoolean2.isTrue());
    assertEquals(1, resultList2.size());

    // Retrieve photo
    MutableBoolean mutableBoolean3 = new MutableBoolean(false);
    final List<Photo> resultList3 = new ArrayList<Photo>();
    final CountDownLatch latch3 = new CountDownLatch(1);
    parsePhotoRepository.findByIncident(TestHelper.createPhotoListener(mutableBoolean3, resultList3, latch3), incident);

    latch3.await();

    assertTrue(mutableBoolean3.isTrue());
    assertEquals(1, resultList3.size());
    assertNotNull(resultList3.get(0).getUri());
  }

  public void testRetrieveByIncidentLacksPhoto() throws Exception {
    final List<Incident> resultList1 = new ArrayList<Incident>();
    MutableBoolean mutableBoolean1 = new MutableBoolean(false);
    final CountDownLatch latch1 = new CountDownLatch(1);
    RepositoryCommandResultListener<Incident> saveListener = TestHelper.createIncidentListener(mutableBoolean1, resultList1, latch1);

    incidentRepository.save(saveListener, incident);

    latch1.await();

    assertTrue(mutableBoolean1.isTrue());
    assertEquals(1, resultList1.size());

    MutableBoolean mutableBoolean2 = new MutableBoolean(false);
    final List<Photo> resultList2 = new ArrayList<Photo>();
    final CountDownLatch latch2 = new CountDownLatch(1);
    parsePhotoRepository.findByIncident(TestHelper.createPhotoListener(mutableBoolean2, resultList2, latch2), incident);

    latch2.await();

    assertFalse(mutableBoolean2.isTrue());
    assertEquals(0, resultList2.size());
  }
}
