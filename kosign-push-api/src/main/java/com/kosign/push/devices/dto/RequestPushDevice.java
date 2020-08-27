package com.kosign.push.devices.dto;

import lombok.Data;

import java.util.ArrayList;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class RequestPushDevice{

    public String title;
    public String message;
    
    @NotNull
    @JsonProperty("device_id_list")
    public ArrayList<String> deviceIdList;
}
