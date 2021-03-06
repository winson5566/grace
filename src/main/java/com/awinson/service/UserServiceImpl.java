package com.awinson.service;

import com.awinson.Entity.*;
import com.awinson.WebSocket.okcoin.WebSocketBase;
import com.awinson.WebSocket.web.WebSocketBaseService;
import com.awinson.cache.CacheManager;
import com.awinson.config.BitvcCnConfig;
import com.awinson.config.OkcoinCnConfig;
import com.awinson.config.OkcoinFutureConfig;
import com.awinson.config.OkcoinUnConfig;
import com.awinson.dictionary.Dict;
import com.awinson.repository.*;
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

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
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
    private UserLogRepository userLogRepository;

    @Autowired
    private OkcoinCnConfig okcoinCnConfig;
    @Autowired
    private OkcoinFutureConfig okcoinFutureConfig;
    @Autowired
    private OkcoinUnConfig okcoinUnConfig;
    @Autowired
    private BitvcCnConfig bitvcCnConfig;
    @Autowired
    private OkcoinService okcoinService;
    @Autowired
    private BitvcService bitvcService;
    @Autowired
    private WebSocketBaseService webSocketBaseService;

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

            user.setEnable(Dict.ENABLE.YES);
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
    public User getUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            String username = userDetail.getUsername();
            User user = userRepository.findByUsername(username);
            if (user != null) {
                return user;
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
                if (userApiRepository.findByUserIdAndPlatformAndApiType(id, apiKeyValid.getPlatform(), Dict.KEY.API) != null) {
                    userApi1 = userApiRepository.findByUserIdAndPlatformAndApiType(id, apiKeyValid.getPlatform(), Dict.KEY.API);
                } else {
                    userApi1.setId(UUID.randomUUID().toString());
                }
                userApi1.setApi(apiKeyValid.getApiKey());
                userApi1.setApiType(Dict.KEY.API);
                userApiRepository.save(userApi1);
            }

            userApi2.setPlatform(apiKeyValid.getPlatform());
            userApi2.setUserId(id);
            if (apiKeyValid.getSecretKey() != null && apiKeyValid.getSecretKey() != "") {
                if (userApiRepository.findByUserIdAndPlatformAndApiType(id, apiKeyValid.getPlatform(), Dict.KEY.SECRET) != null) {
                    userApi2 = userApiRepository.findByUserIdAndPlatformAndApiType(id, apiKeyValid.getPlatform(), Dict.KEY.SECRET);
                } else {
                    userApi2.setId(UUID.randomUUID().toString());
                }
                userApi2.setApi(apiKeyValid.getSecretKey());
                userApi2.setApiType(Dict.KEY.SECRET);
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
                case Dict.KEY.API:
                    apiType = "api-key";
                    break;
                case Dict.KEY.SECRET:
                    apiType = "secret-key";
                    break;
                default:
                    platform = "未知";
            }
            switch (platform) {
                case Dict.PLATFORM.OKCOIN_CN:
                    platform = "OKcoin中国站";
                    break;
                case Dict.PLATFORM.OKCOIN_UN:
                    platform = "OKcoin国际站现货";
                    break;
                case Dict.PLATFORM.OKCOIN_FUTURE:
                    platform = "OKcoin国际站期货";
                    break;
                case Dict.PLATFORM.BITVC_CN:
                    platform = "BITVC中国站";
                    break;
                case Dict.PLATFORM.BITVC_UN:
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
        List<User> list = userRepository.findByEnable(Dict.ENABLE.YES);
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

    @Override
    public Map<String, Object> getUserAssetsInfo2CacheByPlatform(User user, String platform) {
        Map<String, Object> result = new HashMap();
        //获取平台的API key
        String apiKey = userApiRepository.findByUserIdAndPlatformAndApiType(user.getId(), platform, Dict.KEY.API).getApi();
        String secretKey = userApiRepository.findByUserIdAndPlatformAndApiType(user.getId(), platform, Dict.KEY.SECRET).getApi();
        Map<String, Object> map = new HashMap();
        if (Dict.PLATFORM.OKCOIN_CN.equals(platform) || Dict.PLATFORM.OKCOIN_UN.equals(platform) || Dict.PLATFORM.OKCOIN_FUTURE.equals(platform)) {
            map = okcoinService.getSpotUserinfo(platform, apiKey, secretKey);
        } else if (Dict.PLATFORM.BITVC_CN.equals(platform) || Dict.PLATFORM.BITVC_UN.equals(platform)) {
            map = bitvcService.getSpotUserinfo(platform, apiKey, secretKey);
        }
        if (map != null && map.size() > 0) {
            map.put("timestamp", String.valueOf(System.currentTimeMillis()));
            CacheManager.update(Dict.TYPE.ASSETS + platform + "_" + user.getId(), map);
            result.put("code", "1");
            result.put("msg", "获取资产成功");
        } else {
            result.put("code", "0");
            result.put("msg", "获取资产失败");
        }
        return result;
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
                String apiKey = apiMap.get(Dict.KEY.API);
                String secretKey = apiMap.get(Dict.KEY.SECRET);
                String url = null;
                switch (platform) {
                    case Dict.PLATFORM.OKCOIN_CN:
                        url = okcoinCnConfig.getUserinfo();
                        break;
                    case Dict.PLATFORM.OKCOIN_UN:
                        url = okcoinUnConfig.getUserinfo();
                        break;
                    case Dict.PLATFORM.OKCOIN_FUTURE:
                        url = okcoinFutureConfig.getUserinfo();
                        break;
                    case Dict.PLATFORM.BITVC_CN:
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
        if (Dict.PLATFORM.OKCOIN_CN.equals(platform) || Dict.PLATFORM.OKCOIN_UN.equals(platform) || Dict.PLATFORM.OKCOIN_FUTURE.equals(platform)) {
            map = okcoinService.getSpotUserinfo(platform, apiKey, secretKey);
        } else if (Dict.PLATFORM.BITVC_CN.equals(platform) || Dict.PLATFORM.BITVC_UN.equals(platform)) {
            map = bitvcService.getSpotUserinfo(platform, apiKey, secretKey);
        }
        if (map != null && map.size() > 0) {
            map.put("timestamp", String.valueOf(System.currentTimeMillis()));
            CacheManager.update(Dict.TYPE.ASSETS + platform + "_" + userId, map);
        }
    }


    @Override
    public String updateUserTradeSetting(String buyPlatform, String sellPlatform, String coin, String margin) {
        String userId = getUserId();
        UserTradeSetting userTradeSetting;
        userTradeSetting = userTradeSettingRepository.findByUserId(userId);
        //如果用户交易设置就新增
        if (userTradeSetting == null) {

            userTradeSetting = new UserTradeSetting();
            userTradeSetting.setId(UUID.randomUUID().toString());
            userTradeSetting.setUserId(userId);
            userTradeSetting.setAutoTradeBtc("0");
            userTradeSetting.setAutoTradeLtc("0");
            userTradeSetting.setAutoThresholdBtc("0");
            userTradeSetting.setAutoThresholdLtc("0");
            userTradeSetting.setEachAmountBtc("0");
            userTradeSetting.setEachAmountLtc("0");
        }
        //获取用户交易的json格式
        String marginJson = userTradeSetting.getMarginJson();
        Map marginMap;
        Gson gson = new Gson();
        if (marginJson != null && !"".equals(marginJson)) {
            marginMap = gson.fromJson(marginJson, Map.class);    //如果margin_json字段为空
        } else {
            marginMap = new HashMap();
        }
        marginMap.put(Dict.TYPE.SETTING + buyPlatform + sellPlatform + coin, margin);
        userTradeSetting.setMarginJson(gson.toJson(marginMap));
        userTradeSettingRepository.save(userTradeSetting);
        addUserLog(getUser(), Dict.LOGTYPE.USER, "更改阀值,[币种]:" + coin + "  [买平台]:" + buyPlatform + "  [卖平台]:" + sellPlatform + "  [阀值]:" + margin);
        return getUserTradeSetting().getMarginJson();
    }

    @Override
    public String updateUserTradeSettingAuto(String autoTradeBtc, String autoTradeLtc) {
        String userId = getUserId();
        UserTradeSetting userTradeSetting;
        userTradeSetting = userTradeSettingRepository.findByUserId(userId);
        //如果用户交易设置就新增
        if (userTradeSetting == null) {
            userTradeSetting = new UserTradeSetting();
            userTradeSetting.setId(UUID.randomUUID().toString());
            userTradeSetting.setUserId(userId);
        }
        userTradeSetting.setAutoTradeBtc(autoTradeBtc);
        userTradeSetting.setAutoTradeLtc(autoTradeLtc);
        userTradeSettingRepository.save(userTradeSetting);
        Map<String, String> result = new HashMap();
        result.put("autoTradeBtc", autoTradeBtc);
        result.put("autoTradeLtc", autoTradeLtc);
        addUserLog(getUser(), Dict.LOGTYPE.USER, "更改自动设置,[BTC自动交易]:" + autoTradeBtc + "  [LTC自动交易]:" + autoTradeLtc);
        Gson gson = new Gson();
        return gson.toJson(result);
    }

    @Override
    public String updateUserTradeSettingAuto(String autoTradeBtc, String autoTradeLtc, String autoThresholdBtc, String autoThresholdLtc) {
        String userId = getUserId();
        UserTradeSetting userTradeSetting;
        userTradeSetting = userTradeSettingRepository.findByUserId(userId);
        //如果用户交易设置就新增
        if (userTradeSetting == null) {
            userTradeSetting = new UserTradeSetting();
            userTradeSetting.setId(UUID.randomUUID().toString());
            userTradeSetting.setUserId(userId);
        }
        userTradeSetting.setAutoTradeBtc(autoTradeBtc);
        userTradeSetting.setAutoTradeLtc(autoTradeLtc);
        userTradeSetting.setAutoThresholdBtc(autoThresholdBtc);
        userTradeSetting.setAutoThresholdLtc(autoThresholdLtc);
        userTradeSettingRepository.save(userTradeSetting);
        Map<String, String> result = new HashMap();
        result.put("autoTradeBtc", autoTradeBtc);
        result.put("autoTradeLtc", autoTradeLtc);
        result.put("autoThresholdBtc", autoThresholdBtc);
        result.put("autoThresholdLtc", autoThresholdLtc);
        addUserLog(getUser(), Dict.LOGTYPE.USER, "更改自动设置,[BTC自动交易]:" + autoTradeBtc + "  [LTC自动交易]:" + autoTradeLtc + "  [BTC自动阀值]:" + autoThresholdBtc + "  [LTC自动阀值]:" + autoThresholdLtc);
        Gson gson = new Gson();
        return gson.toJson(result);
    }

    @Override
    public String updateUserTradeSettingEachAmount(String eachAmountBtc, String eachAmountLtc) {
        String userId = getUserId();
        UserTradeSetting userTradeSetting;
        userTradeSetting = userTradeSettingRepository.findByUserId(userId);
        //如果用户交易设置就新增
        if (userTradeSetting == null) {
            userTradeSetting = new UserTradeSetting();
            userTradeSetting.setId(UUID.randomUUID().toString());
            userTradeSetting.setUserId(userId);
        }
        if (eachAmountBtc != null && !"".equals(eachAmountBtc)) {
            userTradeSetting.setEachAmountBtc(eachAmountBtc);
        }
        if (eachAmountLtc != null && !"".equals(eachAmountLtc)) {
            userTradeSetting.setEachAmountLtc(eachAmountLtc);
        }
        userTradeSettingRepository.save(userTradeSetting);
        Map<String, String> result = new HashMap();
        result.put("eachAmountBtc", eachAmountBtc);
        result.put("eachAmountLtc", eachAmountLtc);
        addUserLog(getUser(), Dict.LOGTYPE.USER, "更改单位交易量,[BTC]:" + eachAmountBtc + "  [LTC]:" + eachAmountLtc);

        Gson gson = new Gson();
        return gson.toJson(result);
    }

    @Override
    public void addUserLog(User user, String type, String context) {
        UserLog userLog = new UserLog(user.getId(), type, context);
        userLogRepository.save(userLog);

//        //写入缓存
//        List<UserLog> userLogList = (List<UserLog>) CacheManager.get(Dict.TYPE.LOG + type + user.getId());
//        if (userLogList == null || userLogList.size() <= 0) {  //如果缓存中不存在
//            userLogList = new ArrayList();
//        }
//        userLogList.add(userLog);
//        CacheManager.update((Dict.TYPE.LOG + type + user.getId()), userLogList);
    }

    @Override
    public void addTradeLog(User user, String type, String context) {
        UserLog userLog = new UserLog(user.getId(), type, context);
        userLogRepository.save(userLog);
    }

    @Override
    public List<UserLog> getUserLog(String userId, String type, String amount) {
        if ("10".equals(amount)) {
            return userLogRepository.findTop10ByUserIdAndTypeOrderByCreateTimestampDesc(userId, type);
        }
        return userLogRepository.findTop10ByUserIdAndTypeOrderByCreateTimestampDesc(userId, type);
    }

    @Override
    public String getLogByTypeAndAmount(String type, String amount) {
        Gson gson = new Gson();
        List<UserLog> list;
        if ("200".equals(amount)) {
            list = userLogRepository.findTop200ByUserIdAndTypeOrderByCreateTimestampDesc(getUserId(), type);
        } else {
            list = userLogRepository.findTop200ByUserIdAndTypeOrderByCreateTimestampDesc(getUserId(), type);
        }
        return gson.toJson(list);
    }
//    /**
//     * 初始化用户的日志到缓存
//     */
//    @PostConstruct
//    public void init() {
//        List<User> userList = userRepository.findByEnable(Dict.ENABLE.YES);
//        for (User user : userList) {
//            List<UserLog> userLogList = userLogRepository.findTop100ByUserIdAndTypeOrderByStartTimestampDesc(user.getId(), Dict.LOGTYPE.USER);
//            List<UserLog> thresholdLogList = userLogRepository.findTop100ByUserIdAndTypeOrderByStartTimestampDesc(user.getId(), Dict.LOGTYPE.THRESHOLD);
//            List<UserLog> tradeLogList = userLogRepository.findTop100ByUserIdAndTypeOrderByStartTimestampDesc(user.getId(), Dict.LOGTYPE.TRADE);
//        }
//    }


    @Override
    public void pushAccoutInfoByWebSocket() {
        List<User> list = userRepository.findByEnable(Dict.ENABLE.YES);
        Gson gson = new Gson();
        for (User user : list) {
            Map<String, Object> map = new HashMap();
            Map<String, Object> okcoinCnMap = (Map<String, Object>) CacheManager.get(Dict.TYPE.ASSETS + Dict.PLATFORM.OKCOIN_CN + "_" + user.getId());
            Map<String, Object> okcoinUnMap = (Map<String, Object>) CacheManager.get(Dict.TYPE.ASSETS + Dict.PLATFORM.OKCOIN_UN + "_" + user.getId());
            Map<String, Object> okcoinFutureMap = (Map<String, Object>) CacheManager.get(Dict.TYPE.ASSETS + Dict.PLATFORM.OKCOIN_FUTURE + "_" + user.getId());
            Map<String, Object> bitvcCnMap = (Map<String, Object>) CacheManager.get(Dict.TYPE.ASSETS + Dict.PLATFORM.BITVC_CN + "_" + user.getId());
            if (okcoinCnMap != null && okcoinCnMap.size() > 0)
                map.put("p" + Dict.PLATFORM.OKCOIN_CN, okcoinCnMap);
            if (okcoinUnMap != null && okcoinUnMap.size() > 0)
                map.put("p" + Dict.PLATFORM.OKCOIN_UN, okcoinUnMap);
            if (okcoinFutureMap != null && okcoinFutureMap.size() > 0)
                map.put("p" + Dict.PLATFORM.OKCOIN_FUTURE, okcoinFutureMap);
            if (bitvcCnMap != null && bitvcCnMap.size() > 0)
                map.put("p" + Dict.PLATFORM.BITVC_CN, bitvcCnMap);
            if (map.size() > 0) {
                String json = gson.toJson(map);
                webSocketBaseService.broadcastToUser(user.getUsername(), Dict.QUEUE.ASSETS, json);
            }
        }
    }

    @Override
    public void pushUserLogByWebSocket() {
        List<User> list = userRepository.findByEnable(Dict.ENABLE.YES);
        Gson gson = new Gson();
        for (User user : list) {
            Map<String, List<UserLog>> map = new HashMap();
            List<UserLog> thresholdList = getUserLog(user.getId(), Dict.LOGTYPE.THRESHOLD, "10");
            List<UserLog> analyseList = getUserLog(user.getId(), Dict.LOGTYPE.ANALYSE, "10");
            List<UserLog> tradeList = getUserLog(user.getId(), Dict.LOGTYPE.TRADE, "10");
            if (thresholdList != null && thresholdList.size() > 0)
                map.put("thresholdList", thresholdList);
            if (analyseList != null && analyseList.size() > 0)
                map.put("analyseList", analyseList);
            if (tradeList != null && tradeList.size() > 0)
                map.put("tradeList", tradeList);
            if (map.size() > 0) {
                String json = gson.toJson(map);
                webSocketBaseService.broadcastToUser(user.getUsername(), Dict.QUEUE.LOG, json);
            }
        }
    }

    @Override
    public void pushUserSetting() {
        List<User> list = userRepository.findByEnable(Dict.ENABLE.YES);
        Gson gson = new Gson();
        for (User user : list) {
            UserTradeSetting userTradeSetting = userTradeSettingRepository.findByUserId(user.getId());
            String json = gson.toJson(userTradeSetting);
            webSocketBaseService.broadcastToUser(user.getUsername(), Dict.QUEUE.SETTING, json);
        }
    }

    @Override
    public UserTradeSetting getUserTradeSetting() {
        String userId = getUserId();
        UserTradeSetting userTradeSetting = userTradeSettingRepository.findByUserId(userId);
        return userTradeSetting;
    }
}
