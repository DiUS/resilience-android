package au.com.dius.resilience.model;

public class Profile {

  private static final String RESERVED_CHARS = "|\\?*<\":>+\\[\\]/'";

  private String name;

  public Profile(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getPreferencesFilename() {
    return name.toLowerCase().replaceAll(" ", "_").replaceAll(RESERVED_CHARS, "");
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {

    if (obj instanceof Profile) {
      Profile prof2 = (Profile) obj;
      return this.name.equals(prof2.getName());
    }

    return false;
  }
}
