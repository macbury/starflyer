package de.macbury.server.db.models;

import java.util.Date;

/**
 * Base model
 */
public interface BaseModel<TypeOfId> {

  /**
   * Database id
   * @return
   */
  public TypeOfId getId();
  public void setId(TypeOfId newId);
}
