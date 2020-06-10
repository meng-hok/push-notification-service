package com.kosign.push.commons;

import com.kosign.push.logging.AppLogManager;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public abstract class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {
    static private ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(Include.NON_NULL);
    }

    public static <T> T readValue(String str, TypeReference<T> tr) {
        try {
            return mapper.readValue(str, tr);
        } catch (Exception e) {
            AppLogManager.error(e);
        }

        return null;
    }

    public static JsonNode readTree(String str) {
        try {
            return mapper.readTree(str);
        } catch (JsonProcessingException e) {
            AppLogManager.error(e);
        } catch (IOException e) {
            AppLogManager.error(e);
        }

        return null;
    }

    public static String writeValueAsString(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            AppLogManager.error(e);
        }

        return null;
    }

//    public String toString() {
//        try {
//            return mapper.writeValueAsString(this);
//        } catch (JsonProcessingException e) {
//            AppLogManager.error(e);
//        }
//
//        return null;
//    }

}
