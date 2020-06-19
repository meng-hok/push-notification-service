package com.kosign.push.utils;

public interface KeyConf {
    
    class Message {
        public static String SUCCESS = "Successful";
        public static String FAIL = "Fail";
    }
    class Notification{
       public static String TITILE = "Push Server";
       public static String MESSAGE = "Hello";
    }

    class Status {
        public static Character ACTIVE = '1';
        public static Character DISABLED = '9';
        public static Character REQUESTING = '0';
    }

    class PlatForm {
        public static String IOS = "1";
        public static String ANDROID = "2";
        public static String WEB= "3";
        public static String PUTP8FILEPATH = "kosign-push-api/src/main/resources/static/files";
        public static String GETP8FILEPATH = "kosign-push-api/src/main/resources/static/files/";
    }

    class Agent {

        public static Character APNS = '1';
        public static Character FCM = '2';
    }
    class RabbitMQ {
        

        public static String FCMROUTING = "pusher.queue.fcm";
        public static String APNSROUTING ="pusher.queue.apns";
        public static String TOPICROUTING = "pusher.queue.topic";
    }
}