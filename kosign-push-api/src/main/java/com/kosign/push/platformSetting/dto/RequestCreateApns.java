package com.kosign.push.platformSetting.dto;

import com.kosign.push.utils.aspects.ApplicationAspect;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class RequestCreateApns extends ApplicationAspect{

    public String teamId;
    public String fileKey;

    // public String appId;
    public String bundleId;
}