package au.com.dius.resilience.event;

public interface Publisher {
  void subscribe(Object subscriber);
  void unsubscribe(Object unsubscriber);
}
