package com.kosign.push.platformSetting.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class RequestUpdateFcm extends FCMIdentifier{
    @Pattern(regexp = "-?\\d+(\\.\\d+)?")
    @NotEmpty
    public String platformId;
}