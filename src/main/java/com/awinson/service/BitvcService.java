package com.awinson.service;

import java.util.Map;

/**
 * Created by 10228 on 2016/12/13.
 */
public interface BitvcService {

    /**
     * 获取中国站或国际站现货账户信息
     * @param platform 10 中国站    11国际站
     * @return
     */
    Map<String,Object> getSpotUserinfo(String platform);


}
