package au.com.dius.resilience.test.unit.ui.activity;

import android.widget.Spinner;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.dius.resilience.test.unit.utils.TestHelper;
import au.com.dius.resilience.ui.activity.CreateServiceRequestActivity;
import au.com.dius.resilience.ui.adapter.ServiceListSpinnerAdapter;
import au.com.justinb.open311.model.ServiceList;
import junitx.util.PrivateAccessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static au.com.dius.resilience.test.unit.utils.TestHelper.getField;
import static au.com.dius.resilience.test.unit.utils.TestHelper.setField;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(ResilienceTestRunner.class)
public class CreateServiceRequestActivityTest {

  private CreateServiceRequestActivity createServiceRequestActivity;
  private List<ServiceList> serviceList;

  @Mock
  private Spinner serviceSpinner;

  private ServiceList.Builder builder;

  private ServiceListSpinnerAdapter serviceListSpinnerAdapter;

  @Before
  public void setup() throws NoSuchFieldException {
    builder = new ServiceList.Builder();

    createServiceRequestActivity = new CreateServiceRequestActivity();
    serviceListSpinnerAdapter =
      (ServiceListSpinnerAdapter) getField(createServiceRequestActivity, "serviceListSpinnerAdapter");
  }

  @Test
  public void shouldPopulateServiceSpinner() {
    createServiceRequestActivity.onCreate(null);
    verify(serviceSpinner).setAdapter(serviceListSpinnerAdapter);
  }

  // TODO - In addition to the default stored services, we should
  // download any new ones that may have been added on the server-side.
//  @Test
//  public void shouldAddNonDefaultServices() {
//    createServiceRequestActivity.onLoadFinished(null, serviceList);
//  }
}
