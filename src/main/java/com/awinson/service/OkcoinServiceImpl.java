package com.awinson.service;

import com.awinson.Entity.User;
import com.awinson.Entity.UserApi;
import com.awinson.config.OkcoinCnConfig;
import com.awinson.config.OkcoinUnConfig;
import com.awinson.dictionary.Dict;
import com.awinson.utils.HttpUtils;
import com.awinson.utils.MD5Util;
import com.awinson.utils.StringUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 10228 on 2016/12/13.
 */
@Service
@Transactional
public class OkcoinServiceImpl  implements OkcoinService{

    private static final Logger logger  = LoggerFactory.getLogger(OkcoinServiceImpl.class);

    @Autowired
    private OkcoinCnConfig okcoinCnConfig;
    @Autowired
    private OkcoinUnConfig okcoinUnConfig;
    @Autowired
    private UserService userService;

    @Override
    public Map<String,Object> getSpotUserinfo(String platform) {
        Map<String,Object> map  = new HashMap();
        UserApi userApi1 = userService.getUserApiWithPlatformAndApiType(platform, Dict.key.api);
        UserApi userApi2 = userService.getUserApiWithPlatformAndApiType(platform, Dict.key.secret);
        if (userApi1!=null&&userApi2!=null){
            String apiKey = userApi1.getApi();
            String secretKey = userApi2.getApi();
            if (!StringUtil.isEmpty(apiKey)&&!StringUtil.isEmpty(secretKey)){
                // 构造参数签名
                Map<String, String> params = new HashMap();
                params.put("api_key", apiKey);
                String sign = MD5Util.buildMysignV1(params, secretKey);
                params.put("sign", sign);
                // 发送post请求
                try {
                    String url ;
                    if (platform.equals(Dict.Platform.OKCOIN_CN)){
                        url =okcoinCnConfig.getUserinfo();
                    }else {
                        url =okcoinUnConfig.getUserinfo();
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
        map.put("code","0");
        map.put("msg","Okcoind的key不完整，请检查！");
        return map;
    }

}
