package au.com.dius.resilience.factory;

import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.MediaType;
import au.com.dius.resilience.util.Logger;
import au.com.dius.resilience.util.ResilienceDateUtils;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;

import java.io.File;
import java.util.Date;

import static android.os.Environment.DIRECTORY_MOVIES;
import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;

@ContextSingleton
public class MediaFileFactory {

  public static final String JPG_EXT = ".jpg";
  public static final String MP4_EXT = ".mp4";
  private final String filenamePrefix;

  private ResilienceDateUtils resilienceDateUtils;

  public static final String MEDIA_DIR_NAME = "Resilience";

  @Inject
  public MediaFileFactory(ResilienceDateUtils resilienceDateUtils, Resources resources) {
    this.resilienceDateUtils = resilienceDateUtils;
    filenamePrefix = resources.getString(R.string.incident_filename_prefix);
  }

  public File createMediaFile(MediaType mediaType) {

    if (!isExternalStorageMounted()) {
      Logger.w(this, "No external storage is mounted.");
      return null;
    }

    String filename = getFilename(mediaType);
    File storageDirectory = getStorageDirectory(mediaType);

    if (filename == null || storageDirectory == null) {
      Logger.w(this, "Couldn't find acceptable filename or storage directory:"
        , " Filename = ", filename, ", Dir = ", storageDirectory);
      return null;
    }

    return new File(storageDirectory, filename);
  }

  String getFilename(MediaType type) {

    if (type == null) {
      return null;
    }

    Date now = new Date();
    switch (type) {
      case PHOTO:
        return filenamePrefix + resilienceDateUtils.formatFilenameFriendly(now) + JPG_EXT;
      case VIDEO:
        return filenamePrefix + resilienceDateUtils.formatFilenameFriendly(now) + MP4_EXT;
    }

    Logger.e(this, "Failed to find filename for media type ", type);

    return null;
  }

  File getStorageDirectory(MediaType mediaType) {

    String mediaDirectory = (mediaType == MediaType.PHOTO) ? DIRECTORY_PICTURES : DIRECTORY_MOVIES;

    File mediaStorageDir = new File(getExternalStoragePublicDirectory(mediaDirectory), MEDIA_DIR_NAME);

    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
      Logger.d(this, "Failed to create directory.");
      return null;
    }

    return mediaStorageDir;
  }

  boolean isExternalStorageMounted() {
    return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
  }
}
