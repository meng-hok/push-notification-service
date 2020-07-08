package com.kosign.push.platformSetting.dto;

import lombok.Data;

@Data
public class APNSIdentifier {
    public String p8file;
    public String teamId;
    public String fileKey;

    public String appId;
    public String bundleId;
}