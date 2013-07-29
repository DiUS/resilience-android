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

      if (compressedFile.exists()) {
        return compressedFile;
      }

      FileOutputStream fileOutputStream = new FileOutputStream(compressedFile);

      BitmapFactory.Options options = optimiseDecoderOptions(path);

      BitmapFactory.decodeFile(path, options).compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
      fileOutputStream.close();
    } catch (Exception e) {
      Logger.e("An error occurred while compressing image: ", e.getMessage());

      return null;
    }

    return compressedFile;
  }

  // Prevents OOM errors:
  // http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
  private BitmapFactory.Options optimiseDecoderOptions(String path) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;

    BitmapFactory.decodeFile(path, options);

    int heightRatio = Math.round((float) options.outHeight / (float) 500);
    int widthRatio = Math.round((float) options.outWidth / (float) 500);

    options.inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    options.inJustDecodeBounds = false;
    return options;
  }
}
