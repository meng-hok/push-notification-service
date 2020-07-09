package com.kosign.push.utils;

public interface KeyConf {
    
    class Message {
        public static final String P8FILENOTFOUND = "P 8 File Not Found";
        public static final String SUCCESS = "Successful";
        public static final String FAIL = "Fail";
        public static final String SETTINGNOTFOUNT = "Setting Not Found";
        public static final String INCORRECTPLATFORM = "Incorrect Platform Code";
        public static final String PLATFORMSETTINGREGISTERED = "Platform Setting Registered";
		public static final String ALREADYREGISTEREDDEVICE = "This Device id already exist";
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