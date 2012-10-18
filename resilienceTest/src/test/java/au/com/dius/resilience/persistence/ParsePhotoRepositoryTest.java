package au.com.dius.resilience.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
import au.com.dius.resilience.utils.MutableBoolean;
import au.com.dius.resilience.utils.TestHelper;

public class ParsePhotoRepositoryTest extends InstrumentationTestCase {

  private IncidentRepository incidentRepository;
  private ParsePhotoRepository parsePhotoRepository;
  private Incident incident;
  
  public void setUp() {
    incident = new Incident("name", new Date().getTime(), "note", "cat", "subCat", Impact.MEDIUM);
    parsePhotoRepository = new ParsePhotoRepository(incident);
    incidentRepository = new ParseIncidentRepository();
    getInstrumentation().waitForIdleSync();
  }
  
  public void testSave() throws Exception {

    final List<Incident> resultList1 = new ArrayList<Incident>();
    MutableBoolean mutableBoolean1 = new MutableBoolean(false);
    final CountDownLatch incidentSaveLatch = new CountDownLatch(1);
    RepositoryCommandResultListener<Incident> saveListener = TestHelper.createIncidentListener(mutableBoolean1, resultList1, incidentSaveLatch);
    
    incidentRepository.save(saveListener, incident);
    
    incidentSaveLatch.await();
    
    assertTrue(mutableBoolean1.isTrue());
    assertEquals(1, resultList1.size());
    
    File photoFile = new File(Environment.getExternalStorageDirectory().getPath() + "/test_image.jpg");
    
    Bitmap bitmap = BitmapFactory.decodeResource(getInstrumentation().getTargetContext().getResources(), R.drawable.electricity);
    OutputStream out = new FileOutputStream(photoFile);
    bitmap.compress(CompressFormat.JPEG, 100, out);
    out.close();
    Photo photo = new Photo(photoFile);
    
    final List<Incident> resultList2 = new ArrayList<Incident>();
    MutableBoolean mutableBoolean2 = new MutableBoolean(false);
    final CountDownLatch photoSaveLatch = new CountDownLatch(1);
    parsePhotoRepository.save(TestHelper.createIncidentListener(mutableBoolean2, resultList2, photoSaveLatch), photo);
    photoSaveLatch.await();
    
    assertTrue(mutableBoolean2.isTrue());
    assertEquals(1, resultList2.size());
  }
}
