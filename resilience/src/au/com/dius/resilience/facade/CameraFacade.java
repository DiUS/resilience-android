package au.com.dius.resilience.facade;

import static android.app.Activity.RESULT_OK;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import au.com.dius.resilience.model.Photo;

public class CameraFacade {
  
  private static final String FILENAME_DATE_FORMAT = "yyyyMMdd_HHmmss";
  private static final String FILE_PREFIX = "IMG_";
  private static final String EXTENSION = ".jpg";
  private static final String STORAGE_DIRECTORY = "ResilienceIncidents";
  
  public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
  private static final int PHOTO_QUALITY = 100;
  
  private List<Photo> photos = new ArrayList<Photo>();
  
  private Activity callingActivity;
  private File photoFilename;
  
  public CameraFacade(Activity callingActivity) {
    this.callingActivity = callingActivity;
  }

  /**
   * Starts a camera Activity to capture a single photo.
   * To be called before {@link #processPhoto(int, int)}
   */
  public void captureImage() {
    photoFilename = getOutputMediaFile();
    
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFilename));
    callingActivity.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
  }
  
  private File getOutputMediaFile() {
    if (! Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ) {
      throw new RuntimeException("External storage was not detected!");
    }

    File mediaStorageDir = new File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        STORAGE_DIRECTORY);

    if (!mediaStorageDir.exists()) {
      if (!mediaStorageDir.mkdirs()) {
        throw new RuntimeException("Failed to create directory: " + mediaStorageDir);
      }
    }

    String timeStamp = new SimpleDateFormat(FILENAME_DATE_FORMAT)
        .format(new Date());
    File mediaFile;
    mediaFile = new File(mediaStorageDir.getPath() + File.separator + FILE_PREFIX
        + timeStamp + EXTENSION);

    return mediaFile;
  }

  /**
   * @param requestCode Request code 
   * @param resultCode
   * To be called after {@link #captureImage()}.
   * i.e. within a {@link Activity#onActivityResult(int requestCode, int resultCode, Intent data)} method.
   */
  public void processPhoto(int requestCode, int resultCode) {
    if (photoFilename == null) {
      return;
    }
    
    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
      photos.add(new Photo(photoFilename));
      photoFilename = null;
    }
  }

  public List<Photo> getPhotos() {
    return photos;
  }
  
  public static Bitmap decodeBytes(byte[] bytes) {
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
  }
  
  public static byte[] extractBytes(Photo photo) {
    File photoFile = photo.getPath();
    Bitmap photoBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
    ByteArrayOutputStream stream = new ByteArrayOutputStream();
    photoBitmap.compress(Bitmap.CompressFormat.PNG, PHOTO_QUALITY, stream);
    return stream.toByteArray();
  }
}
