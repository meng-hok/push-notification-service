package com.kosign.push.publics;

import com.kosign.push.apps.AppService;
import com.kosign.push.devices.DeviceService;
import com.kosign.push.mybatis.MyBatisRepository;
import com.kosign.push.platformSetting.PlatformSettingService;
import com.kosign.push.topics.TopicService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@AllArgsConstructor
//@RestController
//@RequestMapping("/api/public")
public class DevelopmentController {

    private AppService appService;

    private PlatformSettingService platformSettingService;

    private DeviceService deviceService;


    private TopicService topicService;

    private MyBatisRepository myBatisRepository;
//    public DevelopmentController(AppService appService, PlatformSettingService platformSettingService, DeviceService deviceService, TopicService topicService) {
//        this.appService = appService;
//        this.platformSettingService = platformSettingService;
//        this.deviceService = deviceService;
//    }

    @GetMapping("/topic")
    public Object home(String topicName,String appId) throws  Exception{
        return topicService.createByTopicNameAndAppId(topicName,appId);
    }



//    @GetMapping("/test/mybatis")
//    public Object mybatis(String appId,String topicName) throws  Exception{
//
//        return myBatisRepository.findAll();
//    }


    @GetMapping("/topic/unsubscribe")
    public Object unsubscribe(String topicName) throws  Exception{
        return topicService.unsubscribed("","appId",topicName);
    }

    @GetMapping("/topic/list")
    public Object myList(List<String> data){
        System.out.println(data);
        return "";
    }
}
