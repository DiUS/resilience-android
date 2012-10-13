package au.com.dius.resilience.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class Photo {

  private static final String FILENAME_DATE_FORMAT = "yyyyMMdd_HHmmss";
  private static final String FILE_PREFIX = "IMG_";
  private static final String EXTENSION = ".jpg";
  private static final String STORAGE_DIRECTORY = "Incidents";
  
  public static Uri getOutputMediaFile() {
    if (! Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ) {
      return null;
    }

    File mediaStorageDir = new File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        STORAGE_DIRECTORY);

    if (!mediaStorageDir.exists()) {
      if (!mediaStorageDir.mkdirs()) {
        Log.d(Photo.class.getName(), "Failed to create directory: " + mediaStorageDir);
        return null;
      }
    }

    String timeStamp = new SimpleDateFormat(FILENAME_DATE_FORMAT)
        .format(new Date());
    File mediaFile;
    mediaFile = new File(mediaStorageDir.getPath() + File.separator + FILE_PREFIX
        + timeStamp + EXTENSION);

    return Uri.fromFile(mediaFile);
  }
}
