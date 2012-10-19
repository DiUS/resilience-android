package au.com.dius.resilience.test.shared.utils;

public class MutableBoolean {

  private boolean bool;

  public MutableBoolean(boolean bool) {
    this.bool = bool;
  }

  public void setBool(boolean bool) {
    this.bool = bool;
  }

  public boolean isTrue() {
    return bool;
  }
}
