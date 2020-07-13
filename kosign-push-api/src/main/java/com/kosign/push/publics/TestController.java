package com.kosign.push.publics;

import com.kosign.push.platformSetting.dto.RequestCreateApns;
import com.kosign.push.testModule.TestEntity;
import com.kosign.push.testModule.TestService;
import com.kosign.push.testModule.dto.DHIST_R01In;
import com.kosign.push.testModule.dto.DTEST_C01In;
import com.kosign.push.testModule.dto.DTEST_R01In;
import com.kosign.push.utils.enums.ResponseEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@Api(tags = "Test APIs")
//@RestController
@RequestMapping("/api/publics")
public class TestController {
    @Autowired
    private TestService testService;

    @PostMapping("/test")
    public Object getTest(@RequestBody DTEST_R01In input) {
        return testService.getAllTest(input);
    }

    @PostMapping("/test/create")
    public void createTest(@RequestBody DTEST_C01In input) {
        TestEntity t = testService.createTest(new TestEntity(input));
        System.out.println("#############");
        System.out.println(t.getCode());
        System.out.println("#############");
    }

    @PostMapping("/test/history")
    public Object getHistory(@RequestBody DHIST_R01In input) {

        // Message test = KeyConf.Message.ALREADYREGISTEREDDEVICE;
    //    String me = Message.ALREADYREGISTEREDDEVICE;
    // String me = Key.Message.Response;
        return ResponseEnum.Message.REGISTERED_DEVICE;
       
        // return testService.getAllHistory(input);
    }

    @Transactional(rollbackOn = Exception.class)
    @PostMapping( value = "/uploadtest" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object saveApns(@RequestPart(value = "p8file" ,required = true) MultipartFile p8file ,RequestCreateApns requestCreateApns  ){
        System.out.println(p8file);
        System.out.println(requestCreateApns);
        return "";
    }
    
    @PostMapping("/test/upper")
    public Object getUpper(Upper upper) {

        return upper;
    }


    // @PostMapping("/test/sub")
    // public Object getSub(SubClass sub) {

    //     return sub;
    // }

    // @PostMapping("/test/subson")
    // public Object getSub(ThirdsClass sub) {

    //     return sub;
    // }
}

// class ThirdsClass{ 
//     String me;
// }

// class SubClass extends Upper { 
//     public  String name;
// }