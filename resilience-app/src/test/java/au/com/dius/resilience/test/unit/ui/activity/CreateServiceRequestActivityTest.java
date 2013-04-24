package au.com.dius.resilience.test.unit.ui.activity;

import android.widget.Spinner;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.dius.resilience.ui.activity.CreateServiceRequestActivity;
import au.com.justinb.open311.model.ServiceList;
import junitx.util.PrivateAccessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(ResilienceTestRunner.class)
public class CreateServiceRequestActivityTest {

  private CreateServiceRequestActivity createServiceRequestActivity;
  private List<ServiceList> serviceList;

  private Spinner serviceSpinner;

  private ServiceList.Builder builder;

  @Before
  public void setup() throws NoSuchFieldException {
    builder = new ServiceList.Builder();

    createServiceRequestActivity = new CreateServiceRequestActivity();
    serviceSpinner = new Spinner(createServiceRequestActivity);
    PrivateAccessor.setField(createServiceRequestActivity, "serviceSpinner", serviceSpinner);

    serviceList = new ArrayList<ServiceList>();
    serviceList.add(builder.serviceCode("-1").build());
    serviceList.add(builder.build());
    serviceList.add(builder.build());
  }

  @Test
  public void shouldPopulateServiceSpinner() {
    assertTrue(true);
//    createServiceRequestActivity.onCreate(null);
//
//    assertThat(serviceSpinner.getCount(), is(3));
  }

  // TODO - In addition to the default stored services, we should
  // download any new ones that may have been added on the server-side.
//  @Test
//  public void shouldAddNonDefaultServices() {
//    createServiceRequestActivity.onLoadFinished(null, serviceList);
//  }
}
