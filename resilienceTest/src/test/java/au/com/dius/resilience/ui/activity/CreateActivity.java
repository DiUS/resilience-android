package au.com.dius.resilience.ui.activity;

import android.test.ActivityInstrumentationTestCase2;
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
    solo = new Solo(getInstrumentation(), getActivity());
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
    solo.enterText(0, "Something really bad has happened and I need tor raise an incident");
    solo.goBack();
    solo.clickOnButton("Create");

    solo.assertCurrentActivity("should be in list view", ResilienceActivity.class);

    int incidentsAfter = solo.getCurrentListViews().get(0).getCount();

    solo.sleep(500);

    assertEquals(incidentsBefore + 1, incidentsAfter);

  }


}
