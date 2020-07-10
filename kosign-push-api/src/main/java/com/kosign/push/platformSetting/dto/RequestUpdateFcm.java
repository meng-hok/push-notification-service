package com.kosign.push.platformSetting.dto;

import lombok.Data;

@Data
public class RequestUpdateFcm extends FCMIdentifier{
    public String platformId;
}