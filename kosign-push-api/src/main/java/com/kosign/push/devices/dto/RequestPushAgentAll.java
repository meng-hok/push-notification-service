package com.kosign.push.devices.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Getter
@Setter
public class RequestPushAgentAll {
    @Size(max  = 100)
    public String title;
    @Size(max  = 255)
    public String message;
    public Integer badgeCount;
    public String image;
}