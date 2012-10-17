package au.com.dius.resilience.ui.activity;

import android.test.ActivityInstrumentationTestCase2;
import au.com.dius.resilience.test.util.ParseTestUtils;

import com.jayway.android.robotium.solo.Solo;

/**
 * @author georgepapas
 */
public class CreateActivity extends ActivityInstrumentationTestCase2<ResilienceActivity> {

  private Solo solo;

  public CreateActivity() {
    super(ResilienceActivity.class);
  }

  @Override
  public void setUp() throws Exception {
    getInstrumentation().waitForIdleSync();
    solo = new Solo(getInstrumentation(), getActivity());
    ParseTestUtils.setUp(getActivity());
    ParseTestUtils.dropAll(getInstrumentation());
  }

  @Override
  public void tearDown() {
    solo.finishOpenedActivities();
  }

  public void testCreateIncident() {

    final int incidentsBefore = solo.getCurrentListViews().get(0).getCount();
    solo.clickOnImage(1);
    solo.assertCurrentActivity("expected edit activity", EditIncidentActivity.class);

    solo.pressSpinnerItem(0, 1);
    
    solo.clickOnEditText(0);
    solo.enterText(0, "Something really bad has happened and I need to raise an incident");
    solo.goBack();
    
    solo.clickOnButton("Create");

    solo.waitForText("ALL INCIDENTS", 1, 10000);
    solo.waitForText("Fire", 1, 10000);
    
    int incidentsAfter = solo.getCurrentListViews().get(0).getCount();
    assertEquals(incidentsBefore + 1, incidentsAfter);
  }
}
