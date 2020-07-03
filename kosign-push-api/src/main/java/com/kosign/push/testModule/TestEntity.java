package com.kosign.push.testModule;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Data
@Table(name ="ps_test")
@Entity
public class TestEntity 
{
    @GeneratedValue(strategy = GenerationType.AUTO )
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
    public TestEntity(){}
    public TestEntity(String id) 
    {
        this.id = id;
    }
}