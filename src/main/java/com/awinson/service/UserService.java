package com.awinson.service;

import com.awinson.Entity.User;
import com.awinson.Entity.UserApi;
import com.awinson.Entity.UserLog;
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
     * 根据当前用户名的id
     * @return
     */
    User getUser();
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
     * 获取指定用户的资产信息，并放进缓存
     */
    Map<String,Object> getUserAssetsInfo2CacheByPlatform(User user,String platform);

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
     * 更新用户的设置(自动交易)
     * @param autoTradeBtc
     * @param autoTradeLtc
     * @return
     */
    String updateUserTradeSettingAuto(String autoTradeBtc,String autoTradeLtc);


    /**
     * 更新用户的设置(自动交易、阀值)
     * @param autoTradeBtc
     * @param autoTradeLtc
     * @return
     */
    String updateUserTradeSettingAuto(String autoTradeBtc,String autoTradeLtc,String autoThresholdBtc,String autoThresholdLtc);

    /**
     * 更新用户的设置(最小交易量)
     * @param eachAmountBtc
     * @param eachAmountLtc
     * @return
     */
    String updateUserTradeSettingEachAmount(String eachAmountBtc,String eachAmountLtc);

    /**
     * 获取当前用户的设置
     */
    UserTradeSetting getUserTradeSetting();

    /**
     *  新增日志
     * @param user  用户对象
     * @param type  日志类型
     * @param context   日志内容
     * @return
     */
    void addUserLog(User user,String type,String context);

    /**
     *  新增交易日志
     * @param user  用户对象
     * @param type  日志类型
     * @param context   日志内容
     * @return
     */
    void addTradeLog(User user, String type, String context);

    /**
     * 获取日志
     * @param type 日志类型
     * @return
     */
    List<UserLog> getUserLog(String userId,String type,String amount);

    /**
     *获取当前用户的日志
     * @param type
     * @param amount
     * @return
     */
    String  getLogByTypeAndAmount(String type, String amount);


    /**
     * 推送用户的账户信息
     */
    void pushAccoutInfoByWebSocket();

    /**
     * 推送用户的日志信息
     */
    void pushUserLogByWebSocket();

    /**
     * 推送用户的设置
     */
    void pushUserSetting();
}
