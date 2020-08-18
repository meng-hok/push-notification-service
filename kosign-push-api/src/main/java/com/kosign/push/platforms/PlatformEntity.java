/**
 * File Name        	: PlatformEntity.java
 * File Path        	: /kosign-push-api/src/main/java/com/kosign/push/platforms/PlatformEntity.java
 * File Description 	: 
 * 
 * File Author	  		: Neng Channa
 * Created Date	  	    : 13-July-2020 13:40
 * Developed By	  	    : Sok Menghok
 * Modified Date	  	: 13-July-2020 16:29
 * Modified By          : Sok Menghok
 *
 **/

package com.kosign.push.platforms;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kosign.push.utils.enums.KeyConfEnum;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;

@Data
@Table(name ="ps_platform")
@Entity
public class PlatformEntity 
{
    @NotEmpty
    @Id
    private String id;
    @NotEmpty
    private String name;
    private String icon;
    private String code;
    @JsonIgnore
    private Character status= KeyConfEnum.Status.ACTIVE;
    @JsonIgnore
    @CreationTimestamp
    private Timestamp registeredAt;
    @JsonIgnore
    @UpdateTimestamp
    private Timestamp updatedAt;
    public PlatformEntity(){}
    public PlatformEntity(String id) 
    {
        this.id = id;
    }
    public void setToNewPlatform(PlatformEntity platform) 
    {
        this.id = platform.id;
        this.name = platform.name == null ?  this.name : platform.name;
        this.icon = platform.icon == null ?  this.icon : platform.icon;
        this.code = platform.code == null ?  this.code : platform.code;
    }
}