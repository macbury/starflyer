package de.macbury.tests.json;

import com.badlogic.gdx.utils.Json;
import com.google.gson.annotations.Expose;
import de.macbury.json.JsonHelper;
import junit.framework.Assert;
import org.junit.Test;

class DummyJsonObject {
  @Expose
  public int id;
  public String email;
}

public class TestJson {

  @Test
  public void itShouldNotSerializeNotExposedFields() {
    DummyJsonObject originalObject = new DummyJsonObject();
    originalObject.id = 100;
    originalObject.email = "test@test.local";

    String rawJson = JsonHelper.toJson(originalObject);
    Assert.assertNotNull(rawJson);

    DummyJsonObject finalObject = JsonHelper.fromJson(rawJson, DummyJsonObject.class);
    Assert.assertEquals(originalObject.id, finalObject.id);
    Assert.assertNull(finalObject.email);
  }
}
