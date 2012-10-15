package au.com.dius.resilience.ui.activity;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.view.KeyEvent;
import android.widget.*;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.ImpactScale;
import au.com.dius.resilience.persistence.repository.AbstractSqlLiteRepository;

public class EditIncidentActivityTest extends
    ActivityInstrumentationTestCase2<EditIncidentActivity> {

  private EditIncidentActivity activity;

  public EditIncidentActivityTest() {
    super("au.com.dius.resilience", EditIncidentActivity.class);
  }

  public void setUp() {
    activity = getActivity();
    activity.getApplication().deleteDatabase(AbstractSqlLiteRepository.DB_NAME);
  }
  
  // TODO - This test is rather big..
  // also, this test depends on production values
  // being present in the spinners, maybe we
  // could hook in a test ArrayAdapter for spinners
  // so it's more sand-boxy.
  public void testCreateAndSaveIncident() {
    
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
    
    EditText notes = (EditText) activity.findViewById(R.id.notes);
    Button createButton = (Button) activity.findViewById(R.id.submit_incident);
    
    TouchUtils.clickView(this, notes);
    
    this.sendKeys(KeyEvent.KEYCODE_F, KeyEvent.KEYCODE_I, KeyEvent.KEYCODE_R, KeyEvent.KEYCODE_E);
    this.sendKeys(KeyEvent.KEYCODE_BACK);
    
    sleep(2000);
    
    TouchUtils.clickView(this, createButton);

    //TODO Should be part of the UI tests, not db.. Mixing concerns here
    // Check that it saved
//    Incident incident = repository.findAll(getActivity()).get(0);
//
//    assertEquals("fire", incident.getNote());
//    assertEquals("Fire", incident.getCategory());
//    assertEquals("SubC1", incident.getSubCategory());
//    assertEquals(ImpactScale.LOW, incident.getImpact());
  }
  
  public void testImpactLabelChange() {
    // Test that the initial displayed impact is LOW
    TextView impactRatingLbl = (TextView) activity.findViewById(R.id.impact_scale_desc);
    assertEquals(ImpactScale.LOW.name(), impactRatingLbl.getText().toString());
    
    final SeekBar impactRating = (SeekBar) activity.findViewById(R.id.impact_scale);
    activity.runOnUiThread(new Runnable() {
      public void run() {
        impactRating.setProgress(ImpactScale.MEDIUM.getCode());
      }
    });
    sleep(2000);
    assertEquals(ImpactScale.MEDIUM.name(), impactRatingLbl.getText().toString());
    
    activity.runOnUiThread(new Runnable() {
      public void run() {
        impactRating.setProgress(ImpactScale.HIGH.getCode());
      }
    });
    sleep(2000);
    assertEquals(ImpactScale.HIGH.name(), impactRatingLbl.getText().toString());
  }

  private void sleep(long time) {
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
  
  public void tearDown() {
    activity.getApplication().deleteDatabase(AbstractSqlLiteRepository.DB_NAME);
    activity.finish();
  }
}
