package com.kosign.push.mybatis;

import com.kosign.push.apps.Application;
import com.kosign.push.history.NotificationHistory;
import com.kosign.push.utils.messages.Agent;
import com.kosign.push.users.User;
import com.kosign.push.utils.messages.ApplicationResponse;
import com.kosign.push.utils.messages.ApplicationResponseById;
import com.kosign.push.utils.messages.PlatformSettingRespone;

import org.apache.ibatis.annotations.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface MyBatisRepository {

//    @Select("")

    @Select("SELECT  d.device_id , d.token,d.app_id,d.platform_id, p_s.authorized_key , p_s.bundle_id , p_s.key_id as file_key, p_s.team_id ,p_s.push_url as pFileName \n"+
            "FROM ps_device_client d INNER JOIN ps_platform_setting p_s \n"+
            "ON d.platform_id = p_s.id \n"+
            "WHERE d.device_id = #{deviceId} AND app_id = #{appId} AND d.status = '1' LIMIT 1")
    Agent findByDeviceIdAndAppIdRaw(@Param("deviceId")String deviceId, @Param("appId") String appId);

    @Select({"<script>","SELECT  d.device_id , d.token,d.app_id,d.platform_id, p_s.authorized_key , p_s.bundle_id , p_s.key_id as file_key, p_s.team_id ,p_s.push_url as pFileName \n" +
            "FROM ps_device_client d INNER JOIN ps_platform_setting p_s \n" +
            "ON d.platform_id = p_s.platform_id \n" +
            "WHERE d.device_id IN \n"+
            " <foreach item='item' index='index' collection='deviceIdList' open='(' separator=',' close=')'>#{item}</foreach> \n"+
            "AND app_id = #{appId} AND d.status = '1'","</script>"})
   List<Agent> findByDeviceIdListAndAppIdRaw(@Param("deviceIdList") ArrayList<String> deviceIdList, @Param("appId") String appId);


    //history.getId().toString(),history.getMessage(),history.getRecieverId(),history.getTitle(),history.getAppId(),history.getStatus(),history.getToPlatform(),history.getResponseMsg()

    @Insert("INSERT INTO public.ps_history \n" +
            "(message, reciever_id, title, app_id, status, to_platform, response_msg) VALUES  \n"+
            "(#{history.message},#{history.recieverId},#{history.title},#{history.appId},#{history.status},#{history.toPlatform},#{history.responseMsg})")
    Integer insertHistory(@org.apache.ibatis.annotations.Param("history")  NotificationHistory history);

    @Select("SELECT * FROM vw_application_detail WHERE user_id = #{userId}")
    @Results({
            @Result(property = "id", column = "application"),
            @Result(property = "totalPush", column = "count"),
            @Result(property = "createdAt", column = "created_at")
    })
    List<ApplicationResponse> findActiveByUserId(@org.apache.ibatis.annotations.Param("userId")String userId);

    @Select("SELECT * FROM vw_application_detail WHERE user_id = #{userId} and application = #{appId}")
    @Results({
            @Result(property = "id", column = "application"),
            @Result(property = "totalPush", column = "count"),
            @Result(property = "createdAt", column = "created_at")
    })
    List<ApplicationResponseById> findActiveByAppId(@org.apache.ibatis.annotations.Param("userId")String userId, @org.apache.ibatis.annotations.Param("appId") String appId);

    @Select("SELECT * FROM vw_platform_detail WHERE application_id  = #{appId}")
    List<PlatformSettingRespone> findPlatformrByAppId( @org.apache.ibatis.annotations.Param("appId") String appId);
}
