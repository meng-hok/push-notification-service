package com.kosign.push.testModule;

import java.util.List;

import com.kosign.push.testModule.dto.DTEST_R01Out;
import com.kosign.push.testModule.dto.DHIST_R01In;
import com.kosign.push.testModule.dto.DHIST_R01Out;
import com.kosign.push.testModule.dto.DTEST_R01In;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

@Repository()
public interface TestBatisRepository
{
    @SelectProvider(method = "getSQL", type = DynamicSQL.class)
    public List<DTEST_R01Out> findAllTest(DTEST_R01In params);

    @Select("select * from ps_history")
    public List<DHIST_R01Out> findAllHistory(DHIST_R01In params);
}