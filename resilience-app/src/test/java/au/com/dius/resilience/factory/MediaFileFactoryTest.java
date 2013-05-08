package au.com.dius.resilience.factory;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.MediaType;
import au.com.dius.resilience.util.ResilienceDateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.util.Date;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.reflect.Whitebox.setInternalState;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Environment.class, Uri.class})
public class MediaFileFactoryTest {

  private MediaFileFactory mediaFileFactory;

  @Mock
  private ResilienceDateUtils resilienceDateUtils;

  @Mock
  private Resources resources;

  @Before
  public void setup() {

    when(resources.getString(R.string.incident_filename_prefix)).thenReturn("FilenamePrefix");

    mediaFileFactory = new MediaFileFactory(resilienceDateUtils, resources);

    // Mocking static things is a pain :/
    mockStatic(Environment.class);
    when(Environment.getExternalStorageState()).thenReturn("mounted");
    setInternalState(Environment.class, "DIRECTORY_PICTURES", "Pictures");
    setInternalState(Environment.class, "DIRECTORY_MOVIES", "Movies");

    when(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))
      .thenReturn(new File("/tmp/ResiliencePictures"));

    when(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES))
      .thenReturn(new File("/tmp/ResilienceMovies"));

    when(resilienceDateUtils.formatFilenameFriendly(any(Date.class))).thenReturn("FriendlyFilename");
  }

  @Test
  public void shouldIndicateExternalStorageIsMounted() {
    assertThat(mediaFileFactory.isExternalStorageMounted(), is(true));
  }

  @Test
  public void shouldIndicateExternalStorageIsNotMouned() {
    given(Environment.getExternalStorageState()).willReturn("mounted_ro");
    assertThat(mediaFileFactory.isExternalStorageMounted(), is(false));
  }

  @Test
  public void shouldRetrieveStorageDirectoryPhotos() {

    File storageDirectory = mediaFileFactory.getStorageDirectory(MediaType.PHOTO);
    assertNotNull(storageDirectory);

    assertThat(storageDirectory.getPath(), is("/tmp/ResiliencePictures/Resilience"));
  }

  @Test
  public void shouldRetrieveStorageDirectoryVideo() {

    File storageDirectory = mediaFileFactory.getStorageDirectory(MediaType.VIDEO);
    assertNotNull(storageDirectory);

    assertThat(storageDirectory.getPath(), is("/tmp/ResilienceMovies/Resilience"));
  }

  @Test
  public void shouldForwardToDateUtilsForFilenameForPhoto() {
    mediaFileFactory.getFilename(MediaType.PHOTO);
    verify(resilienceDateUtils).formatFilenameFriendly(any(Date.class));
  }

  @Test
  public void shouldForwardToDateUtilsForFilenameForVideo() {
    mediaFileFactory.getFilename(MediaType.VIDEO);
    verify(resilienceDateUtils).formatFilenameFriendly(any(Date.class));
  }

  @Test
  public void shouldReturnNullWhenMediaTypeIsNull() {
    assertNull(mediaFileFactory.getFilename(null));
  }

  @Test
  public void shouldCreateMediaFile() {
    File mediaFile = mediaFileFactory.createMediaFile(MediaType.PHOTO);
    assertNotNull(mediaFile);
    assertThat(mediaFile.getPath(), is("/tmp/ResiliencePictures/Resilience/FilenamePrefixFriendlyFilename.jpg"));
  }
}
