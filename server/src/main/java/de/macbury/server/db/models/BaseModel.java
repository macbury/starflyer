package de.macbury.server.db.models;

import java.util.Date;

/**
 * Base model
 */
public class BaseModel {
  private String id;

  /**
   * Database id
   * @return
   */
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
