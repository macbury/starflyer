package de.macbury.server.db.models;

import java.util.Date;

/**
 * Base model
 */
public class BaseModel<TypeOfId> {
  protected TypeOfId id;

  /**
   * Database id
   * @return
   */
  public TypeOfId getId() {
    return id;
  }

  public void setId(TypeOfId id) {
    this.id = id;
  }
}
