package com.kosign.push.utils.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PlatformSettingRespone {
    private  String plat_id;

    private String plat_nm;

    private String icon;

    private String plat_code;

    private String bundle_id;

    private String key_id;

    private String team_id;

    private String cert_file;

    private String auth_key;

    private String sts; 
}