package com.awinson.service;

import com.awinson.Entity.User;
import com.awinson.Entity.UserTradeSetting;
import com.awinson.cache.CacheManager;
import com.awinson.dictionary.Dict;
import com.awinson.repository.UserRepository;
import com.awinson.repository.UserTradeSettingRepository;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by 10228 on 2016/12/27.
 */
@Service
public class TradeServiceImpl implements TradeService {

    private static final Logger logger = LoggerFactory.getLogger(TradeServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTradeSettingRepository userTradeSettingRepository;


    @Override
    public void tradeStart() {
        //获取开启了BTC或LTC自动交易的所有用户
        List<User> list = userRepository.findByEnable("1");
        for (User user : list) {
            oneUserTrade(user);
        }
    }

    /**
     * 单个可用用户的自动交易流程
     *
     * @param user
     */
    @Async
    public void oneUserTrade(User user) {
        String userId = user.getId();
        UserTradeSetting userTradeSetting = userTradeSettingRepository.findByUserId(userId);

        if (userTradeSetting != null) {
            String autoTradeBtc = userTradeSetting.getAutoTradeBtc();
            String autoTradeLtc = userTradeSetting.getAutoTradeLtc();
            if ("1".equals(autoTradeBtc)) { //BTC
                judgeTrigger(user, userTradeSetting, Dict.Coin.BTC);
            }

            if ("1".equals(autoTradeLtc)) {  //LTC
                judgeTrigger(user, userTradeSetting, Dict.Coin.LTC);
            }
        }
    }

    /**
     * 判断是否会触发
     * @param user
     * @param userTradeSetting
     * @param coin
     */
    @Async
    public void judgeTrigger(User user, UserTradeSetting userTradeSetting, String coin) {
        //获取用户设置的阀值
        String marginJson = userTradeSetting.getMarginJson();
        if (marginJson != null & marginJson != "") {
            //获取缓存中的价差
            Map<String, Object> marginCacheMap = CacheManager.getCachesByType(Dict.Type.margin);
            if (marginCacheMap != null && marginCacheMap.size() > 0) {
                //对比价差是否能触发用户设置的阀值
                Gson gson = new Gson();
                Map<String, String> marginMap = gson.fromJson(marginJson, Map.class);
                for (Map.Entry<String, String> entry : marginMap.entrySet()) {
                    String key = entry.getKey();
                    if (!key.endsWith(coin)) {
                        continue;
                    }
                    String marginCacheKey = Dict.Type.margin + key.substring(1, key.length());

                    //缓存中的最新价差
                    Map<String, Object> marginCacheMapOne = (Map<String, Object>) marginCacheMap.get(marginCacheKey);
                    if (marginCacheMapOne != null && marginCacheMapOne.size() > 0) {  //缓存中是否有对应的margin
                        String marginCache = marginCacheMapOne.get("margin").toString();
                        BigDecimal cache = new BigDecimal(marginCache);

                        //用户设置的阀值
                        String marginSetting = entry.getValue();
                        BigDecimal setting = new BigDecimal(marginSetting);

                        //对比阀值与价差
                        if (cache.compareTo(setting) == 1 || cache.compareTo(setting) == 0) {
                            //阀值触发
                            //从KEY获取平台
                            String buyPlatform = key.substring(1, 3);
                            String sellPlatform = key.substring(3, 5);
                            logger.info("用户:{},阀值触发!对冲策略：币种:{},做多平台:{},做空平台:{}", user.getUsername(),coin,sellPlatform, buyPlatform);
                            doTrade(user.getId(), coin, buyPlatform, sellPlatform);//注意，这里平台是和价格的相反
                        }
                    }
                }
            }
        }
    }

    /**
     * 已根据策略触发用户的对冲交易
     *
     * @param userId         用户id
     * @param coin           币种
     * @param doSellPlatform 执行做空的平台
     * @param doBuyPlatform  执行做多的平台
     */
    @Async
    public void doTrade(String userId, String coin, String doSellPlatform, String doBuyPlatform) {
        //TODO 交易前的准备工作
        //TODO 获取最小对冲数策略
        //TODO 查询用户资产，判断是否适合对冲
        //TODO 执行对冲
        //TODO 及时修改缓存
    }
}
