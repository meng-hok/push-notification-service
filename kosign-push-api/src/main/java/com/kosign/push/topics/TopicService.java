package com.kosign.push.topics;

import com.google.firebase.FirebaseApp;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kosign.push.apps.AppService;
import com.kosign.push.apps.Application;
import com.kosign.push.devices.Device;
import com.kosign.push.devices.DeviceService;
import com.kosign.push.notifications.utils.FirebaseUtil;
import com.kosign.push.notifications.utils.TopicUtil;
import com.kosign.push.platformSetting.PlatformSetting;
import com.kosign.push.platformSetting.PlatformSettingService;
import com.kosign.push.topics.abstracts.TopicMethod;
import com.kosign.push.users.User;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.HttpClient;
import com.kosign.push.utils.KeyConf;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

//@AllArgsConstructor
@Service
public class TopicService implements TopicMethod {

    private TopicRepository topicRepository;

    private DeviceService deviceService;

    private AppService appService;

    private PlatformSettingService platformSettingService;

    public TopicService(TopicRepository topicRepository, DeviceService deviceService, AppService appService,PlatformSettingService platformSettingService) {
        this.topicRepository = topicRepository;
        this.deviceService = deviceService;
        this.appService = appService;
        this.platformSettingService = platformSettingService;
    }

    Logger logger = LoggerFactory.getLogger(TopicService.class);


    public List<Topic>  getTopicDetailByAppIdAndTopicName(String appId, String topicName) {
        return topicRepository.findAllByApplicationIdAndNameAndStatus(appId,topicName,KeyConf.Status.ACTIVE);
    }
    public List<Topic> getActiveTopicsByAppId(String AppId){
       return topicRepository.findAllByApplicationIdAndStatus(AppId,KeyConf.Status.ACTIVE);
    }



    public JSONObject sendTo(String appId, String topicName, String title, String message) throws Exception {

        String authKey = platformSettingService.getFcmAuthorizedKeyByAppId(appId);
//        String authKey = FirebaseUtil.FIREBASE_SERVER_KEY;
        HttpClient httpClient =  TopicUtil.getGroupTopicHeader(authKey);
        JSONObject jBody = new JSONObject();
//        jBody.put("to","/topics/"+topicName);
        jBody.put("body", message);
        jBody.put("title", title);

        JSONObject notificatedService = TopicUtil.getNotificationBody(topicName, jBody);
        logger.info("[ Request to FCM with body ]");
        System.out.println(notificatedService);
        return httpClient.post(FirebaseUtil.FIREBASE_API_URL,notificatedService.toString());

    }

    public Object unsubscribed(String authKey, String token, String topicName) throws  Exception{
        authKey = FirebaseUtil.FIREBASE_SERVER_KEY;
        List<String> users = new ArrayList<>();
        users.add("c7lEKFLhByY:APA91bEl6tzsfSkBxEsrXH8LaCT3MtdRii5zgEPisJ0isAUBnE9x5RoN3k4yAuWi-eRo_-isMdoebmqiWMDpn-S9wwROVAIMFxbwsYOO53hDXRGab86xwqIWBtI-D6CVbWqOFWDXv_kp");
        HttpClient httpClient =  TopicUtil.getGroupTopicHeader(authKey);
        JSONObject jBody = new JSONObject();
        jBody.put("to","/topics/"+topicName);
        jBody.put("registration_tokens",users);
        return httpClient.post(TopicUtil.TOPIC_MULTI_UNSUBSCRIBE_URL,jBody.toString());

    }

    @Transactional(rollbackOn = Exception.class)
    public List<Topic> createByTopicNameAndAppId(String topicName , String appId) throws Exception{
        try{

            List<Topic> existData = topicRepository.findByNameAndApplicationId(topicName,appId);

            List<Topic> topicList = new ArrayList<>();

            List<Device> androidDevices = deviceService.getActiveDeviceByAppIdAndPlatformId(appId, KeyConf.PlatForm.ANDROID);
            List<Device> iosDevices = deviceService.getActiveDeviceByAppIdAndPlatformId(appId, KeyConf.PlatForm.IOS);

            logger.info("Android size : " + androidDevices.size());
            logger.info("IOS size : " + iosDevices.size());

            if((!androidDevices.isEmpty())){
                List<String> token = GlobalMethod.convertDeviceListToTokenList(androidDevices);
                /*
                 * authKey 100% not null
                 * */
                String authKey = appService.getAuthorizedKeyByAppId(appId);

                logger.info("[ Request for FCM Subscribe... ]");
                Object obj = this.subscribeManyUsers(authKey,topicName,token);

                logger.info("[ Response for FCM Subscribe... ]");
                System.out.println(obj);

                Topic fcmTopic =new Topic(topicName,new Application(appId));

                fcmTopic.setFcm();
                fcmTopic.setDevice(androidDevices);

                if(existData.isEmpty()){
                    logger.info("[ Saving FCM Topic Subscriber... ]");
                    topicRepository.save(fcmTopic);
                }else {
                    logger.info("Topic is already saved");
                }

                topicList.add(fcmTopic);
            }

            if((!iosDevices.isEmpty())) {
                Topic apnsTopic = new Topic(topicName, new Application(appId));

                apnsTopic.setDevice(iosDevices);
                apnsTopic.setApns();


                if(existData.isEmpty()){
                    logger.info("[ Saving APNS Topic Subscriber... ]");
                    topicRepository.save(apnsTopic);
                }else{
                    logger.info("Topic is already saved");

                }

                topicList.add(apnsTopic);
            }

            return topicList;

        }catch (Exception e){
            throw e;

        }
    }


    public Object subscribeManyUsers(String authKey, String topicName , List<String> token) throws Exception {
        /*
            Subscribe to topic
        *
        **/
//        authKey = FirebaseUtil.FIREBASE_SERVER_KEY;
//        users = new ArrayList<String>();
//        users.add("c7lEKFLhByY:APA91bEl6tzsfSkBxEsrXH8LaCT3MtdRii5zgEPisJ0isAUBnE9x5RoN3k4yAuWi-eRo_-isMdoebmqiWMDpn-S9wwROVAIMFxbwsYOO53hDXRGab86xwqIWBtI-D6CVbWqOFWDXv_kp");
        HttpClient httpClient =  TopicUtil.getGroupTopicHeader(authKey);
        JSONObject jBody = new JSONObject();
        jBody.put("to","/topics/"+topicName);
        jBody.put("registration_tokens",token);
        httpClient.post(TopicUtil.TOPIC_MULTI_SUBSCRIBE_URL,jBody.toString());
        return httpClient;
    }

    @Override
    public Topic subscribe(String authKey, String token, String topicName) {
        return null;
    }

    @Override
    public Topic unsubscribe(String authKey, String token, String topicName) {
        return null;
    }
    @Override
    public Topic getAllTopics() {
        return null;
    }

    @Override
    public Topic findTopicByName(String topic) {
        return null;
    }


}
