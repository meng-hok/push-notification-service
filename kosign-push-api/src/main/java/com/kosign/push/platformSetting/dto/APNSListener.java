package com.kosign.push.platformSetting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * APNSListener
 */
@ToString
@NoArgsConstructor
 @Getter
 @Setter
public class APNSListener extends APNS {

    private String requestMode;

    public APNSListener( APNS apns, String requestMode) {
        super(apns.getAppId(), apns.getBulkId() ,apns.getP8file() , apns.getTeamId(),  apns.getFileKey(), apns.getBundleId(), apns.getToken(), apns.getTitle(), apns.getMessage(), apns.getImage(), apns.getBadgeCount());
        this.requestMode = requestMode;
    }

}