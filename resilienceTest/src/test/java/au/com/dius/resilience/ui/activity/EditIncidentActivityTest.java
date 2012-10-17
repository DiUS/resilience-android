package au.com.dius.resilience.ui.activity;

import android.test.TouchUtils;
import android.widget.*;
import au.com.dius.resilience.AbstractResilienceTestCase;
import au.com.dius.resilience.R;
import au.com.dius.resilience.model.Impact;
import au.com.dius.resilience.persistence.repository.impl.AbstractSqlLiteRepository;
import com.jayway.android.robotium.solo.Solo;

public class EditIncidentActivityTest extends AbstractResilienceTestCase<EditIncidentActivity> {

  public EditIncidentActivityTest() {
    super(EditIncidentActivity.class);
  }

  @Override
  protected void beforeTest() {
    getActivity().getApplication().deleteDatabase(AbstractSqlLiteRepository.DB_NAME);
  }

  @Override
  protected void afterTest() {
    getActivity().getApplication().deleteDatabase(AbstractSqlLiteRepository.DB_NAME);
  }

  public void testCreateAndSaveIncident() {
    
    solo.pressSpinnerItem(0, 2);
    solo.pressSpinnerItem(1, 3);

    EditText notes = (EditText) solo.getView(R.id.notes);
    Button createButton = (Button) solo.getView(R.id.submit_incident);
    
    solo.typeText(notes, "fire");
    solo.clickOnView(createButton);

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
    TextView impactRatingLbl = (TextView) solo.getView(R.id.impact_scale_desc);
    assertEquals(Impact.LOW.name(), impactRatingLbl.getText().toString());
    
    final SeekBar impactRating = (SeekBar) solo.getView(R.id.impact_scale);
    solo.setProgressBar(impactRating, 50);
    assertEquals(Impact.MEDIUM.name(), impactRatingLbl.getText().toString());
    
    solo.setProgressBar(impactRating, 100);
    assertEquals(Impact.HIGH.name(), impactRatingLbl.getText().toString());
  }

}
