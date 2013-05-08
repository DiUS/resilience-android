package au.com.dius.resilience.test.unit.ui.activity;

import android.app.Activity;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import au.com.dius.resilience.model.ServiceListDefaults;
import au.com.dius.resilience.persistence.repository.Repository;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.dius.resilience.test.unit.utils.TestHelper;
import au.com.dius.resilience.ui.activity.CreateServiceRequestActivity;
import au.com.dius.resilience.ui.adapter.ServiceListSpinnerAdapter;
import au.com.justinb.open311.model.ServiceList;
import com.google.inject.AbstractModule;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.List;

import static au.com.dius.resilience.test.unit.utils.TestHelper.getField;
import static au.com.dius.resilience.test.unit.utils.TestHelper.setField;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(ResilienceTestRunner.class)
public class CreateServiceRequestActivityTest {

  private CreateServiceRequestActivity createServiceRequestActivity;

  @Mock
  private ServiceListSpinnerAdapter serviceListSpinnerAdapter;

  @Before
  public void setup() throws NoSuchFieldException {
    createServiceRequestActivity = new CreateServiceRequestActivity();
  }

  @Test
  public void shouldPopulateServiceSpinner() {
    createServiceRequestActivity.onCreate(null);

    Spinner serviceSpinner = (Spinner) getField(createServiceRequestActivity, "serviceSpinner");
    SpinnerAdapter adapter = serviceSpinner.getAdapter();
    assertThat(adapter, instanceOf(ServiceListSpinnerAdapter.class));
  }

  /* TODO - In addition to the default stored services, we should
   TODO download any new ones that may have been added on the server-side. */
//  @Test
//  public void shouldAddNonDefaultServices() {
//    createServiceRequestActivity.onLoadFinished(null, serviceList);
//  }
}
