package com.kosign.push.publics;

import com.kosign.push.testModule.TestEntity;
import com.kosign.push.testModule.TestService;
import com.kosign.push.testModule.dto.DTEST_C01In;
import com.kosign.push.testModule.dto.DTEST_R01In;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(tags = "Test APIs")
@RestController
@RequestMapping("/api/public")
public class TestController 
{
    @Autowired
    private TestService testService;

    @PostMapping("/test")
    public Object getTest(@RequestBody DTEST_R01In input)  
    {
        return testService.getAllTest(input);
    }

    @PostMapping("/test/create")
    public void createTest(@RequestBody DTEST_C01In input)
    {
        TestEntity test = new TestEntity();
        test.setName  ( input.getName()  );
        test.setIcon  ( input.getIcon()  );
        test.setCode  ( input.getCode()  );
        test.setStatus( input.getStatus());

        testService.createTest(test);
    }
}