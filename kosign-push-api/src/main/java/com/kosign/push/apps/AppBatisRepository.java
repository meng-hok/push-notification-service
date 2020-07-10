package com.kosign.push.apps;

import java.util.List;

import com.kosign.push.apps.dto.ResponseListAppById;
import com.kosign.push.apps.dto.ResponseListApp;
import com.kosign.push.platformSetting.dto.*;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

@Repository
public interface AppBatisRepository 
{
    @SelectProvider(method = "getSQL", type = AppBatisDynamicSql.class)
    @Results({
            @Result(property = "id"       , column = "application"),
            @Result(property = "ios"      , column = "to_ios"     ),
            @Result(property = "android"  , column = "to_android" ),
            @Result(property = "web"      , column = "to_web"     ),
            @Result(property = "createdAt", column = "created_at" ),
            @Result(property = "createdBy", column = "user_id"    )
    })
    List<ResponseListApp> findActiveByUserIdAndName(@org.apache.ibatis.annotations.Param("userId")String userId,@org.apache.ibatis.annotations.Param("name")String name);
   
    @Select("SELECT * FROM vw_application_detail WHERE user_id = #{userId} and application = #{appId}")
    @Results({
            @Result(property = "id"       , column = "application"),
            @Result(property = "totalPush", column = "count"      ),
            @Result(property = "createdAt", column = "created_at" )
    })
    List<ResponseListAppById> findActiveByAppId(@org.apache.ibatis.annotations.Param("userId")String userId, @org.apache.ibatis.annotations.Param("appId") String appId);

    @Select("SELECT * FROM vw_platform_detail WHERE application_id  = #{appId}")
    List<ResponsePlatformSetting> findPlatformrByAppId(@org.apache.ibatis.annotations.Param("appId") String appId);
}