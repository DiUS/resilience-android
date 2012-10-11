package au.com.dius.resilience.model;

public class Incident {

  private Long id;
  private String name;
  private Long dateCreated;
  private String note;

  public Incident(Long id, String name, Long dateCreated, String note) {
    this.id = id;
    this.name = name;
    this.dateCreated = dateCreated;
    this.note = note;
  }
  
  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  void setName(String name) {
    this.name = name;
  }

  public Long getDateCreated() {
    return dateCreated;
  }

  void setDateCreated(Long dateCreated) {
    this.dateCreated = dateCreated;
  }
  
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }
}
