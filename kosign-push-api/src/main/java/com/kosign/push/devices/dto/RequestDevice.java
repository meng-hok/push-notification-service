package com.kosign.push.devices.dto;

import lombok.Data;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kosign.push.utils.messages.AgentBody;
import com.kosign.push.utils.messages.AgentIdentifier;
import com.kosign.push.utils.messages.AgentRequest;

@Data
public class RequestDevice extends AgentRequest{

    public ArrayList<String> deviceIdList;

    @JsonIgnore
    @Override
    public String getDevice_id() {
        // TODO Auto-generated method stub
        return super.getDevice_id();
    }
}
