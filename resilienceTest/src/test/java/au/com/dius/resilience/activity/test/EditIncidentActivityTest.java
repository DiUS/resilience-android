package au.com.dius.resilience.activity.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import au.com.dius.resilience.R;
import au.com.dius.resilience.activity.EditIncidentActivity;

public class EditIncidentActivityTest extends
    ActivityInstrumentationTestCase2<EditIncidentActivity> {

  private EditIncidentActivity activity;

  public EditIncidentActivityTest() {
    super("au.com.dius.resilience", EditIncidentActivity.class);
  }

  public void setUp() {
    activity = getActivity();
  }

  public void testSaveNoteToDB() {
    
    EditText notes = (EditText) activity.findViewById(R.id.incident_note);
    Button createButton = (Button) activity.findViewById(R.id.submit_incident);
    
    TouchUtils.clickView(this, notes);
    this.sendKeys(KeyEvent.KEYCODE_F, KeyEvent.KEYCODE_I, KeyEvent.KEYCODE_R, KeyEvent.KEYCODE_E);
    TouchUtils.clickView(this, notes);
    
    this.sendKeys(KeyEvent.KEYCODE_BACK);
    
    TouchUtils.clickView(this, createButton);
  }
}
