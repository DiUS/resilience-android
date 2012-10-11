package au.com.dius.resilience;

/**
 * @author georgepapas
 */
public class RuntimeProperties {

  /**
   * Eventually generate different versions of this file based on system properties and templates
   */

  public static final boolean useStrictMode() {
    return true;
  }

  public static final boolean useLiveDb() {
    return false;
  }
}
