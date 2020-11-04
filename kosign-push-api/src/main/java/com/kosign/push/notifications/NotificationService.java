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
import com.kosign.push.platformSetting.dto.APNSListener;
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
    	this.sendNotificationToFCM(fcm.getApp_id(),fcm.authorizedKey,fcm.token,fcm.title,fcm.message,fcm.bulkId);
//        return  historyService.saveHistory(new NotificationHistory());
    }

    @RabbitListener(queues = "pusher.queue.fcm")
    public void sendNotificationByFCMSupport(FCM fcm)
    {
        System.out.println("Response Message from " + fcm);
        this.sendNotificationToFCM(fcm.getApp_id(),fcm.authorizedKey,fcm.token,fcm.title,fcm.message,fcm.bulkId);
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
    public NotificationHistoryEntity sendNotificationToFCM(String appId,String appAuthorizedKey, String userToken,String title ,String message,String bulkId)
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
            history.setBulkId(bulkId);

            historyService.insertHistory(history);
            return history;
        } 
        catch (Exception e) 
        {
            logger.info("ERROR FROM SERVICE");
            logger.info(e.getMessage());
            NotificationHistoryEntity historyException = new NotificationHistoryEntity(appAuthorizedKey, userToken, title, message,PlatformEnum.Platform.ANDROID,"0",e.getMessage());
            historyException.setBulkId(bulkId);
            historyService.insertHistory(historyException);

            return historyException;
        }
    }


    /*
     * return object will be saved by Aspect
     *
     * */
    public String sendNotificationToIOS(APNSListener apns) throws InterruptedException, ExecutionException, InvalidKeyException,
            SSLException, NoSuchAlgorithmException, IOException 
    {
        try
        {
            final ApnsClient apnsClient = APNsUtil.getApnsCredentials(apns.getP8file(),apns.getTeamId(),apns.getFileKey(),apns.getRequestMode());

            ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();
            payloadBuilder.setAlertBody(apns.getMessage());
            payloadBuilder.setAlertTitle(apns.getTitle());
            payloadBuilder.setBadgeNumber(5);
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

            NotificationHistoryEntity history =  new NotificationHistoryEntity(apns.getApp_id(), apns.getToken(), apns.getTitle(), apns.getMessage(),PlatformEnum.Platform.IOS,responseStatus,responseMsg);
            history.setBulkId(apns.getBulkId());
            historyService.insertHistory(history);
            return history.toString();

        }
        catch (Exception e) 
        {
            NotificationHistoryEntity history =  new NotificationHistoryEntity(apns.getApp_id(), apns.getToken(), apns.getTitle(), apns.getMessage(),PlatformEnum.Platform.IOS,"0",e.getLocalizedMessage());
            history.setBulkId(apns.getBulkId());
            historyService.insertHistory(history);
            return history.toString();
        }
    }
}