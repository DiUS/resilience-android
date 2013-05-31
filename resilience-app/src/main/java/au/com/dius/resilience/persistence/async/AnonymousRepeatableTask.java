package au.com.dius.resilience.persistence.async;

import android.os.AsyncTask;

// Since I can't get Robolectric to work with AsyncTask.execute,
// using a simple Task that extends AsyncTask instead.
public class AnonymousRepeatableTask {

  private Runnable runnable;

  public AnonymousRepeatableTask(Runnable runnable) {
    this.runnable = runnable;
  }

  public void execute() {
    new SimpleAnonymousAsyncTask(runnable).execute();
  }

  private class SimpleAnonymousAsyncTask extends AsyncTask<Object, Object, Object> {

    private Runnable runnable;

    public SimpleAnonymousAsyncTask(Runnable runnable) {
      this.runnable = runnable;
    }

    @Override
    protected Object doInBackground(Object... params) {
      this.runnable.run();
      return null;
    }
  }
}
