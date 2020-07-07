package com.kosign.push.apps;

import com.kosign.push.utils.messages.ApplicationIdentifier;

import org.apache.commons.lang3.StringUtils;

public class DynamicSQL {
  
    public String getSQL(String userId,String name)
    {
        String sql="SELECT * FROM vw_application_detail WHERE user_id=#{userId} ";
        
        if(StringUtils.isNotBlank(name))
        {
            sql += " and LOWER(name) like '%' || LOWER(#{name}) || '%'";
        }

        return sql;
    }

}