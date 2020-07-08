package com.kosign.push.platformSetting.dto;
import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class APNS extends RequestAPNS{
    

   
    public String token;
    public String title;
    public String message;
    public APNS(String p8file, String teamId, String fileKey, String bundleId, String token,String title, String message) {
        this.p8file = p8file;
        this.teamId = teamId;
        this.fileKey = fileKey;
        this.bundleId = bundleId;
        this.token = token;
        this.title = title;
        this.message = message;
    }

    public APNS(String p8file, String teamId, String fileKey, String bundleId) {
        this.p8file = p8file;
        this.teamId = teamId;
        this.fileKey = fileKey;
        this.bundleId = bundleId;
    }

    public APNS(){}
}