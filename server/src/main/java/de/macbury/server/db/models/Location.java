package de.macbury.server.db.models;

import java.util.Date;

/**
 * Place in the world
 */
public class Location extends BaseModel {
  private String osmId;
  private double lat;
  private double lng;
  private String name;
  private Date timestamp;

  public String getOsmId() {
    return osmId;
  }

  public void setOsmId(String osmId) {
    this.osmId = osmId;
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
}
