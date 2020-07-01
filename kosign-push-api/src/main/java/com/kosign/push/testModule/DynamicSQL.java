package com.kosign.push.testModule;

import com.kosign.push.testModule.requests.DTEST_R01In;

import org.apache.commons.lang3.StringUtils;

public class DynamicSQL 
{
    public String getSQL(DTEST_R01In params)
    {
        String sql="select * from ps_test \nwhere 1=1";
        if(StringUtils.isNotBlank(params.getId()))
        {
            sql += " and id=#{id}";
        }

        if(StringUtils.isNotBlank(params.getName()))
        {
            sql += " and LOWER(name) like '%' || LOWER(#{name}) || '%'";
        }

        return sql;
    }
}