package com.kosign.push.utils.validations;

import com.kosign.push.platformSetting.PlatformSettingEntity;
import org.apache.commons.lang3.StringUtils;


public class PlatformSettingValidation {

    public static Boolean validateAPNS(PlatformSettingEntity platformSetting){
        return ( !StringUtils.isAnyEmpty( platformSetting.getPushUrl(),platformSetting.getBundleId(),platformSetting.getTeamId(),platformSetting.getKeyId()) );
    };

    public static Boolean validateFcm(PlatformSettingEntity platformSetting){
        return (!StringUtils.isAnyEmpty( platformSetting.getAuthorizedKey()) );
    }

}
