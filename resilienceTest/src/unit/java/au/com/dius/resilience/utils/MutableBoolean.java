package au.com.dius.resilience.utils;

/**
 * @author georgepapas
 */
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
