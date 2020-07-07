package com.kosign.push.utils;

import java.util.HashMap;
import java.util.Map;

public class Response {
    
    public static Map<String, Object> getResponseBody(String msg, Object data,Boolean status ) 
    {
        Map<String, Object> response = new HashMap<>();
        response.put("msg", msg);
        response.put("data", data);
        response.put("status",status);
        return response;
    }

    public static Map<String, Object> getSuccessResponseNonDataBody(String msg) 
    {
        Map<String, Object> response = new HashMap<>();
        response.put("msg", msg);
        response.put("status",true);
        return response;
    }
    public static Map<String, Object> getFailResponseNonDataBody(String msg) 
    {
        Map<String, Object> response = new HashMap<>();
        response.put("msg", msg);
        response.put("status",false);
        return response;
    }
}