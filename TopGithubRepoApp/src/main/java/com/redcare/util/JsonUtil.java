package com.redcare.util;

import org.springframework.stereotype.Component;

import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

@Component
public class JsonUtil {
    public static Long nullSafeLongValue(JsonNumber value) {
        if(value != null) {
            return value.longValue();
        }
        return null;
    }

    public static String nullSafeGetString(JsonObject jsonObject, String key) {
        if (jsonObject.containsKey(key)) {
            JsonValue jsonValue = jsonObject.get(key);
            if (jsonValue instanceof JsonString) {
                return ((JsonString) jsonValue).getString();
            }
        }
        return null;
    }
}
