package com.kosign.push.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ObjectUtils  extends org.apache.commons.lang3.ObjectUtils {
    static private ObjectMapper mapper;

    private static Logger logger = LoggerFactory.getLogger(ObjectUtils.class);

    static {
            mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            mapper.setSerializationInclusion(Include.NON_NULL);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            }

    public static <T> T readValue(String str, TypeReference<T> tr) {
        try {
            return mapper.readValue(str, tr);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    public static JsonNode readTree(String str) {
        try {
            return mapper.readTree(str);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        return null;
    }

    public static String writeValueAsString(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
        }

        return null;
    }
}
