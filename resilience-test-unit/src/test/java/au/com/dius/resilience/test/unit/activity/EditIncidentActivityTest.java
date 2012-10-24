package au.com.dius.resilience.test.unit.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import au.com.dius.resilience.loader.IncidentListLoader;
import au.com.dius.resilience.model.Incident;
import au.com.dius.resilience.persistence.repository.RepositoryCommandResult;
import au.com.dius.resilience.test.shared.utils.MutableBoolean;
import au.com.dius.resilience.ui.activity.EditIncidentActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import roboguice.test.RobolectricRoboTestRunner;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricRoboTestRunner.class)
public class EditIncidentActivityTest {


  private EditIncidentActivity editIncidentActivity;

  @Before
  public void setup() {
    editIncidentActivity = new EditIncidentActivity();
  }

  @Test
  public void shouldSendBroadCastMessageWhenIncidentCreated() {
    final MutableBoolean firedBroadCast = new MutableBoolean(false);

    editIncidentActivity.registerReceiver(new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        firedBroadCast.setBool(true);
      }
    }, new IntentFilter(IncidentListLoader.INCIDENT_LIST_LOADER_FILTER));

    editIncidentActivity.commandComplete(new RepositoryCommandResult<Incident>(true, new Incident()));
    assertTrue("broadcast message was not fired", firedBroadCast.isTrue());
  }
}
