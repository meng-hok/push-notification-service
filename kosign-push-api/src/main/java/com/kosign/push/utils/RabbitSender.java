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
	
	
	@Value("${base.file.server}")
	private String pfileAlias;
	
	Logger logger = org.slf4j.LoggerFactory.getLogger(RabbitSender.class);
	
	
	public void sendNotifcationByAgent (Agent agent,String appId,String title,String message) { 
		System.out.println(agent);

		FCM fcm;
		switch (agent.platform_id)
		{
			case "1":

				final APNS apns = new APNS(pfileAlias+"/"+agent.pfilename,agent.team_id, agent.file_key, agent.bundle_id, agent.token, title, message);
				apns.setApp_id(appId);
				// sendToApns(apns);
				amqpTemplate.convertAndSend(apnsKey, apns);
				logger.info("[ Response Sucess : APNS ]");

				break;
		
			case "2" :

				fcm = new FCM( agent.authorized_key, agent.token,title, message);
				fcm.setApp_id(appId);
				// sendToFcm(fcm);
				logger.info("[ Response Sucess : FCM ]");
				amqpTemplate.convertAndSend(fcmKey, fcm);
				break;
			case "3" :
				fcm = new FCM( agent.authorized_key, agent.token,title, message);
				fcm.setApp_id(appId);
				// sendToFcm(fcm);
				logger.info("[ Response Sucess : FCM ]");
				amqpTemplate.convertAndSend(fcmKey, fcm);
				break;
			
			default : 
				logger.info("[ Platform Out of Range ]");
				break;
		}
	}
}