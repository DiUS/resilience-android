package au.com.dius.resilience;

import android.app.Application;

import com.parse.Parse;

public class ResilienceApplication extends Application {

  @Override
  public void onCreate() {
    Parse.initialize(this, "RiO29avl2HCTX49CphzVrpRKawqUFSUJbHtZRitM",
        "GO9aRT96Wmht7x6ztSTnm4CXNJcd46vSZcaIA9Jm");
  }
}
