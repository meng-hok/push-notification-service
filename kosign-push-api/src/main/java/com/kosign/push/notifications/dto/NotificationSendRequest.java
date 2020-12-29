package com.kosign.push.notifications.dto;

import com.kosign.push.devices.dto.Agent;
import com.kosign.push.platformSetting.dto.APNS;
import com.kosign.push.platformSetting.dto.FCM;
import com.kosign.push.utils.FileStorageUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

//from controller to rabbit service

@Getter
@Setter
public class NotificationSendRequest extends CommonRequest {

    private Agent agent;

    public NotificationSendRequest(Agent agent, String appId, String title, String message, String image, Integer badgeCount, String bulkId, Map actionType) {
        super(appId, title, message, badgeCount, bulkId,image,actionType);
        this.agent = agent;
    }

    public NotificationSendRequest(Agent agent, String appId, String title, String message, String image, Integer badgeCount, String bulkId) {
        super(appId, title, message, badgeCount, bulkId,image);
        this.agent = agent;
    }
    public APNS toApns(){
        APNS apns = new APNS(FileStorageUtil.PUTP8FILEPATH +"/"+agent.getPfilename(),agent.getTeamId(), agent.getFileKey(), agent.getBundleId(), agent.getPush_id(),this.getTitle(), this.getMessage(),this.getActionType());
        apns.setAppId(this.getAppId());
        apns.setBadgeCount(this.getBadgeCount());
        apns.setBulkId(this.getBulkId());
        apns.setImage(this.getImage());
        return apns;
    }

    public FCM toFcm(){
        FCM fcm = new FCM( agent.getAuthorized_key(), agent.getPush_id(),this.getTitle(), this.getMessage(),this.getActionType());
        fcm.setAppId(this.getAppId());
        fcm.setBadgeCount(this.getBadgeCount());
        fcm.setBulkId(this.getBulkId());
        fcm.setImage(this.getImage());
        return fcm;
    }

    @Override
    public String toString() {
        System.out.println(super.toString());
        final StringBuilder sb = new StringBuilder("NotificationSendRequest{");
        sb.append("agent=").append(agent);
        sb.append('}');
        return sb.toString();
    }
}
