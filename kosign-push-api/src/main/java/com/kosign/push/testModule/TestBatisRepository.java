package com.kosign.push.testModule;

import java.util.List;

import com.kosign.push.testModule.dto.DTEST_R01Out;
import com.kosign.push.testModule.dto.DTEST_R01In;

import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

@Repository()
public interface TestBatisRepository
{
    @SelectProvider(method = "getSQL", type = DynamicSQL.class)
    public List<DTEST_R01Out> findAllTest(DTEST_R01In params);
}