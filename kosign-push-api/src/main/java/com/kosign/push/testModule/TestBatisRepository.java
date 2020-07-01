package com.kosign.push.testModule;

import java.util.List;

import com.kosign.push.testModule.requests.DTEST_R01In;
import com.kosign.push.testModule.responses.DTEST_R01Out;

import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

@Repository()
public interface TestBatisRepository
{
    @SelectProvider(method = "getSQL", type = DynamicSQL.class)
    public List<DTEST_R01Out> findAllTest(DTEST_R01In params);
}