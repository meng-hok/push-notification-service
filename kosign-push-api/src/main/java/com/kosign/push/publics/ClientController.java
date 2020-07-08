package com.kosign.push.publics;
import java.util.List;

import com.kosign.push.apps.AppEntity;
import com.kosign.push.devices.DeviceEntity;
import com.kosign.push.devices.DeviceService;
import com.kosign.push.devices.dto.RequestDevice;
import com.kosign.push.notificationHistory.dto.ResponseHistoryDto;
import com.kosign.push.platformSetting.dto.APNS;
import com.kosign.push.utils.messages.Agent;
import com.kosign.push.utils.messages.AgentBody;
import com.kosign.push.utils.messages.AgentIdentifier;
import com.kosign.push.utils.messages.AgentRequest;
import com.kosign.push.platformSetting.dto.FCM;
import com.kosign.push.notifications.NotificationService;
import com.kosign.push.platformSetting.PlatformSettingService;
import com.kosign.push.platforms.PlatformEntity;
import com.kosign.push.utils.FileStorage;
import com.kosign.push.utils.KeyConf;
import com.kosign.push.utils.RabbitSender;
import com.kosign.push.utils.Response;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;

@Api(tags = "KOSIGN Push Client API")
@RestController
@RequestMapping("/api/public")
public class ClientController extends SuperController{
   

    Logger logger = LoggerFactory.getLogger(ClientController.class);
    
    
    
    @ApiOperation(value="Subscribe Device To Application" ,notes = "DeviceId is required ")
    @PostMapping("/devices/create")
    public Object save(@RequestBody AgentIdentifier agentIdentifier){
      
        try {
           
            DeviceEntity device = deviceService.saveDevice(new DeviceEntity(agentIdentifier.getDevice_id(),agentIdentifier.getToken(),new AppEntity(agentIdentifier.getApp_id()),new PlatformEntity( agentIdentifier.getPlatform_id() )));
            System.out.println(device);
           
            return Response.getResponseBody(KeyConf.Message.SUCCESS,device  , true);
        } catch (Exception e) {
            
//            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            return Response.getResponseBody(KeyConf.Message.FAIL,  e.getLocalizedMessage(), false);
        }

    }

    @ApiOperation( value = "Send Notification To Single Device")
    @PostMapping("/notifications/devices/send/single")
    public Object sendByDevice(@RequestBody AgentRequest agentBody) {
        try {
            //   System.out.println(app_id + deviceId);
           
            Agent agent = deviceService.getActiveDeviceByDeviceIdAndAppIdRaw(agentBody.getDevice_id(),agentBody.getApp_id());
           
            String response = null;
             FCM fcm;
            switch (agent.platform_id){
                case "1":

                    APNS apns = new APNS(FileStorage.GETP8FILEPATH+agent.pfilename,agent.team_id, agent.file_key, agent.bundle_id, agent.token, agentBody.getTitle(), agentBody.getMessage());
                    apns.setAppId(agentBody.getApp_id());
                    rabbitSender.sendToApns(apns);
                    logger.info("[ Response Sucess : APNS ]");

                    response = agent.toString();

                    break;
            
                case "2" :

                    fcm = new FCM( agent.authorized_key, agent.token,agentBody.getTitle(), agentBody.getMessage());
                    fcm.setAppId(agentBody.getApp_id());
                    rabbitSender.sendToFcm(fcm);
                    logger.info("[ Response Sucess : FCM ]");

                    response=agent.toString();
                    break;
                case "3" :
                    fcm = new FCM( agent.authorized_key, agent.token,agentBody.getTitle(), agentBody.getMessage());
                    fcm.setAppId(agentBody.getApp_id());
                    rabbitSender.sendToFcm(fcm);
                    logger.info("[ Response Sucess : FCM ]");

                    response=agent.toString();
                    break;
                
                default : 
                    throw new Exception("Device Id not found");
            }

            return Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return Response.getResponseBody("Push notification fail", e.getLocalizedMessage(), false);
        }
      
    }

    @ApiOperation( value = "Send Notification To Device List",notes = "deviceIdList : [ASDQWE,WEQSADZZXC,QWEQE]")
    @PostMapping("/notifications/devices/send/groups")
    public Object send( @RequestBody RequestDevice requestDevice) {
        Integer success =0 ;
        Integer fail = 0;
        List<Agent> devices = deviceService.getActiveDevicesByDeviceIdListAndAppId(requestDevice.getDeviceIdList(),requestDevice.getApp_id());

        for (Agent device : devices) {
            FCM fcm;
            ++success;
            try{
                switch (device.platform_id){
                    case "1":
                        APNS apns = new APNS(FileStorage.GETP8FILEPATH+device.getPfilename(),device.getTeam_id(),device.getFile_key(), device.getBundle_id(), device.getToken(), requestDevice.getTitle(), requestDevice.getMessage());
                        apns.setAppId(requestDevice.getApp_id());
                        rabbitSender.sendToApns(apns);
                        break;
                    case "2":
                        fcm = new FCM(device.getAuthorized_key(), device.getToken(),requestDevice.getTitle(), requestDevice.getMessage());
                        fcm.setAppId(requestDevice.getApp_id());
                        rabbitSender.sendToFcm(fcm);
                        break;
                    case "3":
                        fcm = new FCM(device.getAuthorized_key(), device.getToken(),requestDevice.getTitle(), requestDevice.getMessage());
                        fcm.setAppId(requestDevice.getApp_id());
                        rabbitSender.sendToFcm(fcm);
                        break;
                    default : 
                        logger.info("Out of Platform");
                        --success;
                        ++fail ;
                        break;    
                }
               
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

        return Response.getResponseBody(KeyConf.Message.SUCCESS,"{}", true);
    }

   

    @ApiOperation( value = "Send Notification To Single Device")
    @PostMapping("/notifications/devices/send/all")
    public Object sendAll(@RequestBody AgentRequest agentBody){
        try {
            //   System.out.println(app_id + deviceId);
           
            List<Agent> agents = deviceService.getActiveDeviceByAppIdRaw(agentBody.getApp_id());
            Integer fail = 0;
           
            for(Agent agent : agents){
                FCM fcm;
                switch (agent.platform_id){
                    case "1":
    
                        APNS apns = new APNS(FileStorage.GETP8FILEPATH+agent.pfilename,agent.team_id, agent.file_key, agent.bundle_id, agent.token, agentBody.getTitle(), agentBody.getMessage());
                        apns.setAppId(agentBody.getApp_id());
                        rabbitSender.sendToApns(apns);
                        logger.info("[ Response Sucess : APNS ]");
    
    
                        break;
                
                    case "2" :
    
                        fcm = new FCM( agent.authorized_key, agent.token,agentBody.getTitle(), agentBody.getMessage());
                        fcm.setAppId(agentBody.getApp_id());
                        rabbitSender.sendToFcm(fcm);
                        logger.info("[ Response Sucess : FCM ]");
    
                        break;
                    case "3" :
                        fcm = new FCM( agent.authorized_key, agent.token,agentBody.getTitle(), agentBody.getMessage());
                        fcm.setAppId(agentBody.getApp_id());
                        rabbitSender.sendToFcm(fcm);
                        logger.info("[ Response Sucess : FCM ]");
    
                        break;
                    
                    default : 
                        logger.info("Out of Platform");
                        fail ++;
                        break;
                        // throw new Exception("Device Id not found");
                }

            };
            logger.info("Push Done");
            System.out.println("Device found : " + agents.size());
            System.out.println("Fail : " +fail);
            return Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return Response.getResponseBody("Push notification fail", e.getLocalizedMessage(), false);
        }
      
    }
}