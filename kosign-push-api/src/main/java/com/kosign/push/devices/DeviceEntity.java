package com.kosign.push.devices;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kosign.push.apps.AppEntity;
import com.kosign.push.platforms.PlatformEntity;
import com.kosign.push.users.UserEntity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

// @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Data
@Table(name ="ps_device_client")
@Entity
public class DeviceEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Id
    private Integer id;
   
    private String userId;
    private String deviceId;
    private String token;
    
    private String modelName;
    private String os;

    // @JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
    @ManyToOne(fetch = FetchType.EAGER)
    private AppEntity app;
    // @JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
    @ManyToOne(fetch = FetchType.EAGER)
    private PlatformEntity platform;

    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    private Character status='1';

    public DeviceEntity(String deviceId, String token, AppEntity app, PlatformEntity platform, Character status) {
        this.deviceId = deviceId;
        this.token = token;
        this.app = app;
        this.platform = platform;
        this.status = status;
    }

    public DeviceEntity(){
        
    }

    public DeviceEntity(String deviceId){
        this.deviceId = deviceId; 
    }

    public DeviceEntity(String deviceId, String token, AppEntity app, PlatformEntity platform) {
        this.deviceId = deviceId;
        this.token = token;
        this.app = app;
        this.platform = platform;
    }

	public DeviceEntity(String token, AppEntity application, PlatformEntity platform, String userId) {
        this.userId = userId;
        this.token = token;
        this.app = application;
        this.platform = platform;
	}
}