package com.awinson.service;

import com.awinson.Entity.UserApi;
import com.awinson.Entity.UserTradeSetting;
import com.awinson.valid.ApiKeyValid;
import com.awinson.valid.RegisterValid;

import java.util.List;
import java.util.Map;

/**
 * Created by 10228 on 2016/12/11.
 */
public interface UserService {
    /**
     * 注册
     * @param registerValid
     * @return
     */
    Map<String,Object> register(RegisterValid registerValid);

    /**
     * 更新用户的api-key
     * @param apiKeyValid
     * @return
     */
    Map<String,Object> updateApiKey(ApiKeyValid apiKeyValid);

    /**
     * 根据当前用户名的id
     * @return
     */
    String getUserId();

    /**
     * 获取用户所有的apikey的数据
     * @return
     */
    Map<String,Map<String,String>> getUserAllApi();

    /**
     * 获取制定平台的
     * @param platform
     * @return
     */
    UserApi getUserApiWithPlatformAndApiType(String platform,String apiType);

    /**
     * 获取有所可用用户的资产信息，并放进缓存
     */
    void getAllUserAssetsInfo2Cache();

    /**
     * 获取用户的设置
     */
    UserTradeSetting getUserTradeSetting();

    /**
     * 更新用户的设置
     * @param buyPlatform
     * @param sellPlatform
     * @param coin
     * @param margin
     * @return
     */
    String updateUserTradeSetting(String buyPlatform,String sellPlatform,String coin,String margin);

    /**
     * 更新用户的设置(自动交易和阀值)
     * @param autoTradeBtc
     * @param autoTradeLtc
     * @return
     */
    String updateUserTradeSettingAuto(String autoTradeBtc,String autoTradeLtc,String autoThresholdBtc,String autoThresholdLtc);
}
