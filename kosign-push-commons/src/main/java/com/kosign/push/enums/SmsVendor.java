package com.kosign.push.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Currency;

public enum SmsVendor implements GenericEnum<SmsVendor, String> {
    WEUMS("weums"),
    INFOBIP("infobip"),
    PLASGATE("plasgate"),
    ECHO("echo"),
    ;
    private final String value;

    private SmsVendor(String value) {
        this.value = value;
    }

    @JsonCreator
    public static SmsVendor fromValue(String value) {
        for(SmsVendor my: SmsVendor.values()) {
            if(my.value.equals(value)) {
                return my;
            }
        }

        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public String getLabel() {
        String label = "";

        if("weums".equals(value)) label = "KOSIGN WeUMS (gw)";
        else if("infobip".equals(value)) label = "Infobip (global)";
        else if("plasgate".equals(value)) label = "Plasgate (cambodia local)";
        else if("echo".equals(value)) label = "echo server for testing";

        return label;
    }

    public Currency getCurrency() {
        return Currency.getInstance(getValue());
    }
}
