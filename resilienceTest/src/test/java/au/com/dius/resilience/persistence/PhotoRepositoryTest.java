package au.com.dius.resilience.persistence;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import au.com.dius.resilience.model.Photo;

import com.xtremelabs.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class PhotoRepositoryTest {

  Repository<Photo> repository;
  
  @Before
  public void setUp() {
    repository = RepositoryFactory.createPhotoRepository();
  }
  
  @Test
  public void testCapturePhoto() {
    // Prepare to take photo
    
    // Take photo
    
    // Save photo
  }
}
