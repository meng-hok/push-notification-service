package com.kosign.push.platformSetting;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kosign.push.apps.AppEntity;
import com.kosign.push.platforms.PlatformEntity;

import com.kosign.push.utils.enums.KeyConfEnum;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;


@Data
@Table(name ="ps_platform_setting")
@Entity
public class PlatformSettingEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    @OneToOne
    private PlatformEntity platform;
    @ManyToOne
    private AppEntity application;
    private String authorizedKey;
    private String keyId;
    private String teamId;
    private String bundleId;
    private String pushUrl;//p8file file path directory
    private Character status= KeyConfEnum.Status.ACTIVE;
    @CreationTimestamp
    private Timestamp registeredAt;
    @UpdateTimestamp
    private Timestamp updatedAt;

    public PlatformSettingEntity(PlatformEntity platform, AppEntity application, String keyId, String teamId, String bundleId,
            String pushUrl) {
        this.platform = platform;
        this.application = application;
        this.keyId = keyId;
        this.teamId = teamId;
        this.bundleId = bundleId;
        this.pushUrl = pushUrl;
    }

    public PlatformSettingEntity(PlatformEntity platform, AppEntity application, String authorizedKey) {
        this.platform = platform;
        this.application = application;
        this.authorizedKey = authorizedKey;
    }
   
    public PlatformSettingEntity(){}

    public void setApnsConfiguration(String keyId, String teamId, String bundleId, String pushUrl) {
        this.keyId = keyId;
        this.teamId = teamId;
        this.bundleId = bundleId;
        this.pushUrl = pushUrl;
    }
}