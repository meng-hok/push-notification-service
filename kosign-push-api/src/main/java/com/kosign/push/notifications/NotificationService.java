package com.kosign.push.notifications;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLException;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture;
import com.kosign.push.devices.Device;
import com.kosign.push.devices.DeviceService;
import com.kosign.push.history.NotificationHistory;
import com.kosign.push.history.NotificationHistoryService;
import com.kosign.push.messages.APNS;
import com.kosign.push.messages.FCM;
import com.kosign.push.notifications.utils.APNsUtil;
import com.kosign.push.notifications.utils.FirebaseUtil;
import com.kosign.push.users.User;
import com.kosign.push.utils.HttpClient;
import com.kosign.push.utils.KeyConf;
import com.kosign.push.utils.RabbitSender;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * NotificationService
 */
@Service
public class NotificationService {

    Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationHistoryService historyService;
    
    @Autowired
    private DeviceService deviceService;
    @Autowired
    RabbitSender rabbitSender;

    @RabbitListener(queues = "pusher.queue.fcm")
    public void sendNotificationByFCM(FCM fcm){
        System.out.println("Response Message from " + fcm);
        this.sendNotificationToFCM(fcm.authorizedKey,fcm.token,fcm.title,fcm.message);
    }

    @RabbitListener(queues = "pusher.queue.fcm")
    public void sendNotificationByFCMSupport(FCM fcm){
        System.out.println("Response Message from " + fcm);
        this.sendNotificationToFCM(fcm.authorizedKey,fcm.token,fcm.title,fcm.message);
    }

    @RabbitListener(queues = "pusher.queue.apns")
    public void sendNotificationByAPNS(APNS apns) {
        System.out.println("Response Message from  " + apns);
        try {
            this.sendNotificationToIOS(apns.p8file, apns.teamId, apns.fileKey, apns.bundleId, apns.token, apns.title, apns.message);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    @RabbitListener(queues = "pusher.queue.apns")
    public void sendNotificationByAPNSSupport(APNS apns)  {
        System.out.println("Response Message from " + apns);
        // System.out.println("Message read from myQueue APNS: " + apns);
        try {
            this.sendNotificationToIOS(apns.p8file, apns.teamId, apns.fileKey, apns.bundleId, apns.token, apns.title, apns.message);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }


    public String sendNotificationToFCM(String appAuthorizedKey, String userToken,String title ,String message) {
        HttpClient httpClient = FirebaseUtil.getHttpClientWithFirebaseHeader(appAuthorizedKey);

        JSONObject json = new JSONObject();
        json.put("body", message);
        json.put("title", title);

        try {

            JSONObject notificatedService = FirebaseUtil.getNotificationBody(userToken, json);

            logger.info("[Request To Firebase]");
            logger.info(notificatedService.toString());

            JSONObject jsonResult = httpClient.post(FirebaseUtil.FIREBASE_API_URL, notificatedService.toString());
            logger.info("[Response From Firebase]");
            logger.info(jsonResult.toString());
            
            Integer result = (Integer)jsonResult.get("success");

            org.json.JSONArray strResult = (org.json.JSONArray)jsonResult.get("results");
            historyService.saveHistory(new NotificationHistory(appAuthorizedKey, userToken, title, message,KeyConf.PlatForm.ANDROID,result.toString(),strResult.toString()));
            return jsonResult.toString();
        } catch (Exception e) {
            // TODO: handle exception
            logger.info("ERROR FROM SERVICE");
            logger.info(e.getMessage());
            historyService.saveHistory(new NotificationHistory(appAuthorizedKey, userToken, title, message,KeyConf.PlatForm.ANDROID,"0",e.getMessage()));
            return e.getMessage();
        }

    }

 
    public String sendNotificationToIOS(String p8file,String teamId,String fileKey,String bundleId,String token,String msgTitle,String msgBody) throws InterruptedException, ExecutionException, InvalidKeyException,
            SSLException, NoSuchAlgorithmException, IOException {
        final ApnsClient apnsClient = APNsUtil.getApnsCredentials(p8file,teamId,fileKey);
        
        ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();
        payloadBuilder.setAlertBody(msgBody);
        payloadBuilder.setAlertTitle(msgTitle);
        final String payload = payloadBuilder.build();
        
        logger.info("[ Request Payload From APNs]");
        logger.info(payload);
        
        SimpleApnsPushNotification pushNotification =  APNsUtil.getSimpleApnsWithPayAsString(token,bundleId, payload);
       
        PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
         sendNotificationFuture = apnsClient.sendNotification(pushNotification);
        
        logger.info("[ Response Data From APNs]");
       
        String responseStatus = "";
        String responseMsg = "";
        if(sendNotificationFuture.get().isAccepted()){
            logger.info("[Sucessfully Response]");
            System.out.println(sendNotificationFuture.get().toString());
            responseStatus = "true";
            responseMsg  = sendNotificationFuture.get().getApnsId().toString();
        }else{
            logger.info("[Error Response]");
            System.out.println(sendNotificationFuture.get().getRejectionReason());
            responseStatus = "false";
            responseMsg = sendNotificationFuture.get().getRejectionReason();
        }
        historyService.saveHistory(new NotificationHistory(token, token, msgTitle, msgBody,KeyConf.PlatForm.IOS,responseStatus,responseMsg));
        
         return sendNotificationFuture.get().toString();
    }
    public void sendNotificationByUserId(Integer userId){
        deviceService.getDevicesByUserId(userId).forEach(device -> {
            System.out.println(device);
            // String requestNotificationBody = this.sendNotification(device.getApp().get(), device.getToken());
            // historyService.saveHistory(new NotificationHistory(requestNotificationBody,new User(userId)));
        });

    }



}