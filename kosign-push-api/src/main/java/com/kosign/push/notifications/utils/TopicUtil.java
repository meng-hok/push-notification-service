package com.kosign.push.notifications.utils;

import com.kosign.push.utils.HttpClient;
import org.json.JSONObject;

public class TopicUtil {

//    public static final String FIREBASE_TOPIC_API_URL = "https://iid.googleapis.com/iid/v1/%3CREGISTRATION_TOKEN%3E/rel/topics/%3CTOPIC_NAME%3E";


    /*
        https://iid.googleapis.com/iid/info/IID_TOKEN

         https://iid.googleapis.com/iid/v1/IID_TOKEN/rel/topics/TOPIC_NAME


         https://iid.googleapis.com/iid/v1:batchAdd
         https://iid.googleapis.com/iid/v1:batchRemove
    */

    public static String TOPIC_MULTI_SUBSCRIBE_URL= "https://iid.googleapis.com/iid/v1:batchAdd";

    public static String TOPIC_MULTI_UNSUBSCRIBE_URL= "https://iid.googleapis.com/iid/v1:batchRemove";

    public static  String getFirebaseTopicSingleSubscribeApiUrl(String token,String topicName){
        return "https://iid.googleapis.com/iid/v1/"+token+"/rel/topics/"+topicName;
    }
    public static  String getFirebaseTopicMultiSubscribeApiUrl(String token,String topicName){
        return "https://iid.googleapis.com/iid/v1/"+token+"/rel/topics/"+topicName;
    }
    public static  HttpClient getSingleTopicHeader(String authorizedKey) {
        return FirebaseUtil.getHttpClientWithFirebaseHeader(authorizedKey);
    }
    public static  HttpClient getGroupTopicHeader(String authorizedKey) {
        return FirebaseUtil.getHttpClientWithFirebaseHeader(authorizedKey);
    }

    public static JSONObject getNotificationBody(String topicName, JSONObject jsonBody) throws Exception {
        JSONObject body = new JSONObject();
        body.put("to","/topics/"+topicName);
        body.put("priority", "high");
        body.put("notification", jsonBody);
        // body.put("data", getNotificationJsonForm(requestBody));
        return body;
    }
}
