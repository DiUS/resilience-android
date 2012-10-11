package au.com.dius.resilience.activity.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import au.com.dius.resilience.R;
import au.com.dius.resilience.activity.EditIncidentActivity;
import au.com.dius.resilience.persistence.RepositoryFactory;
import au.com.dius.resilience.persistence.SqlLiteRepository;

public class EditIncidentActivityTest extends
    ActivityInstrumentationTestCase2<EditIncidentActivity> {

  private EditIncidentActivity activity;

  public EditIncidentActivityTest() {
    super("au.com.dius.resilience", EditIncidentActivity.class);
  }

  public void setUp() {
    activity = getActivity();
    RepositoryFactory.setTestFlag(true);
  }

  public void testSaveNoteToDB() {
    
    EditText notes = (EditText) activity.findViewById(R.id.incident_note);
    Button createButton = (Button) activity.findViewById(R.id.submit_incident);
    
    TouchUtils.clickView(this, notes);
    this.sendKeys(KeyEvent.KEYCODE_F, KeyEvent.KEYCODE_I, KeyEvent.KEYCODE_R, KeyEvent.KEYCODE_E);
    
    this.sendKeys(KeyEvent.KEYCODE_BACK);
    
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    
    TouchUtils.clickView(this, createButton);
  }
  
  public void tearDown() {
    getActivity().getApplication().deleteDatabase(SqlLiteRepository.DB_NAME);
  }
}
