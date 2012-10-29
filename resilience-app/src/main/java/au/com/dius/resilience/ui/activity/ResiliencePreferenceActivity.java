package au.com.dius.resilience.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import au.com.dius.resilience.R;
import au.com.dius.resilience.ui.Themer;

public class ResiliencePreferenceActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Themer.applyCurrentTheme(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_preference);
  }
}