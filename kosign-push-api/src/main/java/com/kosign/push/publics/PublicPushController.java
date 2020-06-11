package com.kosign.push.publics;
import com.kosign.push.devices.Device;
import com.kosign.push.devices.DeviceService;
import com.kosign.push.messages.APNS;
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
public class PublicPushController {
   
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private DeviceService deviceService;
   
    @Autowired
    private PlatformSettingService settingService;

    @Autowired 
    private RabbitSender rabbitSender;

    Logger logger = LoggerFactory.getLogger(PublicPushController.class);
    
    
    @PostMapping("/send/single")
    public Object send(String app_id, String receiver_id, String title,String message) {
        try {
              System.out.println(app_id + receiver_id);
            Device device = deviceService.getActiveDeviceByDeviceIdAndAppId(receiver_id,app_id);
            PlatformSetting deviceSetting = settingService.getActivePlatformConfiguredByAppIdAndPlatFormId(app_id, device.getPlatform().getId());
            String response = null;
            if(device.getPlatform().getId().equals(KeyConf.PlatForm.IOS)){
                String p8file = deviceSetting.getPushUrl();
                String teamId = deviceSetting.getTeamId();
                String fileKey = deviceSetting.getKeyId();
                String groupId = deviceSetting.getBundleId();
                response=   notificationService.sendNotificationToIOS(KeyConf.PlatForm.P8FILEPATH+p8file, teamId, fileKey, groupId, device.getToken(), title, message);
            }else if(KeyConf.PlatForm.ANDROID.equals(deviceSetting.getPlatform().getId()) ){
                response= notificationService.sendNotificationToFCM(deviceSetting.getAuthorizedKey(), device.getToken(),title,message);
            }
        
            logger.info("[Device Response]");
             // logger.info(device.toString());

            return Response.getResponseBody(KeyConf.Message.SUCCESS,new JSONObject(response).toMap() , true);
        } catch (Exception e) {
            return Response.getResponseBody(KeyConf.Message.FAIL,"Push notification fail" , false);
        }
      
    }
}