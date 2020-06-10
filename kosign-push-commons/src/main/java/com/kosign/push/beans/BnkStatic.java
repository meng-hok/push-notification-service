package com.kosign.push.beans;

import java.util.TimeZone;

public class BnkStatic {
    public static String PATH_TO_NAS_ROOT = "/WAS_DATA/file/";
    public static String BASE_URL_FOR_PDF = "http://wabooks.dev.content.co.kr/";
    public static String BASE_URL_FOR_WEB = "http://wabooks.dev.content.co.kr/";
    public static String BASE_URL_FOR_MOBILE = "http://greenboxadm.wecambodia.com/";	//TODO: must be replace if public domain is configured

    static {
        //TODO: read from config resource
//        if(JexSystemConfig.get("wabooks", "pathToNasRoot") != null) PATH_TO_NAS_ROOT = JexSystemConfig.get("wabooks", "pathToNasRoot");
//        if(JexSystemConfig.get("wabooks", "baseUrlForPdf") != null) BASE_URL_FOR_PDF = JexSystemConfig.get("wabooks", "baseUrlForPdf");
//        if(JexSystemConfig.get("wabooks", "baseUrlForWeb") != null) BASE_URL_FOR_WEB = JexSystemConfig.get("wabooks", "baseUrlForWeb");
//        if(JexSystemConfig.get("wabooks", "baseUrlForMobile") != null) BASE_URL_FOR_MOBILE = JexSystemConfig.get("wabooks", "baseUrlForMobile");

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Phnom_Penh"));
    }
}
