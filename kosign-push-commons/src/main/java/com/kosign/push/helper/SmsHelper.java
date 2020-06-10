package com.kosign.push.helper;

import com.kosign.push.beans.SingleTextualMessage;
import com.kosign.push.commons.StringUtils;
import com.kosign.push.enums.SmsVendor;
import com.wecambodia.WeUmsClient;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;

public class SmsHelper {
    static private String FROM = "BNK";
    static public SmsVendor SMS_VENDOR = SmsVendor.ECHO;

    static {
        //TODO: read from config resource
//        if (JexSystemConfig.get("sms", "from") != null) FROM = JexSystemConfig.get("sms", "from");
//        FROM = "SMS Info";  //TODO: for testing plasgate
//        SMS_VENDOR = SmsVendor.WEUMS;
    }

    public static void sendPin(HttpServletRequest request, String to) throws Exception {

        HttpSession session = request.getSession();

        // generate pin
        String pin = StringUtils.leftPad(RandomStringUtils.randomNumeric(6), 6, "0");

        SingleTextualMessage message = new SingleTextualMessage();
        message.setFrom(FROM);
        message.setRecipient(to);
        message.setText(MessageFormat.format("Your pin is {0}", pin));

        boolean isSuccess = false;

        switch (SMS_VENDOR) {
            case WEUMS:
                isSuccess = WeUmsClient.send(message);
                break;
//            case PLASGATE:
//                isSuccess = PlasgateClient.send(message);
//                break;
//            case INFOBIP:
//                isSuccess = InfobipClient.sendPin(request, FROM, to);
//                break;
            case ECHO:
                pin = "999999";
                isSuccess = true;
                break;
        }

        if(isSuccess) {
            // Set session
            session.setAttribute("PIN", pin);
        }
    }

    public static boolean send(SingleTextualMessage message) throws Exception {

        boolean isSuccess = false;

        switch(SMS_VENDOR) {
            case WEUMS:
                isSuccess = WeUmsClient.send(message);
                break;
//            case PLASGATE:
//                isSuccess = PlasgateClient.send(message);
//                break;
//            case INFOBIP:
//                isSuccess = InfobipClient.send(message);
//                break;
            case ECHO:
                isSuccess = true;
                break;
        }

        return isSuccess;
    }

    public static boolean verifyPin(HttpServletRequest request, String pin) {
        HttpSession session = request.getSession();

        boolean isSuccess = false;

        switch(SMS_VENDOR) {
//            case INFOBIP:
//                String pinId = (String)(session.getAttribute("PIN_ID"));
//                isSuccess = InfobipClient.verifyPin(pin, pinId);
//                break;
            default:
                String pinInSession = (String)(session.getAttribute("PIN"));
                isSuccess = pin.equals(pinInSession);
                break;
        }

        if(isSuccess) {
            session.removeAttribute("PIN");
            session.removeAttribute("PIN_ID");
        }

        return isSuccess;
    }

//    private static boolean verifyPin(String pin, String pinId) {
//        return true;
//    }


    static public void main(String[] args) throws Exception {
       //  sendPin("85593201410");
       // boolean isVerified = verifyPin("1762", "117C6C99DDE5921102BB6B53F8D89643");
       // AppLogManager.debug(isVerified);
       //  Application application = getApplication();
       // application.getConfiguration().setSendPinPerPhoneNumberLimit("100/1d");
       // updateApplication(application);
       // AppLogManager.debug("TEST");
    }

}
