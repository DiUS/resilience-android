package au.com.dius.resilience.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import au.com.dius.resilience.util.Logger;
import com.squareup.otto.Bus;

import java.io.File;
import java.io.FileOutputStream;

public class ImageCompressor {

  private static final String JPG = ".jpg";
  private static final String COMPRESSED_JPG = ".compressed.jpg";

  public ImageCompressor() { }

  public File compress(String path, int quality) {

    File compressedFile;
    try {
      compressedFile = new File(path.replace(JPG, COMPRESSED_JPG));
      FileOutputStream fileOutputStream = new FileOutputStream(compressedFile);
      BitmapFactory.decodeFile(path).compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
      fileOutputStream.close();
    } catch (Exception e) {
      Logger.e("An error occurred while compressing image: ", e.getMessage());

      return null;
    }

    return compressedFile;
  }
}
