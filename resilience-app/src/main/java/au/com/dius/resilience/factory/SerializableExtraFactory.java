package au.com.dius.resilience.factory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import au.com.dius.resilience.util.Logger;
import com.google.inject.Inject;
import roboguice.inject.ContextSingleton;

import java.io.Serializable;

/*
 * Factory for creating (or in reality, retrieving) a bundled extra from
 * an Activity. Encapsulates checking for nulls in the process of
 * retrieving a value from an extras bundle.
 */
@ContextSingleton
public class SerializableExtraFactory {

  public static final String LOG_TAG = SerializableExtraFactory.class.getName();

  // TODO - tests!
  @Inject
  public Serializable createSerializable(Activity anActivity, String key) {
    Intent intent = anActivity.getIntent();

    if (intent == null) {
      logWarning(key, "intent is null.");
      return null;
    }

    Bundle extras = intent.getExtras();

    if (extras == null) {
      logWarning(key, "extras is null.");
      return null;
    }

    Serializable serializable = extras.getSerializable(key);

    if (serializable == null) {
      logWarning(key, "key exists, but value is null.");
    }

    return serializable;
  }

  private void logWarning(String key, String reason) {
    Logger.w(this, "Couldn't deserialise for key ", key, ": ", reason);
  }
}
