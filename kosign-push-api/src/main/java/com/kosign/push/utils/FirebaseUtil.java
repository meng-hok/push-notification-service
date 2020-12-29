package com.kosign.push.utils;


import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.Notification;
import com.google.gson.JsonObject;
import com.kosign.push.*;
import com.kosign.push.platformSetting.dto.FCM;
import org.json.JSONObject;
import org.springframework.boot.builder.*;

import java.util.*;


public class FirebaseUtil {

    public static final String FIREBASE_SERVER_KEY = "AAAAaO0Mlvo:APA91bECN2HOnEXHEGtqhmZyWZFj7Fo07uYH_nBgii67hR3q51b7qbixv1c4-kFSpCqwwbsjFWsrKUHVflUMO-jUSyld_0TimLUemtoNu2NVT1iXb6YVHdRXbTRbQlCaT_BQ2tnn-bOn";
    public static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";



//     public static JSONObject getNotificationJsonForm(HashMap<String, Object> requestBody) throws Exception{

//         JSONObject notification = new JSONObject();
//         notification.put("title", requestBody.get(KeyConf.Notification.TITILE));
//         notification.put("body",  requestBody.get(KeyConf.Notification.MESSAGE));
//         notification.put("badge", 1);
//         notification.put("sound", "default");
// //        notification.put("content-available",1);
//         return notification;
//     }

    // public static JSONObject getNotificationBody(String token, HashMap<String, Object> requestBody) throws Exception {
    //     JSONObject body = new JSONObject();
    //     body.put("to", token);
    //     body.put("priority", "high");
    //     body.put("notification", getNotificationJsonForm(requestBody));
    //     body.put("data", getNotificationJsonForm(requestBody));
    //     return body;
    // }
    public static JSONObject getNotificationBody( FCM fcm) throws Exception {
        Notification notification = Notification.builder()
                .setImage(fcm.getImage())
                .setBody(fcm.getMessage())
                .setTitle(fcm.getTitle())
                .build();
        JSONObject body = new JSONObject();

        body.put("data", fcm.getActionType());
        body.put("to", fcm.getToken());
        body.put("priority", "high");
        body.put("notification",new JSONObject(GsonUtils.GSON_OBJECT.toJson(notification)));

        if(fcm.getBadgeCount() == null)
            return body;

        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(AndroidNotification
                        .builder()
                        .setNotificationCount(fcm.getBadgeCount()).build())
                .build();
        body.put("android",new JSONObject(GsonUtils.GSON_OBJECT.toJson(androidConfig)));
        return body;

        // body.put("data", getNotificationJsonForm(requestBody));

    }

    public static JSONObject getNotificationBody(String token, JSONObject jsonBody) throws Exception {
        JSONObject body = new JSONObject();
        body.put("to", token);
        body.put("priority", "high");
        body.put("notification", jsonBody);
        // body.put("data", getNotificationJsonForm(requestBody));
        return body;
    }

    public static HttpClient getHttpClientWithFirebaseHeader(){
        HttpClient client = new HttpClient();
        client.setHeader("Authorization", "key=" + FIREBASE_SERVER_KEY);
        client.setHeader("Content-Type", "application/json;charset=UTF-8");
        return client;
    }

    public static HttpClient getHttpClientWithFirebaseHeader(String appAuthorizedKey){
        HttpClient client = new HttpClient();
        client.setHeader("Authorization", "key=" + appAuthorizedKey);
        client.setHeader("Content-Type", "application/json;charset=UTF-8");
        return client;
    }

    public static void main(String[] args) {
       Map data=new HashMap();
       data.put("test","Hello work");
       data.put("time","haha haha");
       data.put("ti", "hello dara");
       Iterator it=data.entrySet().iterator();
      while (it.hasNext()){
          Map.Entry pair=(Map.Entry)it.next();
          System.out.println(pair.getKey() +" = "+ pair.getValue());
          it.remove();
      }
    }
}

