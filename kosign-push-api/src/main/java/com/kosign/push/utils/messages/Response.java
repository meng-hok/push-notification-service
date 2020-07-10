package com.kosign.push.utils.messages;

import java.util.HashMap;
import java.util.Map;

public class Response 
{
    public static Map<String, Object> getResponseBody(String msg, Object data,Boolean status ) 
    {
        Map<String, Object> response = new HashMap<>();
        response.put("status",status);
        response.put("message", msg);
        response.put("data", data);
        
        return response;
    }

    public static Map<String, Object> getSuccessResponseNonDataBody(String msg) 
    {
        Map<String, Object> response = new HashMap<>();
        response.put("status",true);
        response.put("message", msg);
        
        return response;
    }
    public static Map<String, Object> getFailResponseNonDataBody(String msg) 
    {
        Map<String, Object> response = new HashMap<>();
        response.put("status",false);
        response.put("message", msg);
        
        return response;
    }
}