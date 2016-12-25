package com.awinson.service;

import com.awinson.Entity.UserApi;
import com.awinson.config.BitvcCnConfig;
import com.awinson.dictionary.Dict;
import com.awinson.utils.HttpUtils;
import com.awinson.utils.MD5Util;
import com.awinson.utils.StringUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 10228 on 2016/12/16.
 */
@Service
public class BitvcServiceImpl implements BitvcService{
    @Autowired
    private UserService userService;

    @Autowired
    private BitvcCnConfig bitvcCnConfig;

    @Override
    public Map<String, Object> getSpotUserinfo(String platform) {
        Map<String,Object> map  = new HashMap();
        //获取API
        UserApi userApi1 = userService.getUserApiWithPlatformAndApiType(platform, Dict.key.api);
        UserApi userApi2 = userService.getUserApiWithPlatformAndApiType(platform, Dict.key.secret);
        if (userApi1!=null&&userApi2!=null){
            String accessKey = userApi1.getApi();
            String secretKey = userApi2.getApi();
            if (!StringUtil.isEmpty(accessKey)&&!StringUtil.isEmpty(secretKey)){
                map=getSpotUserinfo(platform,accessKey,secretKey);
            }
        }
        map.put("code","0");
        map.put("msg","Bitvc的key不完整，请检查！");
        return map;
    }

    @Override
    public Map<String, Object> getSpotUserinfo(String platform, String accessKey, String secretKey) {
        Map<String,Object> map  = new HashMap();
        // 构造参数签名
        Map<String, String> params = new HashMap();
        params.put("access_key", accessKey);
        params.put("created", String.valueOf(System.currentTimeMillis()).toString().substring(0,10));
        String sign = MD5Util.buildMysignV1(params, secretKey);
        params.put("sign", sign.toLowerCase());
        // 发送post请求
        try {
            String url ;
            if (platform.equals(Dict.Platform.BITVC_CN)){
                url =bitvcCnConfig.getUserinfo();
            }else {
                url =bitvcCnConfig.getUserinfo();
            }
            String result = HttpUtils.doPost(url,params);
            map.put("code","1");
            map.put("msg","OK");
            Gson gson = new Gson();
            map.put("result",gson.fromJson(result,Map.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
