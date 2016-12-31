package com.awinson.service;

import com.awinson.Entity.User;
import com.awinson.Entity.UserTradeSetting;
import com.awinson.cache.CacheManager;
import com.awinson.dictionary.Dict;
import com.awinson.repository.UserRepository;
import com.awinson.repository.UserTradeSettingRepository;
import com.awinson.utils.StringUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 10228 on 2016/12/27.
 */
@Service
public class TradeServiceImpl implements TradeService {

    private static final Logger logger = LoggerFactory.getLogger(TradeServiceImpl.class);
    @Autowired
    private OkcoinService okcoinService;

    @Autowired
    private BitvcService bitvcService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
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
    @Override
    @Async
    public void oneUserTrade(User user) {
        String userId = user.getId();
        UserTradeSetting userTradeSetting = userTradeSettingRepository.findByUserId(userId);

        if (userTradeSetting != null) {
            String autoTradeBtc = userTradeSetting.getAutoTradeBtc();
            String autoTradeLtc = userTradeSetting.getAutoTradeLtc();
            if ("1".equals(autoTradeBtc)) { //BTC
                judgeTrigger(user, userTradeSetting, Dict.COIN.BTC);
            }

            if ("1".equals(autoTradeLtc)) {  //LTC
                judgeTrigger(user, userTradeSetting, Dict.COIN.LTC);
            }
        }
    }

    /**
     * 判断是否会触发
     *
     * @param user
     * @param userTradeSetting
     * @param coin
     */
    @Override
    @Async
    public void judgeTrigger(User user, UserTradeSetting userTradeSetting, String coin) {
        //获取用户设置的阀值
        String marginJson = userTradeSetting.getMarginJson();
        if (marginJson != null & marginJson != "") {
            //获取缓存中的价差
            Map<String, Object> marginCacheMap = CacheManager.getCachesByType(Dict.TYPE.MARGIN);
            if (marginCacheMap != null && marginCacheMap.size() > 0) {
                //对比价差是否能触发用户设置的阀值
                Gson gson = new Gson();
                Map<String, String> marginMap = gson.fromJson(marginJson, Map.class);
                for (Map.Entry<String, String> entry : marginMap.entrySet()) {
                    String key = entry.getKey();
                    if (!key.endsWith(coin)) {
                        continue;
                    }
                    String marginCacheKey = Dict.TYPE.MARGIN + key.substring(1, key.length());

                    //缓存中的最新价差
                    Map<String, Object> marginCacheMapOne = (Map<String, Object>) marginCacheMap.get(marginCacheKey);
                    if (marginCacheMapOne != null && marginCacheMapOne.size() > 0) {  //缓存中是否有对应的margin
                        String deltaTimeStr = marginCacheMapOne.get("deltaTime").toString();
                        Integer deltaTime = Integer.valueOf(deltaTimeStr);

                        String marginCache = marginCacheMapOne.get("margin").toString();
                        BigDecimal cache = new BigDecimal(marginCache);

                        //用户设置的阀值
                        String marginSetting = entry.getValue();
                        BigDecimal setting = new BigDecimal(marginSetting);

                        //对比阀值与价差
                        if (cache.compareTo(setting) == 1 || cache.compareTo(setting) == 0) {
                            //阀值触发
                            //从KEY获取平台
                            if (deltaTime < 3000) {
                                String buyPlatform = key.substring(1, 3);
                                String sellPlatform = key.substring(3, 5);

                                Map<String, Object> logMap = new HashMap();
                                logMap.put("coin", coin);
                                logMap.put("sellPlatform", sellPlatform);
                                logMap.put("buyPlatform", buyPlatform);
                                logMap.put("margin", cache);
                                userService.addUserLog(user, Dict.LOGTYPE.THRESHOLD, gson.toJson(logMap));
                                logger.info("用户:{},阀值触发! 对冲策略: [币种]{},[做多平台]{},[做空平台]{},[margin]{}", user.getUsername(), coin, sellPlatform, buyPlatform, cache);
                                doTrade(user, coin, buyPlatform, sellPlatform);//注意，这里平台是和价格的相反
                            } else {
                                logger.info("用户:{},阀值触发!但延时太高:{}", user.getUsername(), deltaTimeStr);
                            }
                        }

                    }
                }
            }
        }
    }

    @Override
    @Async
    public void doTrade(User user, String coin, String doSellPlatform, String doBuyPlatform) {

        //获取用户交易设置
        UserTradeSetting userTradeSetting = userTradeSettingRepository.findByUserId(user.getId());
        String eachAmountBtc = userTradeSetting.getEachAmountBtc();
        String eachAmountLtc = userTradeSetting.getEachAmountLtc();
        String eachAmount = null;
        //判断最小对冲数策略是否可用
        if (Dict.COIN.BTC.equals(coin)) {
            if (StringUtil.isEmpty(eachAmountBtc)) {
                //输出日志（交易中断，没有设置BTC最小交易数量）
                userService.addUserLog(user, Dict.LOGTYPE.ANALYSE, "交易中断，没有设置BTC最小交易数量");
                return;
            }
            eachAmount = eachAmountBtc;
        } else if (Dict.COIN.LTC.equals(coin)) {
            if (StringUtil.isEmpty(eachAmountLtc)) {
                //输出日志（交易中断，没有设置LTC最小交易数量）
                userService.addUserLog(user, Dict.LOGTYPE.ANALYSE, "交易中断，没有设置LTC最小交易数量");
                return;
            }
            eachAmount = eachAmountLtc;
        }

        //logger.info("用户:{},阀值触发! 对冲策略: [币种]{},[做多平台]{},[做空平台]{},[每次交易量]{}", user.getUsername(), coin, doBuyPlatform, doSellPlatform, eachAmount);

        String okcoinCnAvailableBtc;
        String okcoinCnAvailableLtc;
        String okcoinCnAvailableCny;
        String bitvcCnAvailableCny;
        String bitvcCnAvailableBtc;
        String bitvcCnAvailableLtc;

        //获取用户资产
        Map<String, Object> okcoinCn = (Map<String, Object>) CacheManager.get(Dict.TYPE.ASSETS + Dict.PLATFORM.OKCOIN_CN + "_" + user.getId());
        Map<String, Object> bitvcCn = (Map<String, Object>) CacheManager.get(Dict.TYPE.ASSETS + Dict.PLATFORM.BITVC_CN + "_" + user.getId());

        //解析OkcoinCN资产
        if (okcoinCn != null && okcoinCn.size() > 0) {
            String okcoinCnTimestamp = okcoinCn.get("timestamp").toString();
            Long deltaTime = Math.abs(Long.parseLong(okcoinCnTimestamp) - System.currentTimeMillis());
            if (deltaTime > 3000) {
                //输出日志（交易中断，okcoinCn资产滞后）
                userService.addUserLog(user, Dict.LOGTYPE.ANALYSE, "交易中断，okcoinCn资产滞后");
                logger.info("用户:{},交易中断!OkcoinCn资产获取滞后{}", user.getUsername(), deltaTime);
                return;
            }
            Map<String, Object> okcoinCnResult = (Map<String, Object>) okcoinCn.get("result");
            Map<String, Object> okcoinCnInfo = (Map<String, Object>) okcoinCnResult.get("info");
            Map<String, Object> okcoinCnfunds = (Map<String, Object>) okcoinCnInfo.get("funds");
            Map<String, Object> okcoinCnfree = (Map<String, Object>) okcoinCnfunds.get("free");
            okcoinCnAvailableBtc = okcoinCnfree.get("btc").toString();
            okcoinCnAvailableLtc = okcoinCnfree.get("ltc").toString();
            okcoinCnAvailableCny = okcoinCnfree.get("cny").toString();
        } else {
            //输出日志（交易中断!okcoinCn资产无法获取）
            userService.addUserLog(user, Dict.LOGTYPE.ANALYSE, "交易中断!okcoinCn资产无法获取");
            logger.info("用户:{},交易中断!okcoinCn资产无法获取", user.getUsername());
            return;
        }

        //解析BitvcCN资产
        if (bitvcCn != null && bitvcCn.size() > 0) {
            String bitvcCnTimestamp = bitvcCn.get("timestamp").toString();
            Long deltaTime = Math.abs(Long.parseLong(bitvcCnTimestamp) - System.currentTimeMillis());
            if (deltaTime > 3000) {
                //输出日志（交易中断，bitvcCn资产滞后）
                userService.addUserLog(user, Dict.LOGTYPE.ANALYSE, "交易中断，bitvcCn资产滞后");
                logger.info("用户:{},交易中断!bitvcCn资产获取滞后{}", user.getUsername(), deltaTime);
                return;
            }
            Map<String, Object> bitvcCnResult = (Map<String, Object>) bitvcCn.get("result");
            bitvcCnAvailableCny = bitvcCnResult.get("available_cny").toString();
            bitvcCnAvailableBtc = bitvcCnResult.get("available_btc").toString();
            bitvcCnAvailableLtc = bitvcCnResult.get("available_ltc").toString();
        } else {
            //输出日志（交易中断!bitvcCn资产无法获取）
            userService.addUserLog(user, Dict.LOGTYPE.ANALYSE, "交易中断!bitvcCn资产无法获取");
            logger.info("用户:{},交易中断!bitvcCn资产无法获取", user.getUsername());
            return;
        }

        //分析用户资产
        String doSellPlatformCoinAmount = null;    //卖空平台的可用币数量
        String doBuyPlatformCoinAmount = null;    //买多平台的可用币数量

        if (Dict.PLATFORM.OKCOIN_CN.equals(doSellPlatform)) {
            if (Dict.COIN.BTC.equals(coin)) {
                doSellPlatformCoinAmount = okcoinCnAvailableBtc;
            } else if (Dict.COIN.LTC.equals(coin)) {
                doSellPlatformCoinAmount = okcoinCnAvailableLtc;
            }
        } else if (Dict.PLATFORM.OKCOIN_CN.equals(doBuyPlatform)) {
            doBuyPlatformCoinAmount = okcoinCnAvailableCny;
        }

        if (Dict.PLATFORM.BITVC_CN.equals(doSellPlatform)) {
            if (Dict.COIN.BTC.equals(coin)) {
                doSellPlatformCoinAmount = bitvcCnAvailableBtc;
            } else if (Dict.COIN.LTC.equals(coin)) {
                doSellPlatformCoinAmount = bitvcCnAvailableLtc;
            }
        } else if (Dict.PLATFORM.BITVC_CN.equals(doBuyPlatform)) {
            doBuyPlatformCoinAmount = bitvcCnAvailableCny;
        }

        //用于 0:为对冲中断 1:对冲流程正常
        int allOk = 1;
        String doSellPlatformMsg;
        String doBuyPlatformMsg;

        //准备日志的数据
        String coinName = Dict.translateDicName(Dict.DICTYPE.COIN, coin);
        String doSellPlatformName = Dict.translateDicName(Dict.DICTYPE.PLATFORM, doSellPlatform);
        String doBuyPlatformName = Dict.translateDicName(Dict.DICTYPE.PLATFORM, doBuyPlatform);

        //准备缓存中价格数据，用于判断是否有足够的CNY买入
        Map<String, Object> priceMap = CacheManager.getCachesByType("0");
        String buyPrice = ((Map<String, Object>) priceMap.get(Dict.TYPE.PRICE + doBuyPlatform + coin + Dict.DIRECTION.SELL)).get("price").toString();

        //比较是否有足够的数量卖出,doSellPlatformCoinAmount(可用币数)和eachAmount(每次交易最小数量)比较
        if ((new BigDecimal(doSellPlatformCoinAmount)).compareTo(new BigDecimal(eachAmount)) <= 0) {
            allOk = 0;
            doSellPlatformMsg = doSellPlatformName + ":没有足够的" + coinName + ",剩余:" + doSellPlatformCoinAmount + coinName + ",需要:" + eachAmount + coinName;
        } else {
            doSellPlatformMsg = doSellPlatformName + ":" + coinName + "充足,剩余:" + doSellPlatformCoinAmount;
        }

        //比较是否有足够的CNY买入,doBuyPlatformCoinAmount和eachAmount*(doBuyPlatformSellPriceOne)比较
        BigDecimal buyCny = new BigDecimal(eachAmount).multiply(new BigDecimal(buyPrice));   //购买最小单位需要的CNY
        if ((new BigDecimal(doBuyPlatformCoinAmount)).compareTo(buyCny) <= 0) {
            allOk = 0;
            doBuyPlatformMsg = doBuyPlatformName + ":没有足够CNY,剩余:" + doBuyPlatformCoinAmount + ",需要:" + buyCny + "CNY";
        } else {
            doBuyPlatformMsg = doBuyPlatformName + ":CNY充足,剩余:" + doBuyPlatformCoinAmount;
        }

        //对冲分析完成
        if (allOk == 1) {   //交易中断
            //输出日志（对冲分析完成）
            userService.addTradeLog(user, Dict.LOGTYPE.ANALYSE, "完成分析!  [做空]:" + doSellPlatformMsg + "  [做多]:" + doBuyPlatformMsg);
            logger.info("用户:{},完成分析!  [做空]:{}  [做多]:{}", user.getUsername(), doSellPlatformMsg, doBuyPlatformMsg);
            //TODO 执行对冲做多
            //TODO 执行对冲做空
            //TODO 及时修改缓存

        } else if (allOk == 0) {
            // 输出日志（("交易中断!用户{},对冲分析:" + "{[做空]:" + doSellPlatformMsg + "}" + "{[做多]:" + doBuyPlatformMsg + "}");
            userService.addUserLog(user, Dict.LOGTYPE.ANALYSE, "交易中断! 对冲分析: [做空]:" + doSellPlatformMsg + "  [做多]:" + doBuyPlatformMsg);
            logger.info("用户:{},交易中断! 对冲分析: [做空]:{}  [做多]:{}", user.getUsername(), doSellPlatformMsg, doBuyPlatformMsg);
        }
    }

    @Override
    public Map<String, Object> tradeCommon(User user, String platform, String coin, String direction, String isMarketPrice, String amount, String price) throws IOException {
        //修正okcoin的bitvc的市价买入用的CNY数量
        if (Dict.PLATFORM.OKCOIN_CN.equals(platform) && Dict.TRADE_TYPE.MARKET.equals(isMarketPrice) && Dict.DIRECTION.BUY.equals(direction)) {
            price = amount;
            amount = null;
        }
        return trade(user, platform, coin, direction, isMarketPrice, amount, price);
    }

    @Override
    public Map<String, Object> trade(User user, String platform, String coin, String direction, String isMarketPrice, String amount, String price) throws IOException {
        Map<String, Object> result = new HashMap();
        switch (platform) {
            case Dict.PLATFORM.OKCOIN_CN:
                result = okcoinService.trade(user, platform, coin, direction, isMarketPrice, amount, price);
                break;
            case Dict.PLATFORM.OKCOIN_UN:
                ;
                break;
            case Dict.PLATFORM.BITVC_CN:
                ;
                result = bitvcService.trade(user, platform, coin, direction, isMarketPrice, amount, price);
                break;
            case Dict.PLATFORM.BITVC_UN:
                ;
                break;
            default:
                ;
                break;
        }
        //TODO 检查该订单是否完成
        return result;
    }
}
