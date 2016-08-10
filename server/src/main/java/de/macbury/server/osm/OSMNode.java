package de.macbury.server.osm;

import com.badlogic.gdx.utils.ObjectMap;
import de.macbury.server.db.models.Location;
import org.xml.sax.Attributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A node is one of the core elements in the OpenStreetMap data model. It consists of a single point in space defined by its latitude, longitude and node id.
 */
public class OSMNode {
  private static final String ATTRIBUTE_ID = "id";
  private static final String ATTRIBUTE_LAT = "lat";
  private static final String ATTRIBUTE_LON = "lon";
  private static final String ATTRIBUTE_TIMESTAMP = "timestamp";
  private static final String TYPE_AMENITY = "amenity";
  private static final String TYPE_HISTORIC = "historic";
  private static final String TYPE_TOURISM = "tourism";
  private static final String TYPE_NAME = "name";
  private final SimpleDateFormat formatter;
  /**
   * Node ids are unique between nodes. (However, a way or a relation can have the same id number as a node.) Editors may temporarily save node ids as negative to denote ids that haven't yet been saved to the server. Node ids on the server are persistent, meaning that the assigned id of an existing node will remain unchanged each time data are added or corrected. Deleted node ids must not be reused, unless a former node is now undeleted.
   */
  public long id;

  /**
   * Latitude coordinate in degrees (North of equator is positive) using the standard WGS84 projection. Some applications may not accept latitudes above/below ±85 degrees for some projections.
   */
  public double lat;

  /**
   * Longitude coordinate in degrees (East of Greenwich is positive) using the standard WGS84 projection. Note that the geographic poles will be exactly at latitude ±90 degrees but in that case the longitude will be set to an arbitrary value within this range.
   */
  public double lon;

  /**
   * 	time of the last modification
   */
  public Date timestamp;

  /**
   * A set of key/value pairs, with unique key with map features
   */
  public ObjectMap<String, String> tags = new ObjectMap<String, String>();

  public OSMNode() {
    this.formatter = new SimpleDateFormat("yyyy-MM-dd");
    this.reset();
  }

  public void reset() {
    tags.clear();
    lat = lon = 0;
    id = 0;
  }

  /**
   * Return type of node
   * @return
   */
  public String getType() {
    if (isAmenity()) {
      return TYPE_AMENITY;
    } else if (isHistoric()) {
      return TYPE_HISTORIC;
    } else if (isTourism()) {
      return TYPE_TOURISM;
    } else {
      return null;
    }
  }

  /**
   * @return
   */
  public String getSubType() {
    return tags.get(getType());
  }

  /**
   * Used to map facilities used by visitors and residents. For example: toilets, telephones, banks, pharmacies, cafes, parking and schools. See the page Amenities for an introduction on its usage.
   * @return
   */
  public boolean isAmenity() {
    return tags.containsKey(TYPE_AMENITY);
  }

  /**
   * his is used to describe various historic places. For example: archeological sites, wrecks, ruins, castles and ancient buildings. See the page titled Historic for an introduction on its usage.
   * @return
   */
  public boolean isHistoric() {
    return tags.containsKey(TYPE_HISTORIC);
  }

  /**
   * This is used to map places and things of specific interest to tourists. For example these may be places to see, places to stay and things and places providing support. See the page titled Tourism for an introduction on its usage.
   * @return
   */
  public boolean isTourism() {
    return tags.containsKey(TYPE_TOURISM);
  }

  public void readFrom(Attributes attributes) {
    reset();
    this.id = Long.parseLong(attributes.getValue(ATTRIBUTE_ID));
    this.lat = Double.parseDouble(attributes.getValue(ATTRIBUTE_LAT));
    this.lon = Double.parseDouble(attributes.getValue(ATTRIBUTE_LON));
    try {
      this.timestamp = this.formatter.parse(attributes.getValue(ATTRIBUTE_TIMESTAMP));
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public boolean haveName() {
    return tags.containsKey(TYPE_NAME);
  }

  public boolean isPointOfInterest() {
    return isAmenity() || isHistoric() || isTourism();
  }

  public String getName() {
    return tags.get(TYPE_NAME);
  }

  /**
   * Create raw model ready to insert into database
   * @return
   */
  public Location toLocation() {
    Location location = new Location();
    location.setId(id);
    location.setLng(lon);
    location.setLat(lat);
    location.setName(getName());
    location.setTimestamp(timestamp);
    location.setType(getType());
    location.setSubType(getSubType());
    return location;
  }

  public void setName(String newName) {
    tags.put(TYPE_NAME, newName);
  }


}
