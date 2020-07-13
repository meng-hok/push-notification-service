package com.kosign.push.topics;

import com.kosign.push.apps.AppService;
import com.kosign.push.apps.AppEntity;
import com.kosign.push.devices.DeviceEntity;
import com.kosign.push.devices.DeviceService;
import com.kosign.push.platformSetting.dto.APNS;
import com.kosign.push.platformSetting.PlatformSettingEntity;
import com.kosign.push.platformSetting.PlatformSettingService;
import com.kosign.push.utils.FileStorageUtil;
import com.kosign.push.utils.FirebaseUtil;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.HttpClient;
import com.kosign.push.utils.enums.KeyConfEnum;
import com.kosign.push.utils.RabbitSender;
import com.kosign.push.utils.TopicUtil;
import com.kosign.push.utils.enums.AgentEnum;
import com.kosign.push.utils.enums.PlatformEnum;

import org.json.JSONObject;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

//@AllArgsConstructor
@Service
public class TopicService {

    private TopicRepository topicRepository;

    private DeviceService deviceService;

    private AppService appService;

    private PlatformSettingService platformSettingService;

    @Autowired
    private RabbitSender rabbitSender;

    public TopicService(TopicRepository topicRepository, DeviceService deviceService, AppService appService,PlatformSettingService platformSettingService) {
        this.topicRepository = topicRepository;
        this.deviceService = deviceService;
        this.appService = appService;
        this.platformSettingService = platformSettingService;
    }

    Logger logger = LoggerFactory.getLogger(TopicService.class);

//    public Logger myLogger() {
//
//        return LoggerFactory.getLogger(TopicService.class);
//    }
//

    public TopicEntity  getTopicDetailByAppIdAndTopicName(String appId, String topicName) {
        return topicRepository.findAllByApplicationIdAndNameAndStatus(appId,topicName, KeyConfEnum.Status.ACTIVE);
    }
    public List<TopicEntity> getActiveTopicsByAppId(String AppId){
       return topicRepository.findAllByApplicationIdAndStatus(AppId, KeyConfEnum.Status.ACTIVE);
    }


    public Object registerTopic(String appId,String topicName) throws PSQLException {
        TopicEntity topic =new TopicEntity();
        topic.setApplication(new AppEntity(appId));
        topic.setName(topicName);
        return topicRepository.save(topic);
    }

    @Transactional(rollbackOn = Exception.class)
    public TopicEntity subscribeUserToTopic(String appId,String topicName,List<DeviceEntity> devices){
        TopicEntity topic = topicRepository.findAllByApplicationIdAndNameAndStatus(appId,topicName, KeyConfEnum.Status.ACTIVE);
        if(topic == null ){
            return null;
        }
        topic.setDevice(devices);
        topicRepository.save(topic);

        return topic;
    }


    @Transactional(rollbackOn = Exception.class)
    public Object unsubscribeUserFromTopic(String appId, String topicName, ArrayList<DeviceEntity> devices) {
        TopicEntity topic = topicRepository.findAllByApplicationIdAndNameAndStatus(appId,topicName, KeyConfEnum.Status.ACTIVE);
//        List<TopicDevice> topicDevices = getTopicDetailByAppIdAndTopicName();
        return null;
    }


    /*
     * Statuc work
     * */
    public Object unsubscribed(String authKey, String token, String topicName) throws  Exception{
        authKey = FirebaseUtil.FIREBASE_SERVER_KEY;
        List<String> users = new ArrayList<>();
//        users.add("c7lEKFLhByY:APA91bEl6tzsfSkBxEsrXH8LaCT3MtdRii5zgEPisJ0isAUBnE9x5RoN3k4yAuWi-eRo_-isMdoebmqiWMDpn-S9wwROVAIMFxbwsYOO53hDXRGab86xwqIWBtI-D6CVbWqOFWDXv_kp");
        HttpClient httpClient =  TopicUtil.getGroupTopicHeader(authKey);
        JSONObject jBody = new JSONObject();
        jBody.put("to","/topics/"+topicName);
        jBody.put("registration_tokens",users);
        return httpClient.post(TopicUtil.TOPIC_MULTI_UNSUBSCRIBE_URL,jBody.toString());

    }
    /*
     * Dynasmic work
     * */
    @Transactional(rollbackOn = Exception.class)
    public List<TopicEntity> createByTopicNameAndAppId(String topicName , String appId) throws Exception{
        try{

            List<TopicEntity> existData = topicRepository.findByNameAndApplicationId(topicName,appId);

            List<TopicEntity> topicList = new ArrayList<>();

            List<DeviceEntity> androidDevices = deviceService.getActiveDeviceByAppIdAndPlatformId(appId, PlatformEnum.Platform.ANDROID);
            List<DeviceEntity> iosDevices = deviceService.getActiveDeviceByAppIdAndPlatformId(appId, PlatformEnum.Platform.IOS);

            logger.info("Android size : " + androidDevices.size());
            logger.info("IOS size : " + iosDevices.size());

            if((!androidDevices.isEmpty())){
                List<String> token = GlobalMethod.convertDeviceListToTokenList(androidDevices);
                /*
                 * authKey 100% not null
                 * */
                String authKey = appService.getAuthorizedKeyByAppId(appId);

                logger.info("[ Request for FCM Subscribe... ]");
                Object obj = TopicUtil.subscribeManyUsers(authKey,topicName,token);

                logger.info("[ Response for FCM Subscribe... ]");
                System.out.println(obj);

                TopicEntity fcmTopic =new TopicEntity(topicName,new AppEntity(appId));

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
                TopicEntity apnsTopic = new TopicEntity(topicName, new AppEntity(appId));

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

    /*
    * MAIN method
    *  1. call sendToFCM
    *  2. call sentToApns
    * */
    public JSONObject sendTo(String appId, String topicName, String title, String message) throws Exception {
        sendToFCM(appId, topicName, title, message);
        sentToApns(appId, topicName, title, message);
        return null;
    }

    public Object sentToApns(String appId, String topicName, String title, String message){
        PlatformSettingEntity platformSetting = platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,PlatformEnum.Platform.IOS);
        /* Android including **/
       TopicEntity topic = topicRepository.findByNameAndApplicationIdAndAgentAndStatus(topicName,appId,AgentEnum.Agent.APNS, KeyConfEnum.Status.ACTIVE);
       List<DeviceEntity> devices =  topic.getDevice();

       devices.forEach(device -> {
           //otificationService.sendNotificationToIOS(PlatformEnum.Platform.GETP8FILEPATH+agent.pfilename,agent.team_id, agent.file_key, agent.bundle_id, agent.token, title, message);
            //(String p8file, String teamId, String fileKey, String bundleId, String token,String title, String message) {
           rabbitSender.sendToApns(new APNS(FileStorageUtil.GETP8FILEPATH+platformSetting.getPushUrl(),platformSetting.getTeamId(), platformSetting.getKeyId(), platformSetting.getBundleId(), device.getToken(), title, message));
       });

       return topic;
    }

    public Object sendToFCM(String appId, String topicName, String title, String message)throws Exception {
        String authKey = platformSettingService.getFcmAuthorizedKeyByAppId(appId);

        HttpClient httpClient =  TopicUtil.getGroupTopicHeader(authKey);
        JSONObject jBody = new JSONObject();

        jBody.put("body", message);
        jBody.put("title", title);

        JSONObject notificatedService = TopicUtil.getNotificationBody(topicName, jBody);
        logger.info("[ Request to FCM with body ]");
        System.out.println(notificatedService);
        return httpClient.post(FirebaseUtil.FIREBASE_API_URL,notificatedService.toString());
    }


}
