package au.com.dius.resilience.model;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class Photo {

  public static Uri getOutputMediaFile() {
    if (! Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ) {
      return null;
    }

    File mediaStorageDir = new File(
        Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        "au.com.dius.resilience");

    if (!mediaStorageDir.exists()) {
      if (!mediaStorageDir.mkdirs()) {
        Log.d(Photo.class.getName(), "Failed to create directory: " + mediaStorageDir);
        return null;
      }
    }

    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
        .format(new Date());
    File mediaFile;
    mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"
        + timeStamp + ".jpg");

    return Uri.fromFile(mediaFile);
  }
}
