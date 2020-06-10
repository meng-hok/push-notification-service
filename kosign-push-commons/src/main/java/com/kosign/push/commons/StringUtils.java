package com.kosign.push.commons;

import com.kosign.push.logging.AppLogManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static void main(String[] args) {
//        String userId = "abcdef";
//        System.out.println(StringUtils.overlay(userId, "**", userId.length() - 2, userId.length()));

        String test = "fnsh_0011_003";
        String pattern = "([a-zA-Z]+\\_[0-9]+\\_[0-9]+)\\_(.*)";

        Matcher matcher = Pattern.compile(pattern).matcher(test);

        if(matcher.find()) {
            AppLogManager.debug(matcher.group(1));
        }
    }

}
