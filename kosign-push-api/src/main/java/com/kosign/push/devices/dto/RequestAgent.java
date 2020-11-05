package com.kosign.push.devices.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kosign.push.utils.aspects.ApplicationAspect;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class RequestAgent {
    @Size(max = 100)
    public String title;
    @Size(max = 255)
    public String message;
    public Integer badgeCount;
    public String image;
    @NotEmpty
    public String deviceId;
   
}