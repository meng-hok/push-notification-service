package com.kosign.push.publics;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.kosign.push.devices.Device;
import com.kosign.push.devices.DeviceService;
import com.kosign.push.messages.APNS;
import com.kosign.push.messages.Agent;
import com.kosign.push.messages.FCM;
import com.kosign.push.notifications.NotificationService;
import com.kosign.push.platformSetting.PlatformSetting;
import com.kosign.push.platformSetting.PlatformSettingService;
import com.kosign.push.utils.KeyConf;
import com.kosign.push.utils.RabbitSender;
import com.kosign.push.utils.Response;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
    
    

    @PostMapping("/send/single/user")
    public Object sendByUser(String app_id, String receiver_id, String title,String message) {
        try {
              System.out.println(app_id + receiver_id);
           
              Agent agent = deviceService.getActiveDeviceByUserIdAndAppIdRaw(receiver_id, app_id);
           
            String response = null;
            
            if(agent.platform_id.equals(KeyConf.PlatForm.IOS)){
               
                response=   notificationService.sendNotificationToIOS(KeyConf.PlatForm.GETP8FILEPATH+agent.pfilename,agent.team_id, agent.file_key, agent.bundle_id, agent.token, title, message);
               
                logger.info("[ Response Sucess : APNS ]");
                // logger.info(device.toString());
   
               return Response.getResponseBody(KeyConf.Message.SUCCESS,response , true);
          
            }else if(KeyConf.PlatForm.ANDROID.equals(agent.platform_id) ){

                response= notificationService.sendNotificationToFCM(agent.authorized_key, agent.token,title,message);
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

    @PostMapping("/send/single/device")
    public Object sendByDevice(String app_id, String deviceId, String title,String message) {
        try {
            //   System.out.println(app_id + deviceId);
           
              Agent agent = deviceService.getActiveDeviceByDeviceIdAndAppIdRaw(deviceId,app_id);
           
            String response = null;
            
            if(agent.platform_id.equals(KeyConf.PlatForm.IOS)){
               
                response=   notificationService.sendNotificationToIOS(KeyConf.PlatForm.GETP8FILEPATH+agent.pfilename,agent.team_id, agent.file_key, agent.bundle_id, agent.token, title, message);
               
                logger.info("[ Response Sucess : APNS ]");
                // logger.info(device.toString());
   
               return Response.getResponseBody(KeyConf.Message.SUCCESS,response , true);
          
            }else if(KeyConf.PlatForm.ANDROID.equals(agent.platform_id) ){

                response= notificationService.sendNotificationToFCM(agent.authorized_key, agent.token,title,message);
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

    @PostMapping("/send/all")
    public Object send(String app_id, String title,String message) {
        Integer success=0;
        Integer fail=0;
        List<Map<String,String>> devices = deviceService.getActiveDeviceByAppIdRaw(app_id);
        
        for (Map<String,String> device : devices) {
            try{
                if(KeyConf.PlatForm.IOS.equals(device.get("platform_id"))){
                    notificationService.sendNotificationToIOS(KeyConf.PlatForm.GETP8FILEPATH+device.get("pfilename"),device.get("team_id"),device.get("file_key"), device.get("bundle_id"), device.get("token"), title, message);
                }else{
                    notificationService.sendNotificationToFCM(device.get("authorized_key"), device.get("token"),title,message);
                }
                ++success;
            }catch(Exception e){
                logger.info("Error Message");
                System.out.println(e.getMessage());
                ++fail;
            }
        }

        // devices.forEach(device -> {
           
           
        // });
        
        return Response.getResponseBody(success.toString(),fail.toString(), true);
    }

}