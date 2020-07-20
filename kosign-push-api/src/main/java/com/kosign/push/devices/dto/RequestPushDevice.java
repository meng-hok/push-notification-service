package com.kosign.push.devices.dto;

import lombok.Data;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class RequestPushDevice extends RequestAgent{
    @JsonProperty("device_id_list")
    public ArrayList<String> deviceIdList;

    @JsonIgnore
    @Override
    public String getDevice_id() {
        // TODO Auto-generated method stub
        return super.getDevice_id();
    }
}
