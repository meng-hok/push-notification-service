package com.kosign.push.platformSetting.dto;

import com.kosign.push.utils.aspects.ApplicationAspect;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class RequestRemoveFcm extends ApplicationAspect{
    @Pattern(regexp = "-?\\d+(\\.\\d+)?")
    @NotEmpty
    public String platformId;
}