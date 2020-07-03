package com.kosign.push.testModule;

import com.kosign.push.testModule.dto.DTEST_R01In;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TestService 
{
    @Autowired
    private TestBatisRepository testBatisRepository;

    @Autowired
    private TestRepository testRepository;

    public Object getAllTest(DTEST_R01In params) 
    {
        return testBatisRepository.findAllTest(params);
    }

    public void createTest(TestEntity test)
    {
        testRepository.save(test);
    }
}