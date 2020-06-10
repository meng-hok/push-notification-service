package com.kosign.push.commons;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {

    // BANK_CD
    public static final Pattern BANK_CD_PATTERN = Pattern.compile("[A-Z0-9]{1,20}");

    public static Matcher matchBankCd(String str) {
        return BANK_CD_PATTERN.matcher(str);
    }

    // ACCT_NO
    public static final Pattern ACCT_NO_PATTERN = Pattern.compile("[A-Z0-9]{1,100}");

    public static Matcher matchAcctNo(String str) {
        return ACCT_NO_PATTERN.matcher(str);
    }

}
