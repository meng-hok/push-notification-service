package com.kosign.push.notifications;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.SSLException;
import javax.transaction.Transactional;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kosign.push.notificationHistory.NotificationHistoryEntity;
import com.kosign.push.notificationHistory.NotificationHistoryService;
import com.kosign.push.platformSetting.dto.APNS;
import com.kosign.push.platformSetting.dto.APNSListener;
import com.kosign.push.platformSetting.dto.FCM;
import com.kosign.push.utils.*;

import com.kosign.push.utils.enums.PlatformEnum;

import org.codehaus.groovy.reflection.android.AndroidSupport;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.objenesis.instantiator.android.Android17Instantiator;
import org.springframework.stereotype.Service;

/**
 * NotificationService
 */
@Service
public class NotificationService 
{
    Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationHistoryService historyService;
    
    @Autowired
    RabbitSender rabbitSender;
   
    @RabbitListener(queues = "pusher.queue.fcm")
    public void sendNotificationByFCM(FCM fcm)
    {
    	System.out.println("Response Message from " + fcm);
//    	this.sendNotificationToFCM(fcm.getApp_id(),fcm.authorizedKey,fcm.token,fcm.title,fcm.message,fcm.bulkId);
        sendNotificationToFCM(fcm);

    }

    @RabbitListener(queues = "pusher.queue.fcm")
    public void sendNotificationByFCMSupport(FCM fcm)
    {
        System.out.println("Response Message from " + fcm);
//        this.sendNotificationToFCM(fcm.getApp_id(),fcm.authorizedKey,fcm.token,fcm.title,fcm.message,fcm.bulkId);
        sendNotificationToFCM(fcm);
    }

    @RabbitListener(queues = "pusher.queue.apns")
    public void sendNotificationByAPNS(APNS apns) 
    {
        System.out.println("Response Message from  " + apns);
        try 
        {
            APNSListener apnsListener = new APNSListener(apns,APNsUtil.APNS_PROD_MODE);
            this.sendNotificationToIOS(apnsListener);
        } 
        catch (Exception e) 
        {
            logger.info(e.getMessage());
        }
    }
    @RabbitListener(queues = "pusher.queue.apns")
    public void sendNotificationByAPNSSupport(APNS apns)
    {
        System.out.println("Response Message from " + apns);
        // System.out.println("Message read from myQueue APNS: " + apns);
        try 
        {
            APNSListener apnsListener = new APNSListener(apns,APNsUtil.APNS_PROD_MODE);
            this.sendNotificationToIOS(apnsListener);
        }
        catch (Exception e) 
        {
            logger.info(e.getMessage());
        }
    }

    @RabbitListener(queues = "pusher.queue.apns-dev")
    public void sendNotificationByAPNSDev(APNS apns) 
    {
        System.out.println("Response Message from  " + apns);
        try 
        {
            APNSListener apnsListener = new APNSListener(apns,APNsUtil.APNS_DEV_MODE);
            this.sendNotificationToIOS(apnsListener);
        } 
        catch (Exception e) 
        {
            logger.info(e.getMessage());
        }
    }

    /*
    * return object will be saved by Aspect
    *
    * */
    public void sendNotificationToFCM(FCM fcm)
    {
        HttpClient httpClient = FirebaseUtil.getHttpClientWithFirebaseHeader(fcm.getAuthorizedKey());
        String respMessage= "";
        String respStatus = "0";

//        JSONObject json = new JSONObject();
//        json.put("body", fcm.getMessage());
//        json.put("title", fcm.getTitle());
        //using real FCM
//        if(ObjectUtils.allNotNull(fcm.getBadgeCount()))
//            json.put(apns.getBadgeCount());
//        AndroidConfig.builder().setNotification(AndroidNotification.builder().setNotificationCount(fcm.getBadgeCount()).build()).build();

        try 
        {
            JSONObject notificatedService = FirebaseUtil.getNotificationBody(fcm);

            logger.info("[Request To Firebase]");
            logger.info(notificatedService.toString());

            JSONObject jsonResult = httpClient.post(FirebaseUtil.FIREBASE_API_URL, notificatedService.toString());
            logger.info("[Response From Firebase]");
            logger.info(jsonResult.toString());

            respStatus = jsonResult.get("success").toString();
            org.json.JSONArray strResult = (org.json.JSONArray)jsonResult.get("results");

            logger.info(strResult.toString());
            respMessage = strResult.toString();
        } 
        catch (Exception e) 
        {
            logger.info("ERROR FROM SERVICE");
            logger.info(e.getMessage());
            respMessage = e.getMessage();
        }finally {
            logger.info("CATCH WORKING ");
//            NotificationHistoryEntity history = NotificationHistoryEntity.builder().appId(fcm.getAppId())getAppId())., fcm.getToken(), fcm.getTitle(), fcm.getMessage(),PlatformEnum.Platform.ANDROID,respStatus,respMessage);
            NotificationHistoryEntity history = NotificationHistoryEntity.builder()
                    .appId(fcm.getAppId())
                    .recieverId(fcm.getToken())
                    .title(fcm.getTitle())
                    .message(fcm.getMessage())
                    .badgeCount(fcm.getBadgeCount())
                    .image(fcm.getImage())
                    .toPlatform(PlatformEnum.Platform.ANDROID)
                    .bulkId(fcm.getBulkId())
                    .responseMsg(respMessage)
                    .status(respStatus).build();

            this.saveHistory(history);
        }
    }


    /*
     * return object will be saved by Aspect
     *
     * */

    public void sendNotificationToIOS(APNSListener apns) throws InterruptedException, ExecutionException, InvalidKeyException,
            SSLException, NoSuchAlgorithmException, IOException 
    {

        String respStatus = "false";
        String respMsg = "";
        try
        {
            final ApnsClient apnsClient = APNsUtil.getApnsCredentials(apns.getP8file(),apns.getTeamId(),apns.getFileKey(),apns.getRequestMode());

            ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();
            payloadBuilder.setAlertBody(apns.getMessage());
            payloadBuilder.setAlertTitle(apns.getTitle());
            payloadBuilder.setLaunchImageFileName(apns.getImage());
            payloadBuilder.setBadgeNumber(apns.getBadgeCount());
            payloadBuilder.setContentAvailable(true);
            final String payload = payloadBuilder.build();

            logger.info("[ Request Payload From APNs]");
            logger.info(payload);

            SimpleApnsPushNotification pushNotification =  APNsUtil.getSimpleApnsWithPayAsString(apns.getToken(),apns.getBundleId(), payload);
            logger.info("[ Request Payload To APNs]");
            logger.info(apns.toString());
            PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
                    sendNotificationFuture = apnsClient.sendNotification(pushNotification);

            logger.info("[ Response Data From APNs]");
            logger.info(sendNotificationFuture.get().toString());

            if(sendNotificationFuture.get().isAccepted())
            {
                logger.info("[Sucessfully Response]");
                System.out.println(sendNotificationFuture.get().toString());
                respStatus = "true";
                respMsg  = sendNotificationFuture.get().getApnsId().toString();
            }
            else
            {
                logger.info("[Error Response]");
                System.out.println(sendNotificationFuture.get().getRejectionReason());
                respStatus = "false";
                respMsg = sendNotificationFuture.get().getRejectionReason();
            }

        }
        catch (Exception e) 
        {
            respMsg = e.getMessage();

        }finally {
            logger.info("CATCH WORKING ");

            NotificationHistoryEntity history = NotificationHistoryEntity.builder()
                    .appId(apns.getAppId())
                    .recieverId(apns.getToken())
                    .title(apns.getTitle())
                    .message(apns.getMessage())
                    .image(apns.getImage())
                    .badgeCount(apns.getBadgeCount())
                    .toPlatform(PlatformEnum.Platform.IOS)
                    .bulkId(apns.getBulkId())
                    .responseMsg(respMsg)
                    .status(respStatus).build();
            this.saveHistory(history);

        }
    }

    private void saveHistory(NotificationHistoryEntity history){
        history.setCount(1);

        try {
            historyService.insertHistory(history);
//            historyService.saveHistoryWithRabbit(history);
        }catch (Exception e){
            history.setResponseMsg(e.getMessage());
            historyService.insertHistory(history);
//            historyService.saveHistoryWithRabbit(history);
        }
    }

    public static void main(String[] args) {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();ceSystem.out.println(ObjectUtils.writeValueAsString(AndroidConfig.builder().setNotification(AndroidNotification.builder().setNotificationCount(6).build()).build()));
        AndroidNotification androidNotification = AndroidNotification.builder().setNotificationCount(6).build();
        System.out.println(GsonUtils.GSON_OBJECT.toJson(AndroidConfig.builder().setNotification(AndroidNotification.builder().setNotificationCount(6).build()).build()));
    }

}