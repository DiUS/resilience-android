package au.com.dius.resilience.test.unit.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import au.com.dius.resilience.factory.MediaFileFactory;
import au.com.dius.resilience.model.MediaType;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.dius.resilience.ui.activity.CreateServiceRequestActivity;
import au.com.dius.resilience.ui.adapter.ServiceListSpinnerAdapter;
import au.com.justinb.open311.GenericRequestAdapter;
import au.com.justinb.open311.model.ServiceList;
import au.com.justinb.open311.model.ServiceRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.File;

import static au.com.dius.resilience.test.unit.utils.TestHelper.getField;
import static au.com.dius.resilience.test.unit.utils.TestHelper.setField;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(ResilienceTestRunner.class)
public class CreateServiceRequestActivityTest {

  private CreateServiceRequestActivity createServiceRequestActivity;

  @Mock
  private ServiceListSpinnerAdapter serviceListSpinnerAdapter;

  @Mock
  private View button;

  @Mock
  private MediaFileFactory mediaFileFactory;

  @Mock
  private Spinner serviceSpinner;

  @Mock
  private GenericRequestAdapter requestAdapter;

  @Mock
  private ServiceList serviceList;

  private EditText descriptionField;

  @Before
  public void setup() throws NoSuchFieldException {
    when(mediaFileFactory.createMediaFile(MediaType.PHOTO)).thenReturn(new File("test"));

    createServiceRequestActivity = new CreateServiceRequestActivity();
    descriptionField = new EditText(createServiceRequestActivity);
    setField(createServiceRequestActivity, "mediaFileFactory", mediaFileFactory);
    setField(createServiceRequestActivity, "requestAdapter", requestAdapter);
    setField(createServiceRequestActivity, "serviceSpinner", serviceSpinner);
    setField(createServiceRequestActivity, "descriptionField", descriptionField);
  }

  @Test
  public void shouldPopulateServiceSpinner() {
    createServiceRequestActivity.onCreate(null);

    Spinner serviceSpinner = (Spinner) getField(createServiceRequestActivity, "serviceSpinner");
    SpinnerAdapter adapter = serviceSpinner.getAdapter();
    assertThat(adapter, instanceOf(ServiceListSpinnerAdapter.class));
  }

  @Test
  public void shouldLaunchCameraIntentOnButtonClick() {
    CreateServiceRequestActivity spy = spy(createServiceRequestActivity);
    spy.onCameraButtonClick(button);
    verify(spy).startActivityForResult(any(Intent.class),
      eq(CreateServiceRequestActivity.CAPTURE_PHOTO_REQUEST_CODE));
  }

  @Test
  public void shouldForwardToRequestAdapterForCreation() {
    given(serviceSpinner.getSelectedItem()).willReturn(serviceList);
//    createServiceRequestActivity.onSubmitClick(button);
//    verify(requestAdapter).create(any(ServiceRequest.class));
  }

  /* TODO - In addition to the default stored services, we should
   TODO download any new ones that may have been added on the server-side. */
//  @Test
//  public void shouldAddNonDefaultServices() {
//    createServiceRequestActivity.onLoadFinished(null, serviceList);
//  }
}
