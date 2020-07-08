package com.kosign.push.platformSetting.dto;

import lombok.Data;

@Data
public class RequestFcm extends FCMIdentifier {


    public String platformId;
}