package com.kosign.push.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import javax.net.ssl.SSLException;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;

/**
 * Document : https://pushy-apns.org/
 */
public class APNsUtil {

    public static final String  APNS_DEV_MODE= "APNS_DEV_MODE";

    public static final String  APNS_PROD_MODE= "APNS_PROD_MODE";
    
    public static Logger logger = org.slf4j.LoggerFactory.getLogger(APNsUtil.class);

    public static SimpleApnsPushNotification getSimpleApns(String userToken,String topic, ApnsPayloadBuilder payloadBuilder ) {
      
        final String payload = payloadBuilder.build();
        final String token = TokenUtil.sanitizeTokenString(userToken);

        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token,topic, payload);
        return pushNotification;
    }

    public static SimpleApnsPushNotification getSimpleApnsWithPayAsString(String userToken,String topic,String payload ) {
      
        final String token = TokenUtil.sanitizeTokenString(userToken);

        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token,topic, payload);
        return pushNotification;
    }

    public static ApnsClient getApnsCredentials(String pFileDirectory, String teamId, String keyString , String requestType)
            throws InvalidKeyException, SSLException, NoSuchAlgorithmException, IOException {

            ApnsClient apnsClient = null;
            File p8file = new File(pFileDirectory);

            logger.info("################################### DEBUG ################################################# ");
            logger.info("APNS REQUEST MODE : " + requestType);
            logger.info("P8 file directory ");
            logger.info("FILE PATH :" + pFileDirectory);
            logger.info("FILE IS EXIST :" + p8file.exists());
            logger.info("############################################################################################ ");

            switch (requestType) {
                case APNS_DEV_MODE:
                        apnsClient = new ApnsClientBuilder()
                        .setApnsServer(ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                        .setSigningKey(ApnsSigningKey.loadFromPkcs8File(p8file,teamId,keyString))
                        .build();
                    break;
                case APNS_PROD_MODE:
                        apnsClient = new ApnsClientBuilder()
                        .setApnsServer(ApnsClientBuilder.PRODUCTION_APNS_HOST)
                        .setSigningKey(ApnsSigningKey.loadFromPkcs8File(p8file,teamId,keyString))
                        .build();
                break;
                default:
                    System.out.println("APNS DEFAULT CHECK RABBIT LISTENER");
                    break;
            }
      
        return apnsClient;
    }

   
}