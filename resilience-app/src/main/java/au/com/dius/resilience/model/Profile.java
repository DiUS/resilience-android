package au.com.dius.resilience.model;

public class Profile {

  private static final String RESERVED_CHARS = "|\\?*<\":>+\\[\\]/'";

  private String name;
  private String id;

  public Profile(String name) {
    this.name = name;
    this.id = encodeId(name);
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  private String encodeId(String value) {
    return value.toLowerCase().replaceAll(" ", "_").replaceAll(RESERVED_CHARS, "");
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
