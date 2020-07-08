package com.kosign.push.publics;

import com.kosign.push.devices.DeviceEntity;
import com.kosign.push.topics.TopicService;
import com.kosign.push.utils.KeyConf;
import com.kosign.push.utils.RabbitSender;
import com.kosign.push.utils.Response;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//@AllArgsConstructor
////@PreAuthorize("#oauth2.hasScope('READ')")
//@RestController
//@RequestMapping("/api/public")
public class TopicPushController {

    private TopicService topicService;


    @ApiOperation(value = "Getting available Topic From Application #2cf57d2b-ebe4-4539-af0a-98d252f6b8e2")
    @GetMapping("/topics")
    public Object myTopic (String appId){
        return Response.getResponseBody(KeyConf.Message.SUCCESS,topicService.getActiveTopicsByAppId(appId),true);
    }

    @ApiOperation(value = "Register Topic")
    @PostMapping("/topics/create")
    public Object registerTopic (String appId, String topicName){
        try {

            return Response.getResponseBody(KeyConf.Message.SUCCESS, topicService.registerTopic(appId,topicName) ,true);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return Response.getResponseBody(KeyConf.Message.FAIL,  e.getMessage(), false);
        }

    }
    @ApiOperation(value = "Getting All Device From Specific Application Topic #onDeveloping")
    @GetMapping("/topics/devices")
    public Object myClient (String appId,String topicName){
        return Response.getResponseBody(KeyConf.Message.SUCCESS,topicService.getTopicDetailByAppIdAndTopicName(appId,topicName),true);
    }

    @ApiOperation(value = "Unsubscribe Device from Topic ")
    @PostMapping("/topics/remove")
    public Object registerTopic (String appId, String topicName, ArrayList<DeviceEntity> devices){
        try {

            return Response.getResponseBody(KeyConf.Message.SUCCESS,  topicService.unsubscribeUserFromTopic(appId,topicName,devices) ,true);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return Response.getResponseBody(KeyConf.Message.FAIL,  e.getMessage(), false);
        }

    }
    //    @ApiOperation(value = "Subscribe all FCM device to Topic")
//    @PostMapping("/topics/subscribe/allToTopics")
    public Object subscribe(String appId,String topicName) throws  Exception{
//        return topicService.subscribe("",topicName,null);createByTopicNameAndAppId
        return Response.getResponseBody(KeyConf.Message.SUCCESS,topicService.createByTopicNameAndAppId(topicName,appId),true);
    }

    @ApiOperation(value = "Send to All FCM device subscribed to Topic ")
    @PostMapping("/topics/send")
    public Object sendToTopic(String appId,String title,String message,String topicName) {
        try{
            return Response.getResponseBody(KeyConf.Message.SUCCESS,topicService.sendTo(appId,topicName,title,message),true);
        }catch (Exception ex){
            return Response.getResponseBody(KeyConf.Message.FAIL,ex.getMessage(),false);
        }


    }


    /**
     * devices requires Id & Token
     *
     * */
    @ApiOperation(value = "Add Many Device to FCM")
    @PostMapping("/topics/devices/insert")
    public Object addDeviceToTopic (String appId, String topicName, @RequestBody ArrayList<DeviceEntity> devices){


        /**
         * Validate token
         *
         * */
        try {
            return Response.getResponseBody(KeyConf.Message.SUCCESS,topicService.subscribeUserToTopic(appId,topicName,devices),true);
        } catch (Exception ex){
            return Response.getResponseBody(KeyConf.Message.FAIL,ex.getMessage(),false);
        }


    }

//
//    @ApiOperation(value = "Add Many Device to APNS")
//    @PostMapping("/topics/devices/apns/insert")
//    public Object addDeviceAPNSToTopic (String appId, String topicName, @RequestBody ArrayList<Device> devices){
//
//
//        /**
//         * Validate token
//         *
//         * */
//        try {
//            return Response.getResponseBody(KeyConf.Message.SUCCESS,topicService.subscribeUserToTopic(appId,topicName,devices),true);
//        } catch (Exception ex){
//            return Response.getResponseBody(KeyConf.Message.FAIL,ex.getMessage(),false);
//        }
//
//
//    }


    @ApiOperation(value = "Unsubscribe Device from Topic")
    @PostMapping("/topics/devices/remove")
    public Object unsubscribeDeviceFromTopic(String appId, String topicName, @RequestBody ArrayList<DeviceEntity> devices){
        try {
            return Response.getResponseBody(KeyConf.Message.SUCCESS,topicService.unsubscribeUserFromTopic(appId, topicName, devices),true);
        } catch (Exception ex){
            return Response.getResponseBody(KeyConf.Message.FAIL,ex.getMessage(),false);
        }
    }
}

