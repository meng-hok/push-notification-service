package com.kosign.push.publics;
import java.util.List;

import com.kosign.push.devices.DeviceService;
import com.kosign.push.devices.RequestDevice;
import com.kosign.push.utils.messages.APNS;
import com.kosign.push.utils.messages.Agent;
import com.kosign.push.utils.messages.FCM;
import com.kosign.push.notifications.NotificationService;
import com.kosign.push.platformSetting.PlatformSettingService;
import com.kosign.push.utils.KeyConf;
import com.kosign.push.utils.RabbitSender;
import com.kosign.push.utils.Response;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/notifications")
public class SinglePushController {
   
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private DeviceService deviceService;
   
    @Autowired
    private PlatformSettingService settingService;

    @Autowired 
    private RabbitSender rabbitSender;

    Logger logger = LoggerFactory.getLogger(SinglePushController.class);
    
    
    
/*
    @PostMapping("/send/single/user")
    public Object sendByUser(String app_id, String receiver_id, String title,String message) {
        try {
              System.out.println(app_id + receiver_id);
           
              Agent agent = deviceService.getActiveDeviceByUserIdAndAppIdRaw(receiver_id, app_id);
           
            String response = null;
            
            if(agent.platform_id.equals(KeyConf.PlatForm.IOS)){
               
               rabbitSender.sendToApns(new APNS(KeyConf.PlatForm.GETP8FILEPATH+agent.pfilename,agent.team_id, agent.file_key, agent.bundle_id, agent.token, title, message)); ;
                response= agent.toString();
                logger.info("[ Response Sucess : APNS ]");
                // logger.info(device.toString());
   
               return Response.getResponseBody(KeyConf.Message.SUCCESS,response , true);
          
            }else if(KeyConf.PlatForm.ANDROID.equals(agent.platform_id) ){

                rabbitSender.sendToFcm(new FCM(agent.authorized_key, agent.token,title,message));
                response= agent.toString();
                logger.info("[ Response Sucess : FCM ]");
                // logger.info(device.toString());
   
               return Response.getResponseBody(KeyConf.Message.SUCCESS,new JSONObject(response).toMap() , true);
            }
            
            throw new Exception("Device Id not found");
         
        } catch (Exception e) {
            logger.info(e.getMessage());
            return Response.getResponseBody("Push notification fail", e.getLocalizedMessage(), false);
        }
      
    }
    */

    @PostMapping("/send/single/device")
    public Object sendByDevice(String app_id, String deviceId, String title,String message) {
        try {
            //   System.out.println(app_id + deviceId);
           
              Agent agent = deviceService.getActiveDeviceByDeviceIdAndAppIdRaw(deviceId,app_id);
           
            String response = null;
            
            if(agent.platform_id.equals(KeyConf.PlatForm.IOS)){
               
//               notificationService.sendNotificationToIOS(KeyConf.PlatForm.GETP8FILEPATH+agent.pfilename,agent.team_id, agent.file_key, agent.bundle_id, agent.token, title, message);
                rabbitSender.sendToApns(new APNS(KeyConf.PlatForm.GETP8FILEPATH+agent.pfilename,agent.team_id, agent.file_key, agent.bundle_id, agent.token, title, message));
                logger.info("[ Response Sucess : APNS ]");

                response = agent.toString();

                return Response.getResponseBody(KeyConf.Message.SUCCESS,response , true);
          
            }else if(KeyConf.PlatForm.ANDROID.equals(agent.platform_id) ){
//                notificationService.sendNotificationToFCM(agent.authorized_key,agent.token,title,message);
               rabbitSender.sendToFcm(new FCM( agent.authorized_key, agent.token,title,message));
                logger.info("[ Response Sucess : FCM ]");

                response=agent.toString();
               return Response.getResponseBody(KeyConf.Message.SUCCESS,"{}" , true);
            }
            
            throw new Exception("Device Id not found");
         
        } catch (Exception e) {
            logger.info(e.getMessage());
            return Response.getResponseBody("Push notification fail", e.getLocalizedMessage(), false);
        }
      
    }

    @PostMapping("/devices/send")
    public Object send(String app_id, String title, String message , @RequestBody RequestDevice requestDevice) {
        Integer success =0 ;
        Integer fail = 0;
        List<Agent> devices = deviceService.getActiveDevicesByDeviceIdListAndAppId(requestDevice.getDeviceIdList(),app_id);

        for (Agent device : devices) {
            try{
                if(KeyConf.PlatForm.IOS.equals(device.getPlatform_id())){
                     rabbitSender.sendToApns(new APNS(KeyConf.PlatForm.GETP8FILEPATH+device.getPfilename(),device.getTeam_id(),device.getFile_key(), device.getBundle_id(), device.getToken(), title, message));
                }else{
                     rabbitSender.sendToFcm(new FCM(device.getAuthorized_key(), device.getToken(),title,message));
                }
                ++success;
            }catch(Exception e){
                logger.info("Error Message");
                System.out.println(e.getMessage());
                ++fail;
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success",success) ;
        jsonObject.put("fail",fail) ;
        jsonObject.put("target_devices" , devices.size() );

        return Response.getResponseBody(KeyConf.Message.SUCCESS,jsonObject.toMap() , true);
    }

}