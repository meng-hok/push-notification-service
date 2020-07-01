package com.kosign.push.testModule;

import com.kosign.push.testModule.requests.DTEST_R01In;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TestService 
{
    @Autowired
    private TestBatisRepository testRepository;

    public Object getAllTest(DTEST_R01In params) 
    {
        return testRepository.findAllTest(params);
    }
}