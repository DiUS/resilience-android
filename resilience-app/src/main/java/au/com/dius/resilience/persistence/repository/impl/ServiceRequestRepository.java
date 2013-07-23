package au.com.dius.resilience.persistence.repository.impl;

import au.com.dius.resilience.event.Publisher;
import au.com.dius.resilience.util.Logger;
import au.com.justinb.open311.GenericRequestAdapter;
import au.com.justinb.open311.Open311Exception;
import au.com.justinb.open311.model.ServiceRequest;
import com.squareup.otto.Bus;

public class ServiceRequestRepository implements Publisher {

  public GenericRequestAdapter<ServiceRequest> requestAdapter;
  private final Bus bus;

  public class CreatedEvent {}
  public class ErrorEvent {}

  public ServiceRequestRepository() {
    requestAdapter = new GenericRequestAdapter<ServiceRequest>(ServiceRequest.class);
    bus = new Bus();
  }

  public void create(ServiceRequest serviceRequest) {
    try {
      requestAdapter.create(serviceRequest);
    } catch (Open311Exception e) {
      Logger.e(this, "Error creating service request");
      bus.post(new ErrorEvent());
    }

    bus.post(new CreatedEvent());
  }

  @Override
  public void subscribe(Object subscriber) {
    bus.register(subscriber);
  }

  @Override
  public void unsubscribe(Object unsubscriber) {
    bus.unregister(unsubscriber);
  }
}