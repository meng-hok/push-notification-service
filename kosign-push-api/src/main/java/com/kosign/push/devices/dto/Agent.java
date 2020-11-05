package com.kosign.push.devices.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.micrometer.core.instrument.config.NamingConvention;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;


@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
@Data
@ToString
//@Entity
public class Agent extends AgentBody 
{
    public String authorized_key;
    public String pfilename;
    public String teamId;
    public String fileKey;
    public String bundleId;

    public void setTeam_id(String teamId) {
        this.teamId = teamId;
    }

    public void setFile_key(String fileKey) {
        this.fileKey = fileKey;
    }

    public void setBundle_id(String bundleId) {
        this.bundleId = bundleId;
    }

    public Agent(){}



}