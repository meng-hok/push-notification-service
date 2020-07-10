package com.kosign.push.platformSetting.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class RequestCreateApns {

    public String teamId;
    public String fileKey;

    public String appId;
    public String bundleId;
}