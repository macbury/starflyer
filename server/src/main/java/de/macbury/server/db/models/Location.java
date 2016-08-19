package de.macbury.server.db.models;

import java.util.Date;

/**
 * Place in the world
 */
public class Location implements BaseModel<Long> {
  private double lat;
  private double lng;
  private String name;
  private Date timestamp;

  private String type;
  private String subType;
  private Long id;

  public Location() {

  }

  public long getOsmId() {
    return getId();
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLng() {
    return lng;
  }

  public void setLng(double lng) {
    this.lng = lng;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Is location valid
   * @return
   */
  public boolean isValid() {
    return lat != 0.0 && lat != 0.0 && name != null && timestamp != null && id != 0 && type != null && subType != null;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSubType() {
    return subType;
  }

  public void setSubType(String subType) {
    this.subType = subType;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long newId) {
    this.id = newId;
  }
}
