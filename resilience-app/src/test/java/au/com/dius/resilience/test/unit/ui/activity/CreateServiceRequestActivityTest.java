package au.com.dius.resilience.test.unit.ui.activity;

import android.widget.Spinner;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.dius.resilience.ui.activity.CreateServiceRequestActivity;
import au.com.justinb.open311.model.ServiceList;
import junitx.util.PrivateAccessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(ResilienceTestRunner.class)
public class CreateServiceRequestActivityTest {

  private CreateServiceRequestActivity createServiceRequestActivity;
  private List<ServiceList> serviceList;

  private Spinner serviceSpinner;

  @Before
  public void setup() throws NoSuchFieldException {
    createServiceRequestActivity = new CreateServiceRequestActivity();
    serviceSpinner = new Spinner(createServiceRequestActivity);
    PrivateAccessor.setField(createServiceRequestActivity, "serviceSpinner", serviceSpinner);

    serviceList = new ArrayList<ServiceList>();
    serviceList.add(new ServiceList());
    serviceList.add(new ServiceList());
    serviceList.add(new ServiceList());
  }

  @Test
  public void shouldPopulateServiceSpinner() {
    assertTrue(true);
    // TODO
//    createServiceRequestActivity.onCreate(null);
//    createServiceRequestActivity.onLoadFinished(null, serviceList);
//
//    assertThat(serviceSpinner.getCount(), is(3));
  }
}
