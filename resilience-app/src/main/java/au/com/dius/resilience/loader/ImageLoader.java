package au.com.dius.resilience.loader;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import au.com.dius.resilience.R;
import au.com.dius.resilience.factory.ImageManagerFactory;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.google.inject.Inject;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import com.novoda.imageloader.core.model.ImageTag;
import com.novoda.imageloader.core.model.ImageTagFactory;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectResource;

@ContextSingleton
public class ImageLoader {

  public static final String CLOUD_NAME = "cloud_name";

  private ImageTagFactory imageTagFactory;
  private ImageManager imageManager;
  private Transformation thumbnailTransformation = new Transformation().width(150).height(150).crop("thumb");

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
    cloudinary.setConfig(CLOUD_NAME, context.getString(R.string.cloudinary_cloud_name));
  }

  private void initialiseImageLoader() {

    imageTagFactory = ImageTagFactory.newInstance(context, R.drawable.background_border);
    imageTagFactory.setErrorImageId(R.drawable.border_white);
  }

  public void loadThumbnailImage(ImageView view, String imageName) {
    String thumbnailUrl = cloudinary.url().transformation(thumbnailTransformation).generate(imageName);

    ImageTag tag = imageTagFactory.build(thumbnailUrl, context);
    view.setTag(tag);
    imageManager.getLoader().load(view);
  }
}