package com.kosign.push.utils.messages;

import java.sql.Timestamp;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
public class DeviceClientRespose {
    private String push_id;
    private String model_name;
    private String plat_code;
    private String os_version;
    @ColumnDefault(value = "now()")
    @CreationTimestamp
    private Timestamp created_at;
}