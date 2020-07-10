package com.kosign.push.apps;

import org.apache.commons.lang3.StringUtils;

public class AppBatisDynamicSql 
{
    public String getSQL(@org.apache.ibatis.annotations.Param("userId")String userId,@org.apache.ibatis.annotations.Param("name")String name)
    {
        String sql="SELECT * FROM vw_application_detail WHERE user_id=#{userId} ";
        
        if(StringUtils.isNotBlank(name))
        {
            sql += " and LOWER(name) like '%' || LOWER(#{name}) || '%'";
        }

        return sql;
    }

}