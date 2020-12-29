package com.kosign.push.devices.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kosign.push.notifications.dto.*;
import lombok.Data;

import java.util.*;

import javax.servlet.http.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class RequestPushDevice{
    @Size(max  = 100)
    public String title;
    @Size(max  = 255)
    public String message;
    public Integer badgeCount;
    public String image;
    @NotNull
//    @JsonProperty("device_id_list")
    public ArrayList<String> deviceIdList;

    @JsonProperty("action")
    private Map actionType;


}
