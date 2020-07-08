package com.kosign.push.publics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kosign.push.apps.AppService;
import com.kosign.push.apps.AppEntity;
import com.kosign.push.devices.DeviceEntity;
import com.kosign.push.devices.DeviceService;
import com.kosign.push.notificationHistory.NotificationHistoryService;
import com.kosign.push.platformSetting.PlatformSetting;
import com.kosign.push.platformSetting.PlatformSettingService;
import com.kosign.push.platforms.PlatformService;
import com.kosign.push.topics.TopicService;
import com.kosign.push.users.UserService;
import com.kosign.push.utils.RabbitSender;

import io.swagger.annotations.Api;


@RestController
@RequestMapping("/api/public")
public class SuperController {
  
    @Autowired
    public AppService appService;
    @Autowired
    public PlatformSettingService platformSettingService;
    @Autowired
    public DeviceService deviceService;
    @Autowired
    public TopicService topicService;
    @Autowired
    public PlatformService platformService;
    @Autowired
    public NotificationHistoryService historyService;
    @Autowired
    public UserService userService;
    @Autowired 
    public RabbitSender rabbitSender;

}