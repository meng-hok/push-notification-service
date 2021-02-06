package com.kosign.push.utils;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.client.*;
import org.springframework.web.util.*;


public class HttpUtil {

    private static HttpHeaders getHeader() {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
//        header.setBasicAuth("your key");
//        header.setBearerAuth("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJrb3NpZ25raGFwaSIsImV4cCI6MTYxMTMyMzg1OSwiaWF0IjoxNjExMzA1ODU5fQ.aQUX6p8f1YCv-UrTyBn4-_vcYuUGtUfbizp35qYfFpy537w5BGV1C2DfCo_q6V2UxCacWcbqxi2NR_3DDNuZtw");
        return header;
    }

    public static String exchange (UriComponentsBuilder builder ,HttpMethod httpMethod , Object obj){
        HttpEntity<Object> httpEntity = new HttpEntity<>(obj,getHeader());
        return exchange(builder,httpMethod,httpEntity);
    }

    public static String exchange (UriComponentsBuilder builder ,HttpMethod httpMethod , HttpEntity<?> entity){
        ResponseEntity<String>  response = new RestTemplate().exchange(builder.toUriString(),httpMethod, entity,   String.class );
        return response.toString();
    }

    public static void main(String[] args) {
        String response = HttpUtil.exchange(UriComponentsBuilder.fromHttpUrl("https://bnkcmfi.wecambodia.com/api/v1/app/setting?os=IOS"),HttpMethod.GET, null);
        System.out.println(response);
    }
}
