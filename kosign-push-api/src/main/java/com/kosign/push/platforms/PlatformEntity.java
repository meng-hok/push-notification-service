package com.kosign.push.platforms;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kosign.push.utils.KeyConf;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

/**
 * Platform
 */

@Data
@Table(name ="ps_platform")
@Entity
public class PlatformEntity {
 
    @Id
    private String id;
    private String name;
    private String icon;
    private String code;
    @JsonIgnore
    private Character status=KeyConf.Status.ACTIVE;
    @JsonIgnore
    @CreationTimestamp
    private Timestamp registeredAt;
    @JsonIgnore
    @UpdateTimestamp
    private Timestamp updatedAt;
    public PlatformEntity(){
        
    }
    public PlatformEntity(String id) {
        this.id = id;
    }

    public void setToNewPlatform(PlatformEntity platform) {
        this.id = platform.id;
        this.name = platform.name == null ?  this.name : platform.name;
        this.icon = platform.icon == null ?  this.icon : platform.icon;
        this.code = platform.code == null ?  this.code : platform.code;
    }

 

    
}