/**
 * File Name        	: AppBatisDynamicSql.java
 * File Path        	: /kosign-push-api/src/main/java/com/kosign/push/apps/AppBatisDynamicSql.java
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

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import com.kosign.push.apps.dto.RequestAppList;

public class AppBatisDynamicSql 
{
    public String getSQL(@Param("userId")String userId, @Param("request") RequestAppList request)
    {
        String sql="SELECT * FROM vw_application_detail WHERE user_id=#{userId}";
        
        if(StringUtils.isNotBlank(request.getName()))
        {
            sql += " and LOWER(name) like '%' || LOWER(#{request.name}) || '%'";
        }

        return sql;
    }
}