package com.kosign.push.devices.dto;

import lombok.Data;

@Data
public class AgentBody extends AgentIdentifier {
    public String title;
    public String message;
}