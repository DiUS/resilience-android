package au.com.dius.resilience.model;

import java.io.Serializable;

public class FeedbackResult implements Serializable {
  private boolean result;
  private Exception e;

  public FeedbackResult(boolean result, Exception e) {
    this.result = result;
    this.e = e;
  }

  public boolean getResult() {
    return result;
  }

  public Exception getException() {
    return e;
  }
}
