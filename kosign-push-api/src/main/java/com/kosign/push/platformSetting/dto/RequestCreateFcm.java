package com.kosign.push.platformSetting.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class RequestCreateFcm extends FCMIdentifier {

    @Pattern(regexp = "-?\\d+(\\.\\d+)?")
    @NotEmpty
    public String platformId;
}