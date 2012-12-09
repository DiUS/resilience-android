package au.com.dius.resilience.test.unit.ui.activity;

import au.com.dius.resilience.intent.Intents;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.ui.activity.EditIncidentActivity;
import com.xtremelabs.robolectric.Robolectric;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import roboguice.test.RobolectricRoboTestRunner;

import static au.com.dius.resilience.test.shared.utils.TestHelper.assertContainsIntents;

@RunWith(RobolectricRoboTestRunner.class)
public class EditIncidentActivityTest {

  private EditIncidentActivity editIncidentActivity;

  @Before
  public void setup() {
    editIncidentActivity = new EditIncidentActivity();
  }
}
