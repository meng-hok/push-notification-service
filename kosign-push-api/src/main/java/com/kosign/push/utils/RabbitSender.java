package com.kosign.push.utils;

import com.kosign.push.devices.dto.Agent;
import com.kosign.push.notifications.dto.NotificationSendRequest;
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

	@Value("${pusher.rabbitmq.routingKey.apns-dev}")
	private String apnsDevKey;

	@Value("${base.file.server}")
	private String pfileAlias;
	
	Logger logger = org.slf4j.LoggerFactory.getLogger(RabbitSender.class);

	public void sendNotifcationByAgent (NotificationSendRequest request) {
		System.out.println(request);

		FCM fcm;
		switch (request.getAgent().getPlatform_id())
		{
			case "1":

				final APNS apns = request.toApns();
//				apns.setAppId(appId);
//				apns.setBulkId(bulkId);
				// sendToApns(apns);
				amqpTemplate.convertAndSend(apnsKey, apns);
				logger.info("[ Response Sucess : APNS ]");

				break;

			case "2"  :

//				fcm = request.toFcm();
//				logger.info("[ Response Sucess : FCM ]");
//				amqpTemplate.convertAndSend(fcmKey, fcm);
//				break;
//				fall through 2 -> 3 android and web
			case "3" :
				fcm =  request.toFcm();
				logger.info("[ Response Sucess : FCM ]");
				amqpTemplate.convertAndSend(fcmKey, fcm);
				break;
			case "4":

				final APNS apnsDev = request.toApns();

				amqpTemplate.convertAndSend(apnsDevKey, apnsDev);
				logger.info("[ Response Sucess : APNS Dev]");

				break;
			default :
				logger.info("[ Platform Out of Range ]");
				break;
		}
	}
	
	public void sendNotifcationByAgent (Agent agent,String appId,String title,String message,String bulkId) {
		System.out.println(agent);

		FCM fcm;
		switch (agent.platform_id)
		{
			case "1":

				final APNS apns = new APNS(pfileAlias+"/"+agent.pfilename,agent.teamId, agent.fileKey, agent.bundleId, agent.push_id, title, message);
				apns.setAppId(appId);
				apns.setBulkId(bulkId);
				// sendToApns(apns);
				amqpTemplate.convertAndSend(apnsKey, apns);
				logger.info("[ Response Sucess : APNS ]");

				break;
		
			case "2" :

				fcm = new FCM( agent.authorized_key, agent.push_id,title, message);
				fcm.setAppId(appId);
				fcm.setBulkId(bulkId);
				// sendToFcm(fcm);
				logger.info("[ Response Sucess : FCM ]");
				amqpTemplate.convertAndSend(fcmKey, fcm);
				break;
			case "3" :
				fcm = new FCM( agent.authorized_key, agent.push_id,title, message);
				fcm.setAppId(appId);
				fcm.setBulkId(bulkId);
				// sendToFcm(fcm);
				logger.info("[ Response Sucess : FCM ]");
				amqpTemplate.convertAndSend(fcmKey, fcm);
				break;
			case "4":

				final APNS apnsDev = new APNS(pfileAlias+"/"+agent.pfilename,agent.teamId, agent.fileKey, agent.bundleId, agent.push_id, title, message);
				apnsDev.setAppId(appId);
				apnsDev.setBulkId(bulkId);
				// sendToApns(apns);
				amqpTemplate.convertAndSend(apnsDevKey, apnsDev);
				logger.info("[ Response Sucess : APNS Dev]");

				break;
			default : 
				logger.info("[ Platform Out of Range ]");
				break;
		}
	}
}