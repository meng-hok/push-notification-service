/**
 * File Name        	: AppBatisRepository.java
 * File Path        	: /kosign-push-api/src/main/java/com/kosign/push/apps/AppBatisRepository.java
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

import java.util.List;

import com.kosign.push.apps.dto.ResponseListAppById;
import com.kosign.push.apps.dto.RequestAppList;
import com.kosign.push.apps.dto.ResponseListApp;
import com.kosign.push.platformSetting.dto.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

@Repository()
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
    List<ResponseListApp> findAllApplications(@Param("userId")String userId, @Param("request")RequestAppList request);
   
    @Select("SELECT * FROM vw_application_detail WHERE user_id = #{userId} and application = #{appId}")
    @Results({
            @Result(property = "id"       , column = "application"),
            @Result(property = "totalPush", column = "count"      ),
            @Result(property = "createdAt", column = "created_at" ),
            @Result(property = "createdBy", column = "username"    )
    })
    List<ResponseListAppById> findActiveByAppId(@Param("userId")String userId, @Param("appId") String appId);

    @Select("SELECT * FROM vw_platform_detail WHERE application_id  = #{appId}")
    @Results({
        @Result(property = "auth_key", column = "authorized_key")
    })
    List<ResponsePlatformSetting> findPlatformrByAppId(@Param("appId") String appId);
}