package com.wecambodia.cmo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SingleTextualMessageIn extends com.infobip.cmo.SingleTextualMessageIn {

    @JsonProperty("agent")
    private Boolean isAgent;

    // getter/setter (generated)
    public Boolean getAgent() {
        return isAgent;
    }

    public void setAgent(Boolean agent) {
        isAgent = agent;
    }
}