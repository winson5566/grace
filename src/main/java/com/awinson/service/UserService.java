package com.awinson.service;

import com.awinson.Entity.UserApi;
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
}
