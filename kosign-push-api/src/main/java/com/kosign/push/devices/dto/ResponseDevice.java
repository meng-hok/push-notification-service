package com.kosign.push.devices.dto;

import java.sql.Timestamp;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Data
public class ResponseDevice {
    private String push_id;
    private String model_name;
    private String plat_code;
    private String os_version;
    @ColumnDefault(value = "now()")
    @CreationTimestamp
    private Timestamp created_at;
}