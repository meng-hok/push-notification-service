package com.kosign.push.devices.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Getter
@Setter
public class RequestSubscribeDevice {
    @NotEmpty
    public String deviceId;
    @Pattern( message =  "only 1 -> 3" ,regexp = "-?\\d+(\\.\\d+)?")
    @NotEmpty
    public String platformId;
    @NotEmpty
    public String pushId;
}
