package de.macbury.geo.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * A GeoJSON object with the type "FeatureCollection" is a feature collection object.
 * An object of type "FeatureCollection" must have a member with the name "features". The value corresponding to "features" is an array. Each element in the array is a feature object as defined above.
 */
public class FeatureCollection extends GeoJSON implements Iterable<Feature> {
  private ArrayList<Feature> features = new ArrayList<Feature>();

  public ArrayList<Feature> all() {
    return features;
  }

  public void setFeatures(ArrayList<Feature> features) {
    this.features = features;
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

  /**
   * Number of features in collection
   * @return
   */
  public int size() {
    return features.size();
  }

  public Feature get(int index) {
    return features.get(index);
  }

  public void add(Feature feature) {
    features.add(feature);
  }
}
