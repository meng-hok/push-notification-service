/**
 * File Name        	: AppEntity.java
 * File Path        	: /kosign-push-api/src/main/java/com/kosign/push/apps/AppEntity.java
 * File Description 	: 
 * 
 * File Author	  		: Neng Channa
 * Created Date	  	    : 10-July-2020 18:52
 * Developed By	  	    : Sok Menghok
 * Modified Date	  	: 10-July-2020 18:52
 * Modified By          : Sok Menghok
 *
 **/

package com.kosign.push.apps;

import java.sql.Timestamp;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kosign.push.users.UserEntity;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.enums.KeyConfEnum;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
// @Table(name ="ps_application",uniqueConstraints={
// 		@UniqueConstraint(columnNames = {"user_id", "name"})
// })
@Table(name ="ps_application")
@Entity
public class AppEntity 
{
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String id;
    
    @NotEmpty
    public String name;
    
    @JsonProperty("created_by")
    @ManyToOne(optional = true)
    public UserEntity user;
    
    @CreationTimestamp
    public Timestamp createdAt;
    
    @UpdateTimestamp
    public Timestamp updatedAt;
    
    public Character status = KeyConfEnum.Status.ACTIVE;
    
    public String updatedBy;
    
    public AppEntity(){}
    
	public AppEntity(String  id ) 
	{
		this.id = id;
    }
	
    @PrePersist()
    public void onPrepersist() 
    {
        String userId = GlobalMethod.getUserCredential().getId();
        this.updatedBy = userId;
        if(this.user == null )
        {
            this.user = new UserEntity();
        }
        user.setId(userId);
    }
    @PreUpdate
    public void onPreUpdate()
    {
        this.updatedBy = GlobalMethod.getUserCredential().getId();
    }
}
