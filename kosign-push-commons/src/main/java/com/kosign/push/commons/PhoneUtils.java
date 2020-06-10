package com.kosign.push.commons;

import com.kosign.push.logging.AppLogManager;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import java.util.HashMap;
import java.util.Map;

public class PhoneUtils {

    final public static String DEFAULT_COUNTRY_CODE_NUMERIC= "855";
    final public static String DEFAULT_COUNTRY_CODE_ALPHA2 = "KH";

    public static Map<String, String> countryCodeMap;

    static {
        countryCodeMap = new HashMap<>();

        countryCodeMap.put("93"		, "AF");
        countryCodeMap.put("355"	, "AL");
        countryCodeMap.put("213"	, "DZ");
        countryCodeMap.put("1-684"	, "AS");
        countryCodeMap.put("376"	, "AD");
        countryCodeMap.put("244"	, "AO");
        countryCodeMap.put("1-264"	, "AI");
        countryCodeMap.put("672"	, "AQ");
        countryCodeMap.put("1-268"	, "AG");
        countryCodeMap.put("54"		, "AR");
        countryCodeMap.put("374"	, "AM");
        countryCodeMap.put("297"	, "AW");
        countryCodeMap.put("61"		, "AU");
        countryCodeMap.put("43"		, "AT");
        countryCodeMap.put("994"	, "AZ");
        countryCodeMap.put("1-242"	, "BS");
        countryCodeMap.put("973"	, "BH");
        countryCodeMap.put("880"	, "BD");
        countryCodeMap.put("1-246"	, "BB");
        countryCodeMap.put("375"	, "BY");
        countryCodeMap.put("32"		, "BE");
        countryCodeMap.put("501"	, "BZ");
        countryCodeMap.put("229"	, "BJ");
        countryCodeMap.put("1-441"	, "BM");
        countryCodeMap.put("975"	, "BT");
        countryCodeMap.put("591"	, "BO");
        countryCodeMap.put("387"	, "BA");
        countryCodeMap.put("267"	, "BW");
        countryCodeMap.put("55"		, "BR");
        countryCodeMap.put("246"	, "IO");
        countryCodeMap.put("1-284"	, "VG");
        countryCodeMap.put("673"	, "BN");
        countryCodeMap.put("359"	, "BG");
        countryCodeMap.put("226"	, "BF");
        countryCodeMap.put("257"	, "BI");
        countryCodeMap.put("855"	, "KH");
        countryCodeMap.put("237"	, "CM");
        countryCodeMap.put("1"		, "CA");
        countryCodeMap.put("238"	, "CV");
        countryCodeMap.put("1-345"	, "KY");
        countryCodeMap.put("236"	, "CF");
        countryCodeMap.put("235"	, "TD");
        countryCodeMap.put("56"		, "CL");
        countryCodeMap.put("86"		, "CN");
        countryCodeMap.put("61"		, "CX");
        countryCodeMap.put("61"		, "CC");
        countryCodeMap.put("57"		, "CO");
        countryCodeMap.put("269"	, "KM");
        countryCodeMap.put("682"	, "CK");
        countryCodeMap.put("506"	, "CR");
        countryCodeMap.put("385"	, "HR");
        countryCodeMap.put("53"		, "CU");
        countryCodeMap.put("599"	, "CW");
        countryCodeMap.put("357"	, "CY");
        countryCodeMap.put("420"	, "CZ");
        countryCodeMap.put("243"	, "CD");
        countryCodeMap.put("45"		, "DK");
        countryCodeMap.put("253"	, "DJ");
        countryCodeMap.put("1-767"	, "DM");
        countryCodeMap.put("1-809"	, "DO");
        countryCodeMap.put("1-829"	, "DO");
        countryCodeMap.put("1-849"	, "DO");
        countryCodeMap.put("670"	, "TL");
        countryCodeMap.put("593"	, "EC");
        countryCodeMap.put("20"		, "EG");
        countryCodeMap.put("503"	, "SV");
        countryCodeMap.put("240"	, "GQ");
        countryCodeMap.put("291"	, "ER");
        countryCodeMap.put("372"	, "EE");
        countryCodeMap.put("251"	, "ET");
        countryCodeMap.put("500"	, "FK");
        countryCodeMap.put("298"	, "FO");
        countryCodeMap.put("679"	, "FJ");
        countryCodeMap.put("358"	, "FI");
        countryCodeMap.put("33"		, "FR");
        countryCodeMap.put("689"	, "PF");
        countryCodeMap.put("241"	, "GA");
        countryCodeMap.put("220"	, "GM");
        countryCodeMap.put("995"	, "GE");
        countryCodeMap.put("49"		, "DE");
        countryCodeMap.put("233"	, "GH");
        countryCodeMap.put("350"	, "GI");
        countryCodeMap.put("30"		, "GR");
        countryCodeMap.put("299"	, "GL");
        countryCodeMap.put("1-473"	, "GD");
        countryCodeMap.put("1-671"	, "GU");
        countryCodeMap.put("502"	, "GT");
        countryCodeMap.put("44-1481"	, "GG");
        countryCodeMap.put("224"	, "GN");
        countryCodeMap.put("245"	, "GW");
        countryCodeMap.put("592"	, "GY");
        countryCodeMap.put("509"	, "HT");
        countryCodeMap.put("504"	, "HN");
        countryCodeMap.put("852"	, "HK");
        countryCodeMap.put("36"		, "HU");
        countryCodeMap.put("354"	, "IS");
        countryCodeMap.put("91"		, "IN");
        countryCodeMap.put("62"		, "ID");
        countryCodeMap.put("98"		, "IR");
        countryCodeMap.put("964"	, "IQ");
        countryCodeMap.put("353"	, "IE");
        countryCodeMap.put("44-1624"	, "IM");
        countryCodeMap.put("972"	, "IL");
        countryCodeMap.put("39"		, "IT");
        countryCodeMap.put("225"	, "CI");
        countryCodeMap.put("1-876"	, "JM");
        countryCodeMap.put("81"		, "JP");
        countryCodeMap.put("44-1534"	, "JE");
        countryCodeMap.put("962"	, "JO");
        countryCodeMap.put("7"		, "KZ");
        countryCodeMap.put("254"	, "KE");
        countryCodeMap.put("686"	, "KI");
        countryCodeMap.put("383"	, "XK");
        countryCodeMap.put("965"	, "KW");
        countryCodeMap.put("996"	, "KG");
        countryCodeMap.put("856"	, "LA");
        countryCodeMap.put("371"	, "LV");
        countryCodeMap.put("961"	, "LB");
        countryCodeMap.put("266"	, "LS");
        countryCodeMap.put("231"	, "LR");
        countryCodeMap.put("218"	, "LY");
        countryCodeMap.put("423"	, "LI");
        countryCodeMap.put("370"	, "LT");
        countryCodeMap.put("352"	, "LU");
        countryCodeMap.put("853"	, "MO");
        countryCodeMap.put("389"	, "MK");
        countryCodeMap.put("261"	, "MG");
        countryCodeMap.put("265"	, "MW");
        countryCodeMap.put("60"		, "MY");
        countryCodeMap.put("960"	, "MV");
        countryCodeMap.put("223"	, "ML");
        countryCodeMap.put("356"	, "MT");
        countryCodeMap.put("692"	, "MH");
        countryCodeMap.put("222"	, "MR");
        countryCodeMap.put("230"	, "MU");
        countryCodeMap.put("262"	, "YT");
        countryCodeMap.put("52"		, "MX");
        countryCodeMap.put("691"	, "FM");
        countryCodeMap.put("373"	, "MD");
        countryCodeMap.put("377"	, "MC");
        countryCodeMap.put("976"	, "MN");
        countryCodeMap.put("382"	, "ME");
        countryCodeMap.put("1-664"	, "MS");
        countryCodeMap.put("212"	, "MA");
        countryCodeMap.put("258"	, "MZ");
        countryCodeMap.put("95"		, "MM");
        countryCodeMap.put("264"	, "NA");
        countryCodeMap.put("674"	, "NR");
        countryCodeMap.put("977"	, "NP");
        countryCodeMap.put("31"		, "NL");
        countryCodeMap.put("599"	, "AN");
        countryCodeMap.put("687"	, "NC");
        countryCodeMap.put("64"		, "NZ");
        countryCodeMap.put("505"	, "NI");
        countryCodeMap.put("227"	, "NE");
        countryCodeMap.put("234"	, "NG");
        countryCodeMap.put("683"	, "NU");
        countryCodeMap.put("850"	, "KP");
        countryCodeMap.put("1-670"	, "MP");
        countryCodeMap.put("47"		, "NO");
        countryCodeMap.put("968"	, "OM");
        countryCodeMap.put("92"		, "PK");
        countryCodeMap.put("680"	, "PW");
        countryCodeMap.put("970"	, "PS");
        countryCodeMap.put("507"	, "PA");
        countryCodeMap.put("675"	, "PG");
        countryCodeMap.put("595"	, "PY");
        countryCodeMap.put("51"		, "PE");
        countryCodeMap.put("63"		, "PH");
        countryCodeMap.put("64"		, "PN");
        countryCodeMap.put("48"		, "PL");
        countryCodeMap.put("351"	, "PT");
        countryCodeMap.put("1-787" 	, "PR");
        countryCodeMap.put("1-939"	, "PR");
        countryCodeMap.put("974"	, "QA");
        countryCodeMap.put("242"	, "CG");
        countryCodeMap.put("262"	, "RE");
        countryCodeMap.put("40"		, "RO");
        countryCodeMap.put("7"		, "RU");
        countryCodeMap.put("250"	, "RW");
        countryCodeMap.put("590"	, "BL");
        countryCodeMap.put("290"	, "SH");
        countryCodeMap.put("1-869"	, "KN");
        countryCodeMap.put("1-758"	, "LC");
        countryCodeMap.put("590"	, "MF");
        countryCodeMap.put("508"	, "PM");
        countryCodeMap.put("1-784"	, "VC");
        countryCodeMap.put("685"	, "WS");
        countryCodeMap.put("378"	, "SM");
        countryCodeMap.put("239"	, "ST");
        countryCodeMap.put("966"	, "SA");
        countryCodeMap.put("221"	, "SN");
        countryCodeMap.put("381"	, "RS");
        countryCodeMap.put("248"	, "SC");
        countryCodeMap.put("232"	, "SL");
        countryCodeMap.put("65"		, "SG");
        countryCodeMap.put("1-721"	, "SX");
        countryCodeMap.put("421"	, "SK");
        countryCodeMap.put("386"	, "SI");
        countryCodeMap.put("677"	, "SB");
        countryCodeMap.put("252"	, "SO");
        countryCodeMap.put("27"		, "ZA");
        countryCodeMap.put("82"		, "KR");
        countryCodeMap.put("211"	, "SS");
        countryCodeMap.put("34"		, "ES");
        countryCodeMap.put("94"		, "LK");
        countryCodeMap.put("249"	, "SD");
        countryCodeMap.put("597"	, "SR");
        countryCodeMap.put("47"		, "SJ");
        countryCodeMap.put("268"	, "SZ");
        countryCodeMap.put("46"		, "SE");
        countryCodeMap.put("41"		, "CH");
        countryCodeMap.put("963"	, "SY");
        countryCodeMap.put("886"	, "TW");
        countryCodeMap.put("992"	, "TJ");
        countryCodeMap.put("255"	, "TZ");
        countryCodeMap.put("66"		, "TH");
        countryCodeMap.put("228"	, "TG");
        countryCodeMap.put("690"	, "TK");
        countryCodeMap.put("676"	, "TO");
        countryCodeMap.put("1-868"	, "TT");
        countryCodeMap.put("216"	, "TN");
        countryCodeMap.put("90"		, "TR");
        countryCodeMap.put("993"	, "TM");
        countryCodeMap.put("1-649"	, "TC");
        countryCodeMap.put("688"	, "TV");
        countryCodeMap.put("1-340"	, "VI");
        countryCodeMap.put("256"	, "UG");
        countryCodeMap.put("380"	, "UA");
        countryCodeMap.put("971"	, "AE");
        countryCodeMap.put("44"		, "GB");
        countryCodeMap.put("1"		, "US");
        countryCodeMap.put("598"	, "UY");
        countryCodeMap.put("998"	, "UZ");
        countryCodeMap.put("678"	, "VU");
        countryCodeMap.put("379"	, "VA");
        countryCodeMap.put("58"		, "VE");
        countryCodeMap.put("84"		, "VN");
        countryCodeMap.put("681"	, "WF");
        countryCodeMap.put("212"	, "EH");
        countryCodeMap.put("967"	, "YE");
        countryCodeMap.put("260"	, "ZM");
        countryCodeMap.put("263"	, "ZW");

    }

    // https://www.programcreek.com/java-api-examples/index.php?api=com.google.i18n.phonenumbers.PhoneNumberUtil
    public static PhoneNumber parseNumber(String str, String countryCode) {
        try {
//            str = normalizeNumber(str, countryCode);
//            AppLogManager.debug(str);
//
            return PhoneNumberUtil.getInstance().parse(str, countryCode);
        } catch (NumberParseException e) {
            AppLogManager.error(e);
        }

        return null;
        /*
        if (StringUtils.isBlank(str)) {
            return null;
        }

        PhoneNumberUtil numberUtil = PhoneNumberUtil.getInstance();
        PhoneNumber result;
        try {
            result = numberUtil.parse(str, countryCode);
            if (result == null) {
                return null;
            }
        } catch (NumberParseException localNumberParseException) {
            return null;
        }
        return result;
        */
    }

    public static PhoneNumber parseNumber(String str) {
        return parseNumber(str, "");
    }

    /*
    public static String normalizeNumber(String number) {
        try {
            PhoneNumber phoneNum = parseNumber(number);

            if(phoneNum == null) {
                phoneNum = parseNumber(number, "");
            }

//			String countryCode = StringUtils.defaultIfBlank(PhoneNumberUtil.getInstance().getRegionCodeForNumber(phoneNum), DEFAULT_COUNTRY_CODE_ALPHA2);

            if(phoneNum != null) {
                return normalizeNumber(phoneNum);
            }

//			PhoneNumber phoneNum = PhoneUtils.parseNumber(number);
//
//			if(phoneNum == null) {
//				phoneNum = PhoneUtils.parseNumber(number, PhoneUtils.DEFAULT_COUNTRY_CODE_ALPHA2);
//			}
//
//			return PhoneUtils.(phoneNum, PhoneNumberFormat.INTERNATIONAL);
        }
        catch(Exception e) {
            AppLogManager.error(e);
        }

//		return number;
        return null;
    }
    */

    public static String normalizeNumber(PhoneNumber phoneNum) {
        return "+" + formatNumberDigitsOnly(phoneNum, PhoneNumberFormat.INTERNATIONAL);
    }

    public static String normalizeNumber(String number) {
        return normalizeNumber(number, "");
    }

    public static String normalizeNumber(String number, String countryCode) {
        return "+" + formatNumberDigitsOnly(number, PhoneNumberFormat.INTERNATIONAL, countryCode);

		/*
		number = number.toLowerCase();

		// if the number ends with e11, then that is Excel corrupting it, remove it
		if (number.endsWith("e+11") || number.endsWith("e+12")) {
			number = number.substring(0, number.length() - 4).replace(".", "");
		}

		// remove other characters
		number = number.replaceAll("[^0-9a-z\\+]", "");

		// add on a plus if it looks like it could be a fully qualified number
		if (number.length() > 11 && number.charAt(0) != '+') {
			number = '+' + number;
		}

		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		Phonenumber.PhoneNumber normalized = null;
		try {
			if(countryCodeMap.containsKey(countryCode)) {
				normalized = phoneUtil.parse(number, countryCodeMap.get(countryCode));
			}
			else {
				normalized = phoneUtil.parse(number, countryCode);
			}
		} catch (NumberParseException e) {
		}

		// now does it look plausible ?
		try {
			if (phoneUtil.isValidNumber(normalized)) {
				return phoneUtil.format(normalized, PhoneNumberUtil.PhoneNumberFormat.E164);
			}
		} catch (NullPointerException ex) {
		}

		// this must be a local number of some kind, just lowercase and save
		return number.replaceAll("[^0-9a-z]", "");
		*/
    }

	/*
	public static boolean isValidPhoneNumber(String str) {
		return isValidPhoneNumber(str, "");
	}
	*/

    public static boolean isValidNumber(String str) {
        return isValidNumber(str, "");
    }

    public static boolean isValidNumber(String str, String countryCode) {
        try {
            PhoneNumber phoneNumber = parseNumber(str, countryCode);
            return PhoneNumberUtil.getInstance().isValidNumber(phoneNumber);
        }
        catch(Exception e) {
            AppLogManager.error(e);
        }

        return false;
    }

    public static String formatNumberDigitsOnly(String number, PhoneNumberFormat numberFormat) {
        String countryCode = StringUtils.defaultIfBlank(PhoneNumberUtil.getInstance().getRegionCodeForNumber(parseNumber(number)), DEFAULT_COUNTRY_CODE_ALPHA2);

        if(PhoneUtils.isValidNumber(number, countryCode)) {
            return formatNumberDigitsOnly(number, numberFormat, countryCode);
        }

        return number;
    }

    public static String formatNumber(String number) {
        if(StringUtils.startsWith(number, "+")) {
            return formatNumber(number, PhoneNumberFormat.INTERNATIONAL);
        }
        else {
            return formatNumber(number, PhoneNumberFormat.INTERNATIONAL, DEFAULT_COUNTRY_CODE_ALPHA2);
        }
    }

    public static String formatNumber(String number, PhoneNumberFormat numberFormat) {
        String countryCode = StringUtils.defaultIfBlank(PhoneNumberUtil.getInstance().getRegionCodeForNumber(parseNumber(number)), DEFAULT_COUNTRY_CODE_ALPHA2);

        if(PhoneUtils.isValidNumber(number, countryCode)) {
            return formatNumber(number, numberFormat, countryCode);
        }

        return number;
    }

    public static String formatNumber(String number, PhoneNumberFormat numberFormat, String countryCode)
    {
        PhoneNumber phoneNumber = parseNumber(number, countryCode);

        String formatted = null;

        if(phoneNumber != null) {
            formatted = PhoneNumberUtil.getInstance().format(phoneNumber, numberFormat);
        }

        return StringUtils.defaultString(formatted, number);
    }

    public static String formatNumber(PhoneNumber phoneNumber, PhoneNumberFormat numberFormat)
    {
        return PhoneNumberUtil.getInstance().format(phoneNumber, numberFormat);
    }

    public static String formatNumberDigitsOnly(String number, PhoneNumberFormat numberFormat, String countryCode)
    {
        return PhoneNumberUtil.normalizeDigitsOnly(formatNumber(number, numberFormat, countryCode));
    }

    public static String formatNumberDigitsOnly(PhoneNumber phoneNumber, PhoneNumberFormat numberFormat)
    {
        return PhoneNumberUtil.normalizeDigitsOnly(formatNumber(phoneNumber, numberFormat));
    }

    public static String getCountryCodeAlpha2(String num) {
        return getCountryCodeAlpha2(parseNumber(num));
    }

    public Integer getCountryCodeNumeric(String num) {
        return getCountryCodeNumeric(parseNumber(num));
    }

    public static String getCountryCodeAlpha2(PhoneNumber phoneNumber) {
        if(phoneNumber != null) {
            return PhoneNumberUtil.getInstance().getRegionCodeForNumber(phoneNumber);
        }
        return null;
    }

    public static Integer getCountryCodeNumeric(PhoneNumber phoneNumber) {
        if(phoneNumber != null) {
            return phoneNumber.getCountryCode();
        }
        return null;
    }


	/*
	public static boolean isValidPhoneNumber(String str, String countryCode) {

		str = str.replaceAll("\\D+", "");

		countryCode = defaultIfBlank(countryCode, "KR");

		//RegexValidator validator = new RegexValidator("^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$");
		//return validator.isValid(str);

		if("KR".equals(countryCode)) {
			if(str.startsWith("010") & str.length() != 11) {
				return false;
			}
		}

		try {
			PhoneNumber phoneNumber = PhoneNumberUtil.getInstance().parse(str, countryCode);
			return PhoneNumberUtil.getInstance().isValidNumber(phoneNumber);
		}
		catch(Exception e) {
			return false;
		}
	}
	*/

    public static void main(String[] args) throws Exception {

//        AppLogManager.debug(normalizeNumber("+85523885538"));
//        AppLogManager.debug(DateTimeZone.getDefault().toString());
//        AppLogManager.debug(System.getProperty("user.country"));

//        PhoneNumber phoneNumber = PhoneUtils.parseNumber("85592333208");
        AppLogManager.debug(normalizeNumber("85592333208"));
//        AppLogManager.debug(ValidationHelper.validatePhoneNumber("85592333208")+"");
//        AppLogManager.debug(normalizeNumber("092333208", "KH"));
//        AppLogManager.debug(normalizeNumber("85592333208"));
//        AppLogManager.debug(getCountryCodeAlpha2("85592333208"));

//
//        String original = "85592333208";
//        AppLogManager.debug("from ui: " + original);
//
//        String normalized = normalizeNumber(original);
//        AppLogManager.debug("to db: " + normalized);
//
//        AppLogManager.debug("from db: " + PhoneUtils.formatNumber(normalized));
    }

    /*
    public static boolean isValidPhoneNumber(String mobNumber, String countryCode) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        PhoneNumber phoneNumber = null;
//        boolean finalNumber = false;
        PhoneNumberUtil.PhoneNumberType isMobile = null;
        boolean isValid = false;

        try {
            String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(countryCode));
            phoneNumber = phoneNumberUtil.parse(mobNumber, isoCode);
            isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            isMobile = phoneNumberUtil.getNumberType(phoneNumber);
        } catch (NumberParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (isValid && (PhoneNumberUtil.PhoneNumberType.MOBILE == isMobile ||
                PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE == isMobile)) {
//            finalNumber = true;
            return true;
        }

        return false;
    }
    */
}
