package com.kosign.push.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {
    public final static Gson GSON_OBJECT;

    static {
        GSON_OBJECT = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

    }


}
