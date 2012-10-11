package au.com.dius.resilience.activity.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import au.com.dius.resilience.R;
import au.com.dius.resilience.activity.EditIncidentActivity;
import au.com.dius.resilience.model.ImpactScale;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.Repository;
import au.com.dius.resilience.persistence.RepositoryFactory;
import au.com.dius.resilience.persistence.SqlLiteRepository;

public class EditIncidentActivityTest extends
    ActivityInstrumentationTestCase2<EditIncidentActivity> {

  private EditIncidentActivity activity;
  private Repository repository;

  public EditIncidentActivityTest() {
    super("au.com.dius.resilience", EditIncidentActivity.class);
  }

  public void setUp() {
    activity = getActivity();
    repository = RepositoryFactory.create(getActivity());
  }

  public void testSaveNoteToDB() {
    
    Spinner categorySpinner = (Spinner) activity.findViewById(R.id.category_spinner);
    TouchUtils.clickView(this, categorySpinner);
    this.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
    this.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
    this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
    
    Spinner subCategorySpinner = (Spinner) activity.findViewById(R.id.sub_category_spinner);
    TouchUtils.clickView(this, subCategorySpinner);
    this.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
    this.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
    this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
    
    final SeekBar impactRating = (SeekBar) activity.findViewById(R.id.impact_scale);
    
    activity.runOnUiThread(new Runnable() {
      public void run() {
        impactRating.setProgress(ImpactScale.MEDIUM.getCode());
      }
    });
    
    EditText notes = (EditText) activity.findViewById(R.id.notes);
    Button createButton = (Button) activity.findViewById(R.id.submit_incident);
    
    TouchUtils.clickView(this, notes);
    
    this.sendKeys(KeyEvent.KEYCODE_F, KeyEvent.KEYCODE_I, KeyEvent.KEYCODE_R, KeyEvent.KEYCODE_E);
    this.sendKeys(KeyEvent.KEYCODE_BACK);
    
    sleep(2000);
    
    TouchUtils.clickView(this, createButton);
    
    // Check that it saved
    Incident incident = repository.findAll().get(0);
    
    assertEquals("fire", incident.getNote());
  }

  private void sleep(long time) {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
  
  public void tearDown() {
    getActivity().getApplication().deleteDatabase(SqlLiteRepository.DB_NAME);
  }
}
