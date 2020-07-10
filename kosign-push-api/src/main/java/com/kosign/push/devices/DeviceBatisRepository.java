package com.kosign.push.devices;

import java.util.ArrayList;
import java.util.List;

import com.kosign.push.devices.dto.Agent;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;
import com.kosign.push.devices.dto.*;
@Repository
public interface DeviceBatisRepository {
    
    @Select({"<script>","SELECT  d.device_id , d.token,d.app_id,d.platform_id, p_s.authorized_key , p_s.bundle_id , p_s.key_id as file_key, p_s.team_id ,p_s.push_url as pFileName \n" +
        "FROM ps_device_client d INNER JOIN ps_platform_setting p_s \n" +
        "ON d.platform_id = p_s.platform_id AND d.app_id = p_s.application_id AND d.status = p_s.status \n" +
        "WHERE d.device_id IN \n"+
        " <foreach item='item' index='index' collection='deviceIdList' open='(' separator=',' close=')'>#{item}</foreach> \n"+
        "AND app_id = #{appId} AND d.status = '1'","</script>"})
    List<Agent> findByDeviceIdListAndAppIdRaw(@Param("deviceIdList") ArrayList<String> deviceIdList, @Param("appId") String appId);
    
    @Select({"<script>","SELECT  d.device_id , d.token,d.app_id,d.platform_id, p_s.authorized_key , p_s.bundle_id , p_s.key_id as file_key, p_s.team_id ,p_s.push_url as pFileName \n" +
        "FROM ps_device_client d INNER JOIN ps_platform_setting p_s \n" +
        "ON d.platform_id = p_s.platform_id AND d.app_id = p_s.application_id AND d.status = p_s.status \n" +
        "WHERE app_id = #{appId} AND d.status = '1'","</script>"})
    List<Agent> findAllDeviceByAppIdRaw( @Param("appId") String appId);
    
@Select("SELECT  d.device_id , d.token,d.app_id,d.platform_id, p_s.authorized_key , p_s.bundle_id , p_s.key_id as file_key, p_s.team_id ,p_s.push_url as pFileName \n"+
            "FROM ps_device_client d INNER JOIN ps_platform_setting p_s \n"+
            "ON d.platform_id = p_s.platform_id AND d.app_id = p_s.application_id AND d.status = p_s.status \n"+
            "WHERE d.device_id = #{deviceId} AND app_id = #{appId}  AND d.status = '1' LIMIT 1")
    Agent findByDeviceIdAndAppIdRaw(@Param("deviceId")String deviceId, @Param("appId") String appId);
    
    @SelectProvider(method = "getSQL",type = com.kosign.push.devices.DeviceBatisDynasmicSql.class)
    List<ResponseDevice> findAllDevicesClient(@org.apache.ibatis.annotations.Param("app_id") String appId,@org.apache.ibatis.annotations.Param("start_date") String startDate, @org.apache.ibatis.annotations.Param("end_date") String endDate, @org.apache.ibatis.annotations.Param("push_id") String token, @org.apache.ibatis.annotations.Param("model_name") String modelName,
    @org.apache.ibatis.annotations.Param("plat_code") String platform, @org.apache.ibatis.annotations.Param("os_version") String os);
}