package com.kosign.push.utils.messages;

import lombok.Data;

@Data
public class AgentBody extends AgentIdentifier {
    public String title;
    public String message;
}