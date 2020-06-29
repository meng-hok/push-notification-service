package com.kosign.push.utils.validations;

import com.kosign.push.platformSetting.PlatformSetting;
import org.apache.commons.lang3.StringUtils;


public class PlatformSettingValidation {

    public static Boolean validateAPNS(PlatformSetting platformSetting){
        return ( !StringUtils.isAnyEmpty( platformSetting.getPushUrl(),platformSetting.getBundleId(),platformSetting.getTeamId(),platformSetting.getKeyId()) );
    };

    public static Boolean validateFcm(PlatformSetting platformSetting){
        return (!StringUtils.isAnyEmpty( platformSetting.getAuthorizedKey()) );
    }
}
