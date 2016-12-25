package com.awinson.service;

import com.awinson.Entity.UserApi;
import com.awinson.Entity.User;
import com.awinson.Entity.UserRole;
import com.awinson.dictionary.Dict;
import com.awinson.repository.UserApiRepository;
import com.awinson.repository.UserRepository;
import com.awinson.repository.UserRoleRepository;
import com.awinson.valid.ApiKeyValid;
import com.awinson.valid.RegisterValid;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;
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


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private UserApiRepository userApiRepository;

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
                if (userApiRepository.findByPlatformAndApiType(apiKeyValid.getPlatform(), Dict.key.api) != null) {
                    userApi1 = userApiRepository.findByPlatformAndApiType(apiKeyValid.getPlatform(), Dict.key.api);
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
                if (userApiRepository.findByPlatformAndApiType(apiKeyValid.getPlatform(), Dict.key.secret) != null) {
                    userApi2 = userApiRepository.findByPlatformAndApiType(apiKeyValid.getPlatform(), Dict.key.secret);
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
         return userApiRepository.findByUserIdAndPlatformAndApiType(getUserId(),platform,apiType);
    }
}
