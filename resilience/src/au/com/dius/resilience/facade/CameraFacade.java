package au.com.dius.resilience.facade;

import static android.app.Activity.RESULT_OK;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import au.com.dius.resilience.model.Photo;

public class CameraFacade {
  
  private static final String FILENAME_DATE_FORMAT = "yyyyMMdd_HHmmss";
  private static final String FILE_PREFIX = "IMG_";
  private static final String EXTENSION = ".jpg";
  private static final String STORAGE_DIRECTORY = "ResilienceIncidents";
  
  private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
  
  private List<Photo> photos = new ArrayList<Photo>();
  
  private Activity callingActivity;
  private Uri photoFilename;
  
  public CameraFacade(Activity callingActivity) {
    this.callingActivity = callingActivity;
  }

  // TODO - make async
  public void captureImage() {
    photoFilename = getOutputMediaFile();
    
    if (photoFilename == null) { 
      return;
    }
    
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoFilename);
    callingActivity.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
  }
  
  private Uri getOutputMediaFile() {
    if (! Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ) {
      Log.d(Photo.class.getName(), "External storage was not detected!");
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

  public void processPhoto(int requestCode, int resultCode) {
    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
      photos.add(new Photo(photoFilename));
    }
  }

  public List<Photo> getPhotos() {
    return photos;
  }
}
