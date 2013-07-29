package au.com.dius.resilience.loader;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.factory.ImageManagerFactory;
import au.com.dius.resilience.util.Logger;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.google.inject.Inject;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.model.ImageTag;
import com.novoda.imageloader.core.model.ImageTagFactory;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class ImageLoader {

  public static final String CLOUD_NAME = "cloud_name";

  private static final int THUMBNAIL_SIZE = 150;
  private static final String THUMB = "thumb";

  private ImageTagFactory imageTagFactory;
  private ImageManager imageManager;
  private Transformation thumbnailTransformation;

  private Cloudinary cloudinary;
  private Context context;

  @Inject
  public ImageLoader(Context context, Cloudinary cloudinary, ImageManagerFactory imageManagerFactory) {
    this.context = context;
    this.cloudinary = cloudinary;
    this.imageManager = imageManagerFactory.createImageManager(context);
    initialiseCloudinary();
    initialiseImageLoader();
  }

  private void initialiseCloudinary() {
    thumbnailTransformation = new Transformation();
    cloudinary.setConfig(CLOUD_NAME, context.getString(R.string.cloudinary_cloud_name));
  }

  private void initialiseImageLoader() {
    imageTagFactory = ImageTagFactory.newInstance(context, R.drawable.background_border);
    imageTagFactory.setErrorImageId(android.R.drawable.ic_menu_close_clear_cancel);
  }

  public void loadFullsizeImage(ImageView view, String imageName) {
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    int widthPixels = metrics.widthPixels;

    String thumbnailUrl = cloudinary.url().transformation(thumbnailTransformation
      .width(widthPixels)
      .crop(THUMB)
    ).generate(imageName);

    Logger.i(this, "Loading full sized image with width:", widthPixels);

    loadFromUrl(view, thumbnailUrl);
  }

  public void loadThumbnailImage(ImageView view, String imageName) {
    String thumbnailUrl = cloudinary.url().transformation(thumbnailTransformation.
      width(THUMBNAIL_SIZE)
      .height(THUMBNAIL_SIZE)
      .crop(THUMB))
      .generate(imageName);

    loadFromUrl(view, thumbnailUrl);
  }

  public void loadFromUrl(ImageView view, String url) {

    Logger.i(this, "Loading image from url", url);

    ImageTag tag = imageTagFactory.build(url, context);
    view.setTag(tag);
    imageManager.getLoader().load(view);
  }
}