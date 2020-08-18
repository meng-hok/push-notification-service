package com.kosign.push.devices.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class RequestSubscribeDevice {
    @NotEmpty
    public String device_id;
    @Pattern( message =  "only 1 -> 3" ,regexp = "-?\\d+(\\.\\d+)?")
    @NotEmpty
    public String platform_id;
    @NotEmpty
    public String push_id;
}
