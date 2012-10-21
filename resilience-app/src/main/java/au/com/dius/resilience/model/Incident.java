package au.com.dius.resilience.model;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Incident implements Serializable {

  private String id;
  private String name;
  private Long dateCreated;
  private String note;
  private String category;
  private String subCategory;
  private Impact scale;
  private Point point;

  private List<Photo> photos = new ArrayList<Photo>();

  public Incident(){}

  public Incident(
          String id,
          String name,
          Long dateCreated,
          String note,
          String category,
          String subCategory,
          Impact scale) {

    this(name, dateCreated, note, category, subCategory, scale);
    this.id = id;
  }

  public Incident(String name, Long dateCreated, String note, String category, String subCategory, Impact scale) {
    this.name = name;
    this.dateCreated = dateCreated;
    this.note = note;
    this.category = category;
    this.subCategory = subCategory;
    this.scale = scale;
  }

  public Impact getImpact() {
    return scale;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
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

  public String getCategory() {
    return category;
  }

  public String getSubCategory() {
    return subCategory;
  }
  
  public List<Photo> getPhotos() {
    return photos;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setSubCategory(String subCategory) {
    this.subCategory = subCategory;
  }

  public void setScale(Impact scale) {
    this.scale = scale;
  }
  
  public void addPhotos(List<Photo> photos) {
    this.photos.addAll(photos);
  }

  public void setId(String id) {
    this.id = id;
  }

  public Point getPoint() {
    return point;
  }

  public void setPoint(Point point) {
    this.point = point;
  }
}
