package au.com.dius.resilience.persistence.repository.impl;

import android.content.res.Resources;
import au.com.dius.resilience.R;
import au.com.dius.resilience.loader.ImageLoader;
import au.com.dius.resilience.util.Logger;
import com.cloudinary.Cloudinary;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CloudinaryRepository {

  private Cloudinary cloudinary;

  public static final String API_KEY = "api_key";
  public static final String API_SECRET = "api_secret";
  public static final String PUBLIC_ID = "public_id";
  public static final String JPG = ".jpg";

  public CloudinaryRepository(Resources resources) {
    cloudinary = new Cloudinary();

    cloudinary.setConfig(ImageLoader.CLOUD_NAME, resources.getString(R.string.cloudinary_cloud_name));
    cloudinary.setConfig(API_KEY, resources.getString(R.string.cloudinary_api_key));
    cloudinary.setConfig(API_SECRET, resources.getString(R.string.cloudinary_api_secret));
  }

  public String create(File imageFile) throws IOException {

    try {
      Map result = cloudinary.uploader().upload(imageFile, Cloudinary.emptyMap());
      String publicId = (String) result.get(PUBLIC_ID);
      return cloudinary.url().generate(publicId + JPG);
    } catch (IOException e) {
      Logger.e(this, "Failed to upload image to Cloudinary: " + e.getMessage());
      throw e;
    }
  }
}