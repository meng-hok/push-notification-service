package com.kosign.push.apps;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kosign.push.users.UserEntity;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.KeyConf;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
@Table(name ="ps_application",uniqueConstraints={
        @UniqueConstraint(columnNames = {"user_id", "name"})}
)
@Entity
public class AppEntity {
    // @GeneratedValue(strategy = GenerationType.AUTO )
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    public String id;

    public String name;

    @JsonProperty("created_by")
    @ManyToOne(optional = true)
    public UserEntity user;
    
    @CreationTimestamp
    public Timestamp createdAt;
    @UpdateTimestamp
    public Timestamp updatedAt;
    public Character status = KeyConf.Status.ACTIVE;

    public String updatedBy;

    public AppEntity(){}

	public AppEntity(String  id ) {
		this.id = id;
	
    }
    @PrePersist()
    public void onPrepersist() {
        String userId = GlobalMethod.getUserCredential().getId();
        this.updatedBy = userId;
        if(this.user == null ){
            this.user = new UserEntity();
        }
        user.setId(userId);
    }
    @PreUpdate
    public void onPreUpdate() {
        this.updatedBy = GlobalMethod.getUserCredential().getId();
    }
      
    
}
