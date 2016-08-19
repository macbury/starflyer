package de.macbury.geo;

import com.badlogic.gdx.utils.Array;
import com.google.gson.annotations.Expose;
import de.macbury.json.JsonHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * A GeoJSON object with the type "FeatureCollection" is a feature collection object.
 * An object of type "FeatureCollection" must have a member with the name "features". The value corresponding to "features" is an array. Each element in the array is a feature object as defined above.
 */
public class FeatureCollection extends GeoJSON implements Iterable<Feature> {
  @Expose
  private ArrayList<Feature> features;

  public ArrayList<Feature> getFeatures() {
    return features;
  }

  public void setFeatures(ArrayList<Feature> features) {
    this.features = features;
  }

  /**
   * Parses the given String as FeatureCollection and returns a concrete subclass of
   * {@link GeoJSON} corresponding to the type of the root object.
   * @param rawJson
   * @return
   */
  public static FeatureCollection parse(String rawJson) {
    return JsonHelper.fromJson(rawJson, FeatureCollection.class);
  }

  @Override
  public Iterator<Feature> iterator() {
    return features.iterator();
  }

  @Override
  public void forEach(Consumer<? super Feature> action) {
    features.forEach(action);
  }

  @Override
  public Spliterator<Feature> spliterator() {
    return features.spliterator();
  }
}
