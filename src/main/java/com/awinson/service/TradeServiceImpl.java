package com.awinson.service;

import com.awinson.Entity.User;
import com.awinson.Entity.UserApi;
import com.awinson.Entity.UserTradeSetting;
import com.awinson.cache.CacheManager;
import com.awinson.config.BitvcCnBtcConfig;
import com.awinson.config.OkcoinCnBtcConfig;
import com.awinson.config.OkcoinCnConfig;
import com.awinson.dictionary.Dict;
import com.awinson.repository.UserRepository;
import com.awinson.repository.UserTradeSettingRepository;
import com.awinson.utils.HttpUtils;
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
    @Autowired
    private OkcoinCnBtcConfig okcoinCnBtcConfig;
    @Autowired
    private BitvcCnBtcConfig bitvcCnBtcConfig;

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

        //获取最新的用户资产，并更新到缓存，如果获取不了中断交易
        Map<String, Object> buyPlatfromAssets = userService.getUserAssetsInfo2CacheByPlatform(user, doBuyPlatform);
        Map<String, Object> sellPlatfromAssets = userService.getUserAssetsInfo2CacheByPlatform(user, doSellPlatform);

        if ("0".equals(buyPlatfromAssets.get("code")) || "0".equals(sellPlatfromAssets.get("code"))) {
            if ("0".equals(buyPlatfromAssets.get("code"))) {
                logger.info("用户:{},交易中断!做多平台{}访问异常,消息:{}", user.getUsername(), doBuyPlatform,buyPlatfromAssets.get("msg"));
                userService.addUserLog(user, Dict.LOGTYPE.ANALYSE, "交易中断!做多平台"+doBuyPlatform+"访问异常");
            }
            if ("0".equals(sellPlatfromAssets.get("code"))) {
                logger.info("用户:{},交易中断!做空平台{}访问异常,消息:{}", user.getUsername(), doSellPlatform,sellPlatfromAssets.get("msg"));
                userService.addUserLog(user, Dict.LOGTYPE.ANALYSE, "交易中断!做空平台"+doSellPlatform+"访问异常");
            }
            return;
        }

        if ("1".equals(buyPlatfromAssets.get("code")) && "1".equals(sellPlatfromAssets.get("code"))) {
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
                    logger.info("用户:{},交易中断!OkcoinCn资产在缓存中获取滞后{}", user.getUsername(), deltaTime);
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
                logger.info("用户:{},交易中断!okcoinCn资产在缓存中无法获取", user.getUsername());
                return;
            }

            //解析BitvcCN资产
            if (bitvcCn != null && bitvcCn.size() > 0) {
                String bitvcCnTimestamp = bitvcCn.get("timestamp").toString();
                Long deltaTime = Math.abs(Long.parseLong(bitvcCnTimestamp) - System.currentTimeMillis());
                if (deltaTime > 3000) {
                    //输出日志（交易中断，bitvcCn资产滞后）
                    userService.addUserLog(user, Dict.LOGTYPE.ANALYSE, "交易中断，bitvcCn资产在缓存中滞后");
                    logger.info("用户:{},交易中断!bitvcCn资产在缓存中获取滞后{}", user.getUsername(), deltaTime);
                    return;
                }
                Map<String, Object> bitvcCnResult = (Map<String, Object>) bitvcCn.get("result");
                bitvcCnAvailableCny = bitvcCnResult.get("available_cny").toString();
                bitvcCnAvailableBtc = bitvcCnResult.get("available_btc").toString();
                bitvcCnAvailableLtc = bitvcCnResult.get("available_ltc").toString();
            } else {
                userService.addUserLog(user, Dict.LOGTYPE.ANALYSE, "交易中断!bitvcCn资产在缓存中无法获取");
                logger.info("用户:{},交易中断!bitvcCn资产在缓存中无法获取", user.getUsername());
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
            Map<String, Object> priceMap = CacheManager.getCachesByType(Dict.TYPE.PRICE);
            String buyPrice = ((Map<String, Object>) priceMap.get(Dict.TYPE.PRICE + doBuyPlatform + coin + Dict.DIRECTION.SELL)).get("price").toString();

            //比较是否有足够的数量卖出,doSellPlatformCoinAmount(可用币数)和eachAmount(每次交易最小数量)比较
            if ((new BigDecimal(doSellPlatformCoinAmount)).compareTo(new BigDecimal(eachAmount)) < 0) {
                allOk = 0;
                doSellPlatformMsg = doSellPlatformName + ":没有足够的" + coinName + ",剩余:" + doSellPlatformCoinAmount + coinName + ",需要:" + eachAmount + coinName;
            } else {
                doSellPlatformMsg = doSellPlatformName + ":" + coinName + "充足,剩余:" + doSellPlatformCoinAmount;
            }

            //比较是否有足够的CNY买入,doBuyPlatformCoinAmount和eachAmount*(doBuyPlatformSellPriceOne)比较
            BigDecimal buyCny = new BigDecimal(eachAmount).multiply(new BigDecimal(buyPrice));   //购买最小单位需要的CNY
            if ((new BigDecimal(doBuyPlatformCoinAmount)).compareTo(buyCny) < 0) {
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
                Map<String, Object> doBuyMap = null;
                Map<String, Object> doSellMap = null;

                //先交易BITVC，如果BITVC交易失败则不继续交易
                try {
                    if (Dict.PLATFORM.BITVC_CN .equals(doBuyPlatform)){
                        doBuyMap = trade(Dict.ENABLE.YES, user, doBuyPlatform, coin, Dict.DIRECTION.BUY, eachAmount);
                        //失败则重试一遍
                        if ("0".equals(doBuyMap.get("code").toString())) {
                            userService.addUserLog(user, Dict.LOGTYPE.TRADE, "交易失败，重试![做多平台:" + doBuyPlatformName + "]" );
                            doBuyMap = trade(Dict.ENABLE.YES, user, doBuyPlatform, coin, Dict.DIRECTION.BUY, eachAmount);
                        }
                        //如果还是失败，则终止该次交易
                        if ("0".equals(doBuyMap.get("code").toString())){
                            userService.addUserLog(user, Dict.LOGTYPE.TRADE, "交易异常![做多平台:" + doBuyPlatformName + "]重试交易异常中断该次交易");
                            return;
                        }else if ("1".equals(doBuyMap.get("code").toString())){ //如果BITVC交易成功再进行其他平台的操作
                            doSellMap = trade(Dict.ENABLE.YES, user, doSellPlatform, coin, Dict.DIRECTION.SELL, eachAmount);
                            if ("0".equals(doSellMap.get("code").toString())) {
                                userService.addUserLog(user, Dict.LOGTYPE.TRADE, "交易失败，重试![做空平台:" + doSellPlatformName + "]" );
                                doSellMap = trade(Dict.ENABLE.YES, user, doSellPlatform, coin, Dict.DIRECTION.SELL, eachAmount);
                            }
                        }
                    }else if (Dict.PLATFORM.BITVC_CN .equals(doSellPlatform)){
                        doSellMap = trade(Dict.ENABLE.YES, user, doSellPlatform, coin, Dict.DIRECTION.SELL, eachAmount);
                        //失败则重试一遍
                        if ("0".equals(doSellMap.get("code").toString())) {
                            userService.addUserLog(user, Dict.LOGTYPE.TRADE, "交易失败，重试![做空平台:" + doSellPlatformName + "]" );
                            doSellMap = trade(Dict.ENABLE.YES, user, doSellPlatform, coin, Dict.DIRECTION.SELL, eachAmount);
                        }
                        //如果还是失败，则终止该次交易
                        if ("0".equals(doSellMap.get("code").toString())) {
                            userService.addUserLog(user, Dict.LOGTYPE.TRADE, "交易异常![做空平台:" + doSellPlatformName + "]重试交易异常中断该次交易");
                            return;
                        }else if ("1".equals(doSellMap.get("code").toString())){    //如果BITVC交易成功再进行其他平台的操作
                            doBuyMap = trade(Dict.ENABLE.YES, user, doBuyPlatform, coin, Dict.DIRECTION.BUY, eachAmount);
                            if ("0".equals(doBuyMap.get("code").toString())) {
                                userService.addUserLog(user, Dict.LOGTYPE.TRADE, "交易失败，重试![做多平台:" + doBuyPlatformName + "]" );
                                doBuyMap = trade(Dict.ENABLE.YES, user, doBuyPlatform, coin, Dict.DIRECTION.BUY, eachAmount);
                            }
                        }
                    }


                    //交易成功
                    if ("1".equals(doBuyMap.get("code").toString()) && "1".equals(doSellMap.get("code").toString())) {
                        userService.addUserLog(user, Dict.LOGTYPE.TRADE, "交易完成![币种:" + coin + "],[对冲数量:" + eachAmount + "]" + "[做空平台:" + doSellPlatform + "]" + "[做多平台:" + doBuyPlatform + "]");

                    } else {
                        //交易失败
                        String doBuyResult = "交易成功";
                        String doSellResult = "交易成功";
                        if ("0".equals(doBuyMap.get("code").toString())) {
                            doBuyResult = "交易失败";
                        }
                        if ("0".equals(doSellMap.get("code").toString())) {
                            doSellResult = "交易失败";
                        }
                        //输出交易失败日志日志
                        userService.addUserLog(user, Dict.LOGTYPE.TRADE, "交易失败![币种:" + coin + "],[对冲数量:" + eachAmount + "]" + "[做空平台:" + doSellPlatform + doSellResult + "]" + "[做多平台:" + doBuyPlatform + doBuyResult + "]");
                        userService.addUserLog(user, Dict.LOGTYPE.TRADE, "暂停所有自动交易");

                        //暂停所有自动交易
                        userService.updateUserTradeSettingAuto(Dict.ENABLE.NO, Dict.ENABLE.NO);
                    }
                } catch (IOException e) {
                    //输出异常日志
                    userService.addUserLog(user, Dict.LOGTYPE.TRADE, "交易异常![币种:" + coin + "],[对冲数量:" + eachAmount + "]" + "[做空平台:" + doSellPlatform + "]" + "[做多平台:" + doBuyPlatform + "]");
                    userService.addUserLog(user, Dict.LOGTYPE.TRADE, e.toString());
                    userService.addUserLog(user, Dict.LOGTYPE.TRADE, "暂停所有自动交易");
                    //暂停所有自动交易
                    userService.updateUserTradeSettingAuto(Dict.ENABLE.NO, Dict.ENABLE.NO);
                }
            } else if (allOk == 0) {
                // 输出日志（("交易中断!用户{},对冲分析:" + "{[做空]:" + doSellPlatformMsg + "}" + "{[做多]:" + doBuyPlatformMsg + "}");
                userService.addUserLog(user, Dict.LOGTYPE.ANALYSE, "交易中断! 对冲分析: [做空]:" + doSellPlatformMsg + "  [做多]:" + doBuyPlatformMsg);
                logger.info("用户:{},交易中断! 对冲分析: [做空]:{}  [做多]:{}", user.getUsername(), doSellPlatformMsg, doBuyPlatformMsg);
            }
        }
    }

    @Override
    public Map<String, Object> trade(String islippage, User user, String platform, String coin, String direction, String amount) throws IOException {
        if (Dict.DIRECTION.SELL.equals(direction)) {
            //如果是市价卖,则直接调用通用的tradeCommon
            return tradeCommon(user, platform, coin, direction, Dict.TRADE_TYPE.MARKET, amount, null);
        } else if (Dict.DIRECTION.BUY.equals(direction)) {
            //如果是市价卖,先获取最新的价格,根据购买的数量计算需要的CNY,再调用tradeCommon接口
            Map<String, Object> map = CacheManager.getCachesByType(Dict.TYPE.PRICE);            //获取最新价格
            if (map != null && map.size() > 0) {
                Map<String, Object> priceMap = (Map<String, Object>) map.get(Dict.TYPE.PRICE + platform + coin + Dict.DIRECTION.SELL);
                if (priceMap != null & priceMap.size() > 0) {

                    if (Dict.ENABLE.NO.equals(islippage)) {  //使用市价进行交易
                        BigDecimal price = new BigDecimal(priceMap.get("price").toString());
                        BigDecimal realAmount = price.multiply(new BigDecimal(amount));
                        realAmount = realAmount.setScale(2, BigDecimal.ROUND_HALF_UP);//保留两位小数
                        return tradeCommon(user, platform, coin, direction, Dict.TRADE_TYPE.MARKET, realAmount.toString(), null);

                    } else if (Dict.ENABLE.YES.equals(islippage)) {   //使用滑价进行交易
                        BigDecimal price = new BigDecimal(priceMap.get("price").toString());
                        BigDecimal slippage = null;
                        if (Dict.COIN.BTC.equals(coin)) {
                            slippage = new BigDecimal(0.5);
                        } else if (Dict.COIN.LTC.equals(coin)) {
                            slippage = new BigDecimal(0.01);
                        }
                        String realPrice = price.add(slippage).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                        return tradeCommon(user, platform, coin, direction, Dict.TRADE_TYPE.TAKER, amount, realPrice);
                    }
                }
            }
        }
        Map result = new HashMap();
        result.put("code", "0");
        result.put("msg", "缓存价格为空");
        return result;
    }


    @Override
    public Map<String, Object> tradeCommon(User user, String platform, String coin, String direction, String isMarketPrice, String amount, String price) throws IOException {
        //修正okcoin的bitvc的市价买入用的CNY数量
        if (Dict.PLATFORM.OKCOIN_CN.equals(platform) && Dict.TRADE_TYPE.MARKET.equals(isMarketPrice) && Dict.DIRECTION.BUY.equals(direction)) {
            price = amount;
            amount = null;
        }
        return tradeBase(user, platform, coin, direction, isMarketPrice, amount, price);
    }

    @Override
    public Map<String, Object> tradeBase(User user, String platform, String coin, String direction, String isMarketPrice, String amount, String price) throws IOException {
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
