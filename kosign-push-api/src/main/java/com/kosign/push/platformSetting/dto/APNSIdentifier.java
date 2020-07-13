package com.kosign.push.platformSetting.dto;

import com.kosign.push.utils.aspects.ApplicationAspect;

import lombok.Data;

@Data
public class APNSIdentifier extends ApplicationAspect {
    public String p8file;
    public String teamId;
    public String fileKey;

    // public String appId;
    public String bundleId;
}