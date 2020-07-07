package com.kosign.push.apps;

import java.util.List;

import com.kosign.push.utils.messages.ApplicationResponse;
import com.kosign.push.utils.messages.ApplicationResponseById;
import com.kosign.push.utils.messages.PlatformSettingRespone;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

@Repository
public interface AppMybatisRepository {
    @SelectProvider(method = "getSQL", type = com.kosign.push.apps.DynamicSQL.class)
    @Results({
            @Result(property = "id", column = "application"),
            @Result(property = "ios", column = "to_ios"),
            @Result(property = "android", column = "to_android"),
            @Result(property = "web", column = "to_web"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "createdBy", column = "user_id")
    })
    List<ApplicationResponse> findActiveByUserIdAndName(@org.apache.ibatis.annotations.Param("userId")String userId,@org.apache.ibatis.annotations.Param("name")String name);
   
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