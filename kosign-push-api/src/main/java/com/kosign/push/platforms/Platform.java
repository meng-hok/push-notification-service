package com.kosign.push.platforms;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

/**
 * Platform
 */

@Data
@Table(name ="ps_platform")
@Entity
public class Platform {
 
    @Id
    private String id;
    private String name;
    private String icon;
    private String code;
    private Character status;
    @CreationTimestamp
    private Timestamp registeredAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
    public Platform(){
        
    }
    public Platform(String id) {
        this.id = id;
    }

    public void setToNewPlatform(Platform platform) {
        this.id = platform.id;
        this.name = platform.name == null ?  this.name : platform.name;
        this.icon = platform.icon == null ?  this.icon : platform.icon;
        this.code = platform.code == null ?  this.code : platform.code;
    }

 

    
}