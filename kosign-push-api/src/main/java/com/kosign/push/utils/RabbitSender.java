package com.kosign.push.utils;

import com.kosign.push.devices.dto.Agent;
import com.kosign.push.notificationHistory.NotificationHistoryEntity;
import com.kosign.push.platformSetting.dto.APNS;
import com.kosign.push.platformSetting.dto.FCM;

import org.slf4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class RabbitSender {
    
    @Autowired
	private AmqpTemplate amqpTemplate;
	
	@Value("${pusher.rabbitmq.exchange}")
	private String exchange;
	
	@Value("${pusher.rabbitmq.routingKey.fcm}")
	private String fcmKey;	
   
	@Value("${pusher.rabbitmq.routingKey.apns}")
	private String apnsKey;	

    @Value("${pusher.rabbitmq.routingKey.topic}")
	private String topicKey;	
	
	@Value("${pusher.rabbitmq.routingKey.history}")
	private String historyKey;	

	
	Logger logger = org.slf4j.LoggerFactory.getLogger(RabbitSender.class);
	
	// @Autowired
	// private Queue apnsQueue;

	// public void sendToFcm(Object object) {
	// 	logger.info("{ Message to "+fcmKey+" }");
	// 	System.out.println(object);
	// 	amqpTemplate.convertAndSend(fcmKey, object);
	// 	// System.out.println("Send msg = " + new Device(32));
	//     // amqpTemplate.send("pusher.queue.fcm", new Device("32"));
	// }

	public void sendToFcm(FCM object) {
		logger.info("{ Request Message to "+fcmKey+" }");
		System.out.println(object);
		amqpTemplate.convertAndSend(fcmKey, object);
	}

	public void sendToApns(APNS apns){
		logger.info("{ Request Message to "+apnsKey+" }");
		System.out.println(apns);
		amqpTemplate.convertAndSend(apnsKey, apns);
	}

	public void sendToTopic(Object object){
		logger.info("{ Request Message to "+topicKey+" }");
		System.out.println(object);
		amqpTemplate.convertAndSend(topicKey, object);
	}

	public void sendToHistoryQueue(NotificationHistoryEntity history){
		logger.info("{ Request Message to "+historyKey+" }");
		System.out.println(history);
		amqpTemplate.convertAndSend(historyKey,history);
	}

	public void sendNotifcationByAgent (Agent agent,String appId,String title,String message) { 
		FCM fcm;
		switch (agent.platform_id)
		{
			case "1":

				final APNS apns = new APNS(FileStorageUtil.GETP8FILEPATH+agent.pfilename,agent.team_id, agent.file_key, agent.bundle_id, agent.token, title, message);
				apns.setApp_id(appId);
				sendToApns(apns);
				logger.info("[ Response Sucess : APNS ]");

				break;
		
			case "2" :

				fcm = new FCM( agent.authorized_key, agent.token,title, message);
				fcm.setApp_id(appId);
				sendToFcm(fcm);
				logger.info("[ Response Sucess : FCM ]");

				break;
			case "3" :
				fcm = new FCM( agent.authorized_key, agent.token,title, message);
				fcm.setApp_id(appId);
				sendToFcm(fcm);
				logger.info("[ Response Sucess : FCM ]");

				break;
			
			default : 
				logger.info("[ Platform Out of Range ]");
				break;
		}
	}
}