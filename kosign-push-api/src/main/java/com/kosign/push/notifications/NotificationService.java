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
import com.kosign.push.notificationHistory.NotificationHistoryEntity;
import com.kosign.push.notificationHistory.NotificationHistoryService;
import com.kosign.push.platformSetting.dto.APNS;
import com.kosign.push.platformSetting.dto.FCM;
import com.kosign.push.utils.APNsUtil;
import com.kosign.push.utils.FirebaseUtil;
import com.kosign.push.utils.HttpClient;

import com.kosign.push.utils.RabbitSender;
import com.kosign.push.utils.enums.PlatformEnum;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
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
    	this.sendNotificationToFCM(fcm.getApp_id(),fcm.authorizedKey,fcm.token,fcm.title,fcm.message);
//        return  historyService.saveHistory(new NotificationHistory());
    }

    @RabbitListener(queues = "pusher.queue.fcm")
    public void sendNotificationByFCMSupport(FCM fcm)
    {
        System.out.println("Response Message from " + fcm);
        this.sendNotificationToFCM(fcm.getApp_id(),fcm.authorizedKey,fcm.token,fcm.title,fcm.message);
    }

    @RabbitListener(queues = "pusher.queue.apns")
    public void sendNotificationByAPNS(APNS apns) 
    {
        System.out.println("Response Message from  " + apns);
        try 
        {
            this.sendNotificationToIOS(apns.getApp_id(),apns.p8file, apns.teamId, apns.fileKey, apns.bundleId, apns.token, apns.title, apns.message);
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
            this.sendNotificationToIOS(apns.getApp_id(), apns.p8file, apns.teamId, apns.fileKey, apns.bundleId, apns.token, apns.title, apns.message);
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
    public NotificationHistoryEntity sendNotificationToFCM(String appId,String appAuthorizedKey, String userToken,String title ,String message) 
    {
        HttpClient httpClient = FirebaseUtil.getHttpClientWithFirebaseHeader(appAuthorizedKey);

        JSONObject json = new JSONObject();
        json.put("body", message);
        json.put("title", title);

        try 
        {
            JSONObject notificatedService = FirebaseUtil.getNotificationBody(userToken, json);

            logger.info("[Request To Firebase]");
            logger.info(notificatedService.toString());

            JSONObject jsonResult = httpClient.post(FirebaseUtil.FIREBASE_API_URL, notificatedService.toString());
            logger.info("[Response From Firebase]");
            logger.info(jsonResult.toString());
            
            Integer result = (Integer)jsonResult.get("success");
            org.json.JSONArray strResult = (org.json.JSONArray)jsonResult.get("results");

            logger.info(strResult.toString());

            NotificationHistoryEntity history = new NotificationHistoryEntity(appId, userToken, title, message,PlatformEnum.Platform.ANDROID,result.toString(),strResult.toString());


            historyService.insertHistory(history);
            return history;
        } 
        catch (Exception e) 
        {
            logger.info("ERROR FROM SERVICE");
            logger.info(e.getMessage());
            NotificationHistoryEntity historyException = new NotificationHistoryEntity(appAuthorizedKey, userToken, title, message,PlatformEnum.Platform.ANDROID,"0",e.getMessage());
            historyService.insertHistory(historyException);

            return historyException;
        }
    }


    /*
     * return object will be saved by Aspect
     *
     * */
    public String sendNotificationToIOS(String appId,String p8file,String teamId,String fileKey,String bundleId,String token,String msgTitle,String msgBody) throws InterruptedException, ExecutionException, InvalidKeyException,
            SSLException, NoSuchAlgorithmException, IOException 
    {
        try
        {
            final ApnsClient apnsClient = APNsUtil.getApnsCredentials(p8file,teamId,fileKey);

            ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();
            payloadBuilder.setAlertBody(msgBody);
            payloadBuilder.setAlertTitle(msgTitle);
            final String payload = payloadBuilder.build();

            logger.info("[ Request Payload From APNs]");
            logger.info(payload);

            SimpleApnsPushNotification pushNotification =  APNsUtil.getSimpleApnsWithPayAsString(token,bundleId, payload);


            logger.info("[ Request Payload To APNs]");
            PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
                    sendNotificationFuture = apnsClient.sendNotification(pushNotification);

            logger.info("[ Response Data From APNs]");

            String responseStatus = "";
            String responseMsg = "";
            if(sendNotificationFuture.get().isAccepted())
            {
                logger.info("[Sucessfully Response]");
                System.out.println(sendNotificationFuture.get().toString());
                responseStatus = "true";
                responseMsg  = sendNotificationFuture.get().getApnsId().toString();
            }
            else
            {
                logger.info("[Error Response]");
                System.out.println(sendNotificationFuture.get().getRejectionReason());
                responseStatus = "false";
                responseMsg = sendNotificationFuture.get().getRejectionReason();
            }

            NotificationHistoryEntity history =  new NotificationHistoryEntity(appId, token, msgTitle, msgBody,PlatformEnum.Platform.IOS,responseStatus,responseMsg);
            historyService.insertHistory(history);
            return history.toString();

        }
        catch (Exception e) 
        {
            NotificationHistoryEntity history =  new NotificationHistoryEntity(appId, token, msgTitle, msgBody,PlatformEnum.Platform.IOS,"0",e.getLocalizedMessage());
            historyService.insertHistory(history);
            return history.toString();
        }
    }
}