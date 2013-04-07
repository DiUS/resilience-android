package au.com.dius.resilience.test.unit.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import au.com.dius.resilience.factory.SerializableExtraFactory;
import au.com.dius.resilience.intent.Extras;
import au.com.dius.resilience.test.unit.utils.ResilienceTestRunner;
import au.com.dius.resilience.ui.activity.ViewServiceRequestActivity;
import au.com.dius.resilience.util.ResilienceDateUtils;
import au.com.justinb.open311.model.ServiceRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.internal.IGoogleMapDelegate;
import junitx.util.PrivateAccessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.lang.reflect.Constructor;
import java.util.Date;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(ResilienceTestRunner.class)
public class ViewServiceRequestActivityTest {

  public static final Date NOW = new Date();
  public static final String FORMATTED_REPORTED_DATE = "7 APRIL, 13:59";
  public static final String DESCRIPTION = "This is a description of a Service Request.";

  @Mock
  private ServiceRequest serviceRequest;

  @Mock
  private ResilienceDateUtils dateUtils;
  @Mock
  private SerializableExtraFactory extraFactory;

  @Mock
  private TextView title;
  @Mock
  private TextView timeReported;
  @Mock
  private TextView description;

  private ViewServiceRequestActivity serviceRequestActivity;

  @Before
  public void setup() throws Exception {

    when(serviceRequest.getRequestedDatetime()).thenReturn(NOW);
    when(serviceRequest.getDescription()).thenReturn(DESCRIPTION);
    when(dateUtils.formatRelativeDate(NOW)).thenReturn(FORMATTED_REPORTED_DATE);

    serviceRequestActivity = new ViewServiceRequestActivity();

    PrivateAccessor.setField(serviceRequestActivity, "map", createGoogleMapInstance());
//    PrivateAccessor.setField(serviceRequestActivity, "serviceRequest", serviceRequest);
//    PrivateAccessor.setField(serviceRequestActivity, "title", title);
//    PrivateAccessor.setField(serviceRequestActivity, "description", description);
//    PrivateAccessor.setField(serviceRequestActivity, "dateUtils", dateUtils);
//    PrivateAccessor.setField(serviceRequestActivity, "extraFactory", extraFactory);
//    PrivateAccessor.setField(serviceRequestActivity, "title", title);
//    PrivateAccessor.setField(serviceRequestActivity, "timeReported", timeReported);
//    PrivateAccessor.setField(serviceRequestActivity, "description", description);
  }

  private Intent createServiceRequestIntent() {
    Bundle bundle = new Bundle();
    bundle.putSerializable(Extras.SERVICE_REQUEST, serviceRequest);
    Intent newIntent = new Intent();
    newIntent.putExtras(bundle);
    return newIntent;
  }

  // Constructor is protected, so we'll break that open with reflection. Some guys just want to see the world burn.
  private GoogleMap createGoogleMapInstance() throws Exception {
    Constructor<GoogleMap> constructor = GoogleMap.class.getDeclaredConstructor(IGoogleMapDelegate.class);
    constructor.setAccessible(true);
    return constructor.newInstance(mock(IGoogleMapDelegate.class));
  }

  // FIXME - Maybe this should be its own test class? When onCreate is called
  // Guice seems to want to inject its dependencies, which tramples on any mocking
  // that's been put in place. Maybe this is almost an integration test?
  @Test
  public void shouldSetServiceOnComplete() throws NoSuchFieldException {
    serviceRequestActivity.setIntent(createServiceRequestIntent());
    serviceRequestActivity.onCreate(null);

    ServiceRequest sRequest = (ServiceRequest) PrivateAccessor.getField(serviceRequestActivity, "serviceRequest");
    assertSame(sRequest, serviceRequest);
  }

// Argh.
//  @Test
//  public void populateViewFromValidServiceRequest() {
//    serviceRequestActivity.onResume();
//
//    verify(title).setText("Melbourne CBD");
//    verify(timeReported).setText(FORMATTED_REPORTED_DATE);
//    verify(description).setText(DESCRIPTION);
//  }
}
