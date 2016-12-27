package com.awinson.service;

import com.awinson.Entity.UserApi;
import com.awinson.Entity.User;
import com.awinson.Entity.UserRole;
import com.awinson.Entity.UserTradeSetting;
import com.awinson.cache.CacheManager;
import com.awinson.config.BitvcCnConfig;
import com.awinson.config.OkcoinCnConfig;
import com.awinson.config.OkcoinUnConfig;
import com.awinson.dictionary.Dict;
import com.awinson.repository.UserApiRepository;
import com.awinson.repository.UserRepository;
import com.awinson.repository.UserRoleRepository;
import com.awinson.repository.UserTradeSettingRepository;
import com.awinson.utils.StringUtil;
import com.awinson.valid.ApiKeyValid;
import com.awinson.valid.RegisterValid;
import com.google.gson.Gson;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by 10228 on 2016/12/11.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserApiRepository userApiRepository;

    @Autowired
    private UserTradeSettingRepository userTradeSettingRepository;

    @Autowired
    private OkcoinCnConfig okcoinCnConfig;
    @Autowired
    private OkcoinUnConfig okcoinUnConfig;
    @Autowired
    private BitvcCnConfig bitvcCnConfig;
    @Autowired
    private OkcoinService okcoinService;
    @Autowired
    private BitvcService bitvcService;

    @Override
    public Map<String, Object> register(RegisterValid registerValid) {
        Map<String, Object> map = new HashMap();
        if (userRepository.findByUsername(registerValid.getUsername()) != null) {
            map.put("code", "0");
            map.put("msg", "该用户名已存在");
        } else {
            User user = new User();
            String userId = UUID.randomUUID().toString();
            user.setId(userId);
            user.setUsername(registerValid.getUsername());

            //盐值
            String salt = UUID.randomUUID().toString();
            user.setSalt(salt);
            String password = registerValid.getPassword();
            user.setPassword(password);

            user.setEnable("1");
            userRepository.save(user);

            //保存权限
            UserRole userRole = new UserRole();
            userRole.setId(UUID.randomUUID().toString());
            userRole.setUserId(userId);
            userRole.setRoleId("00024bd8-dfdf-4dbf-8a76-1507008fe402");
            userRoleRepository.save(userRole);

            map.put("code", "1");
            map.put("msg", "注册成功");
        }
        return map;
    }

    @Override
    public String getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            String username = userDetail.getUsername();
            User user = userRepository.findByUsername(username);
            if (user != null) {
                return user.getId();
            }
        }
        return null;
    }

    @Override

    public Map<String, Object> updateApiKey(ApiKeyValid apiKeyValid) {
        Map<String, Object> map = new HashMap();
        UserApi userApi1 = new UserApi();
        UserApi userApi2 = new UserApi();
        String id = getUserId();
        if (id != null && id != "") {
            userApi1.setPlatform(apiKeyValid.getPlatform());
            userApi1.setUserId(id);
            if (apiKeyValid.getApiKey() != null && apiKeyValid.getApiKey() != "") {
                if (userApiRepository.findByUserIdAndPlatformAndApiType(id,apiKeyValid.getPlatform(), Dict.key.api) != null) {
                    userApi1 = userApiRepository.findByUserIdAndPlatformAndApiType(id,apiKeyValid.getPlatform(), Dict.key.api);
                } else {
                    userApi1.setId(UUID.randomUUID().toString());
                }
                userApi1.setApi(apiKeyValid.getApiKey());
                userApi1.setApiType(Dict.key.api);
                userApiRepository.save(userApi1);
            }

            userApi2.setPlatform(apiKeyValid.getPlatform());
            userApi2.setUserId(id);
            if (apiKeyValid.getSecretKey() != null && apiKeyValid.getSecretKey() != "") {
                if (userApiRepository.findByUserIdAndPlatformAndApiType(id,apiKeyValid.getPlatform(), Dict.key.secret) != null) {
                    userApi2 = userApiRepository.findByUserIdAndPlatformAndApiType(id,apiKeyValid.getPlatform(), Dict.key.secret);
                } else {
                    userApi2.setId(UUID.randomUUID().toString());
                }
                userApi2.setApi(apiKeyValid.getSecretKey());
                userApi2.setApiType(Dict.key.secret);
                userApiRepository.save(userApi2);
            }
            map.put("code", "1");
            map.put("msg", "更新成功");
        } else {
            map.put("code", "0");
            map.put("msg", "找不到用户，更新失败");
        }
        return map;
    }

    @Override
    public Map<String, Map<String, String>> getUserAllApi() {
        Map<String, Map<String, String>> map = new HashMap();
        List<UserApi> list = userApiRepository.findByUserId(getUserId());
        for (UserApi userApi : list) {
            String apiType = userApi.getApiType();
            String platform = userApi.getPlatform();
            switch (apiType) {
                case Dict.key.api:
                    apiType = "api-key";
                    break;
                case Dict.key.secret:
                    apiType = "secret-key";
                    break;
                default:
                    platform = "未知";
            }
            switch (platform) {
                case Dict.Platform.OKCOIN_CN:
                    platform = "OKcoin中国站";
                    break;
                case Dict.Platform.OKCOIN_UN:
                    platform = "OKcoin国际站";
                    break;
                case Dict.Platform.BITVC_CN:
                    platform = "BITVC中国站";
                    break;
                case Dict.Platform.BITVC_UN:
                    platform = "BITVC国际站站";
                    break;
                default:
                    apiType = "未知";
            }
            Map<String, String> platformMap;
            if (map.containsKey(platform)) {
                platformMap = map.get(platform);
            } else {
                platformMap = new HashMap();
            }
            platformMap.put(apiType, userApi.getApi());
            map.put(platform.toString(), platformMap);
        }
        return map;
    }

    @Override
    public UserApi getUserApiWithPlatformAndApiType(String platform, String apiType) {
        return userApiRepository.findByUserIdAndPlatformAndApiType(getUserId(), platform, apiType);
    }

    @Override
    public void getAllUserAssetsInfo2Cache() {
        // 获取所有可用的用户
        List<User> list = userRepository.findByEnable(Dict.Enable.YES);
        Map<String, Object> userApiMap = new HashMap();
        //遍历用户查询是否有符合条件的key对
        for (User user : list) {

            //获取用户所有的api记录
            List<UserApi> apiList = userApiRepository.findByUserId(user.getId());
            if (apiList != null && apiList.size() > 0) {
                Map<String, Object> oneUserApiMap = new HashMap();
                //分平台
                for (UserApi userApi : apiList) {
                    //每一个用户,整理key
                    String platform = userApi.getPlatform();
                    Map<String, String> tempMap;
                    if (oneUserApiMap.containsKey(platform)) {
                        tempMap = (Map<String, String>) oneUserApiMap.get(platform);
                    } else {
                        tempMap = new HashMap();
                    }
                    String apiType = userApi.getApiType();
                    String api = userApi.getApi();
                    tempMap.put(apiType, api);
                    oneUserApiMap.put(platform, tempMap);
                }
                userApiMap.put(user.getId(), oneUserApiMap);
                getInfo2CacheByUserApiMap(userApiMap);
            }
        }
    }

    /**
     * 获取URL请求用户信息
     *
     * @param userApiMap
     */
    private void getInfo2CacheByUserApiMap(Map<String, Object> userApiMap) {
        for (Map.Entry<String, Object> entry : userApiMap.entrySet()) {
            String userId = entry.getKey();
            Map<String, Object> userApi = (Map<String, Object>) entry.getValue();
            for (Map.Entry<String, Object> platformEntry : userApi.entrySet()) {
                String platform = platformEntry.getKey();
                Map<String, String> apiMap = (Map<String, String>) platformEntry.getValue();
                String apiKey = apiMap.get(Dict.key.api);
                String secretKey = apiMap.get(Dict.key.secret);
                String url = null;
                switch (platform) {
                    case Dict.Platform.OKCOIN_CN:
                        url = okcoinCnConfig.getUserinfo();
                        break;
                    case Dict.Platform.OKCOIN_UN:
                        url = okcoinUnConfig.getUserinfo();
                        break;
                    case Dict.Platform.BITVC_CN:
                        url = bitvcCnConfig.getUserinfo();
                        break;
                    default:
                        break;
                }
                if (!StringUtil.isEmpty(apiKey) && !StringUtil.isEmpty(secretKey) && !StringUtil.isEmpty(url)) {
                    getUserInfo2Cache(userId, platform, apiKey, secretKey);
                }
            }
        }
    }

    private void getUserInfo2Cache(String userId, String platform, String apiKey, String secretKey) {
        Map<String, Object> map = new HashMap();
        if (Dict.Platform.OKCOIN_CN.equals(platform) || Dict.Platform.OKCOIN_UN.equals(platform)) {
            map = okcoinService.getSpotUserinfo(platform, apiKey, secretKey);
        } else if (Dict.Platform.BITVC_CN.equals(platform) || Dict.Platform.BITVC_UN.equals(platform)) {
            map = bitvcService.getSpotUserinfo(platform, apiKey, secretKey);
        }
        if (map != null && map.size() > 0) {
            map.put("timestamp",String.valueOf(System.currentTimeMillis()));
            CacheManager.update(Dict.Type.ASSETS + platform +"_"+userId, map);
        }
    }

    @Override
    public UserTradeSetting getUserTradeSetting() {
        String userId = getUserId();
        UserTradeSetting userTradeSetting = userTradeSettingRepository.findByUserId(userId);
        return userTradeSetting;
    }

    @Override
    public String updateUserTradeSetting(String buyPlatform,String sellPlatform,String coin,String margin){
        String userId = getUserId();
        UserTradeSetting userTradeSetting;
        userTradeSetting = userTradeSettingRepository.findByUserId(userId);
        //如果用户交易设置就新增
        if (userTradeSetting==null){
            userTradeSetting =new UserTradeSetting();
            userTradeSetting.setId(UUID.randomUUID().toString());
            userTradeSetting.setUserId(userId);
            userTradeSetting.setAutoTradeBtc("0");
            userTradeSetting.setAutoTradeLtc("0");
            userTradeSetting.setAutoThresholdBtc("0");
            userTradeSetting.setAutoThresholdLtc("0");
        }
        //获取用户交易的json格式
        String marginJson = userTradeSetting.getMarginJson();
        Map marginMap;
        Gson gson = new Gson();
        if (marginJson!=null&&!"".equals(marginJson)){
            marginMap = gson.fromJson(marginJson,Map.class);    //如果margin_json字段为空
        }else {
            marginMap = new HashMap();
        }
        marginMap.put(Dict.Type.SETTING+buyPlatform+sellPlatform+coin,margin);
        userTradeSetting.setMarginJson(gson.toJson(marginMap));
        userTradeSettingRepository.save(userTradeSetting);
        return  getUserTradeSetting().getMarginJson();
    }
    @Override
    public String updateUserTradeSettingAuto(String autoTradeBtc,String autoTradeLtc,String autoThresholdBtc,String autoThresholdLtc){
        String userId = getUserId();
        UserTradeSetting userTradeSetting;
        userTradeSetting = userTradeSettingRepository.findByUserId(userId);
        //如果用户交易设置就新增
        if (userTradeSetting==null){
            userTradeSetting =new UserTradeSetting();
            userTradeSetting.setId(UUID.randomUUID().toString());
            userTradeSetting.setUserId(userId);
        }
        userTradeSetting.setAutoTradeBtc(autoTradeBtc);
        userTradeSetting.setAutoTradeLtc(autoTradeLtc);
        userTradeSetting.setAutoThresholdBtc(autoThresholdBtc);
        userTradeSetting.setAutoThresholdLtc(autoThresholdLtc);
        userTradeSettingRepository.save(userTradeSetting);
        Map<String,String> result = new HashMap();
        result.put("autoTradeBtc",autoTradeBtc);
        result.put("autoTradeLtc",autoTradeLtc);
        result.put("autoThresholdBtc",autoThresholdBtc);
        result.put("autoThresholdLtc",autoThresholdLtc);
        Gson gson = new Gson();
        return  gson.toJson(result);
    }
}
