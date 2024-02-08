package com.redcare.util;


import org.junit.jupiter.api.Test;

import javax.json.Json;
import javax.json.JsonObject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class JsonUtilTest {
    @Test
    void nullSafeLongValueShouldReturnLongValueWhenNotNull() {
        JsonObject jsonObject = Json.createObjectBuilder().add("value", 42L).build();
        Long result = JsonUtil.nullSafeLongValue(jsonObject.getJsonNumber("value"));

        assertEquals(42L, result);
    }

    @Test
    void nullSafeLongValueShouldReturnNullWhenNull() {
        JsonObject jsonObject = Json.createObjectBuilder().build();
        Long result = JsonUtil.nullSafeLongValue(jsonObject.getJsonNumber("value"));

        assertNull(result);
    }

    @Test
    void nullSafeGetStringShouldReturnStringValueWhenKeyExists() {
        JsonObject jsonObject = Json.createObjectBuilder().add("name", "John").build();
        String result = JsonUtil.nullSafeGetString(jsonObject, "name");

        assertEquals("John", result);
    }

    @Test
    void nullSafeGetStringShouldReturnNullWhenKeyDoesNotExist() {
        JsonObject jsonObject = Json.createObjectBuilder().add("age", 25).build();
        String result = JsonUtil.nullSafeGetString(jsonObject, "name");

        assertNull(result);
    }
}
