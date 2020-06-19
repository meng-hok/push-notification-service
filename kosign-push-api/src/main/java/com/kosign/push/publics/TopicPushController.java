package com.kosign.push.publics;

import com.kosign.push.topics.TopicService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@PreAuthorize("#oauth2.hasScope('READ')")
@RestController
@RequestMapping("/api/public")
public class TopicPushController {

    private TopicService topicService;

    @ApiOperation(value = "Send to All FCM device subscribed to Topic ")
    @PostMapping("/topics/send")
    public Object sendToTopic(String appId,String title,String message,String topicName) throws Exception{
       return topicService.sendTo(appId,topicName,title,message);
    }

    @ApiOperation(value = "Subscribe all FCM device to Topic")
    @PostMapping("/topics/subscribe/allToTopics")
    public Object subscribe(String appId,String topicName) throws  Exception{
//        return topicService.subscribe("",topicName,null);createByTopicNameAndAppId
        return topicService.createByTopicNameAndAppId(topicName,appId);
    }

    @ApiOperation(value = "Getting available Topic From Application")
    @GetMapping("/topics")
    public Object myTopic (String appId){
       return topicService.getActiveTopicsByAppId(appId);
    }

    @ApiOperation(value = "Getting All Device From Specific Application Topic")
    @GetMapping("/topics/devices")
    public Object myClient (String appId,String topicName){
        return topicService.getTopicDetailByAppIdAndTopicName(appId,topicName);
    }

}

