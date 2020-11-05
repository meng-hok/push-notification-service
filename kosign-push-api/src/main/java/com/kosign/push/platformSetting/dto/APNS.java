package com.kosign.push.platformSetting.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonProperty;

@AllArgsConstructor
@Data
@ToString
public class APNS extends APNSIdentifier{
    

   
    public String token;
    public String title;
    public String message;
    @JsonProperty("badge_count")
    public Integer badgeCount;
    public String image;
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
   
    public APNS(String appId,String bulkId,String p8file, String teamId, String fileKey, String bundleId, String token,String title, String message,String image,Integer badgeCount) {
        this.setAppId(appId);
        this.bulkId = bulkId;
        this.p8file = p8file;
        this.teamId = teamId;
        this.fileKey = fileKey;
        this.bundleId = bundleId;
        this.token = token;
        this.title = title;
        this.message = message;
        this.image = image;
        this.badgeCount = badgeCount;
    }

    public APNS(){}
}