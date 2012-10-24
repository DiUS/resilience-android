package au.com.dius.resilience.test.unit.loader;

import android.content.Context;

import android.content.Loader;
import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;

@Implements(Loader.class)
public class LoaderShadow {

  private Context context;
  private boolean started = false;
  private Object data;
  private boolean takeContentChanged = false;
  private boolean forceLoadCalled = false;


  public void __constructor__(Context context) {
    this.context = context;
  }

  @Implementation
  public Context getContext() {
    return context;
  }

  @Implementation
  public boolean isStarted() {
    return started;
  }

  @Implementation
  public void deliverResult(Object data) {
    this.data = data;
  }

  public void setStarted(boolean value) {
    started = value;
  }

  public Object getDeliveredResult() {
    return this.data;
  }

  @Implementation
  public boolean takeContentChanged() {
    return takeContentChanged;
  }

  @Implementation
  public void forceLoad() {
    forceLoadCalled = true;
  }

  public boolean isForceLoadCalled() {
    return forceLoadCalled;
  }

  public void setTakeContentChanged(boolean takeContentChanged) {
    this.takeContentChanged = takeContentChanged;
  }
}
