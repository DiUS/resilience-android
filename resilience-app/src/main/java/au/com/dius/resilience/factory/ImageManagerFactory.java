package au.com.dius.resilience.factory;

import android.content.Context;
import com.novoda.imageloader.core.ImageManager;
import com.novoda.imageloader.core.LoaderSettings;
import roboguice.inject.ContextSingleton;

@ContextSingleton
public class ImageManagerFactory {
  public ImageManager createImageManager(Context context) {
    LoaderSettings settings = new LoaderSettings.SettingsBuilder()
      .withDisconnectOnEveryCall(true).build(context);
    return new ImageManager(context, settings);
  }
}
