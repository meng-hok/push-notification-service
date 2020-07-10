package com.kosign.push.platformSetting.dto;

import lombok.Data;

@Data
public class RequestCreateFcm extends FCMIdentifier {


    public String platformId;
}