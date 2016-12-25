package com.awinson.service;

import java.util.Map;

/**
 * Created by 10228 on 2016/12/13.
 */
public interface OkcoinService {

    /**
     * 获取中国站或国际站现货账户信息
     * @param platform 00 中国站    01国际站
     * @return
     */
    Map<String,Object> getSpotUserinfo(String platform);

    /**
     * 获取中国站或国际站现货账户信息
     * @param platform 00 中国站    01国际站
     * @param platform
     * @param apiKey
     * @param secretKey
     * @return
     */
    Map<String,Object> getSpotUserinfo(String platform,String apiKey,String secretKey);
}
