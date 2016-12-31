package com.awinson;

import com.awinson.dictionary.Dict;

/**
 * Created by winson on 2016/12/31.
 */
public class Backup {

//    /**
//     * 获取Okcoin中国站资产信息
//     * @return
//     */
//    @RequestMapping("getOkcoinAssets")
//    @ResponseBody
//    public Map<String, Object> getOkcoinAssets() {
//        //API获取
//        //Map<String, Object> map = okcoinService.getSpotUserinfo(Dict.PLATFORM.OKCOIN_CN);
//        //从缓存获取
//        Map<String, Object> map =(Map<String, Object>)CacheManager.get(Dict.TYPE.ASSETS+Dict.PLATFORM.OKCOIN_CN+"_"+userService.getUserId());
//        return map;
//    }
//
//    /**
//     * 获取Bitvc资产信息
//     * @return
//     */
//    @RequestMapping("getBitvcAssets")
//    @ResponseBody
//    public Map<String, Object> getBitvcAssets() {
//        //API获取
//        //Map<String, Object> map = bitvcService.getSpotUserinfo(Dict.PLATFORM.BITVC_CN);
//        //从缓存获取
//        Map<String, Object> map =(Map<String, Object>)CacheManager.get(Dict.TYPE.ASSETS+Dict.PLATFORM.BITVC_CN+"_"+userService.getUserId());
//        return map;
//    }

//    @RequestMapping("account")
//    public String accoutInfo(Model model) {
//        Map<String, Object> map = new HashMap();
//        //获取Okcoin中国站资产信息
//        Map<String, Object> okcoinCn = okcoinService.getSpotUserinfo(Dict.PLATFORM.OKCOIN_CN);
//        okcoinCn.put("platform", "Okcoin中国站");
//        map.put("00",okcoinCn);
//
//        //获取Okcoin国际站资产信息
//        Map<String, Object> okcoinUn = okcoinService.getSpotUserinfo(Dict.PLATFORM.OKCOIN_UN);
//        okcoinUn.put("platform", "Okcoin国际站");
//        map.put("01",okcoinUn);
//
//        //获取Bitvc资产信息
//        Map<String, Object> bitvcUn = bitvcService.getSpotUserinfo(Dict.PLATFORM.BITVC_CN);
//        bitvcUn.put("platform", "Bitvc中国站");
//        map.put("10",bitvcUn);
//
//        model.addAttribute("map", map);
//
//        return "user/account";
//    }

//    /**
//     * 获取价格和价差的接口
//     * @param model
//     * @return
//     */
//    @RequestMapping("price")
//    public String priceInfo(Model model){
//        Map<String, Object> map = new HashMap();
//        Map<String, Object> priceMap = CacheManager.getCachesByType(Dict.TYPE.PRICE);
//        Map<String, Object> marginMap = CacheManager.getCachesByType(Dict.TYPE.MARGIN);
//
//        Map<String,Object> priceMapNew = new HashMap();
//        for(Map.Entry<String,Object> entry : priceMap.entrySet()){
//            priceMapNew.put("p"+entry.getKey(),entry.getValue());
//        }
//
//        Map<String,Object> marginMapNew = new HashMap();
//        for(Map.Entry<String,Object> entry : marginMap.entrySet()){
//            marginMapNew.put("m"+entry.getKey(),entry.getValue());
//        }
//
//        map.put("priceMap",priceMapNew);
//        map.put("marginMap",marginMapNew);
//        model.addAttribute("map", map);
//        return "user/price";
//    }

//    /**
//     * 获取监控日志
//     * @return
//     */
//    @RequestMapping("getThresholdLog")
//    @ResponseBody
//    public String getThresholdLog(){
//        List<UserLog> thresholdList  = userService.getUserLog(Dict.LOGTYPE.THRESHOLD);
//        Gson gson  = new Gson();
//        return gson.toJson(thresholdList);
//    }
//
//    /**
//     * 获取交易实时日志
//     * @return
//     */
//    @RequestMapping("getTradeLog")
//    @ResponseBody
//    public String getTradeLog(){
//        List<UserLog> tradeList  = userService.getUserLog(Dict.LOGTYPE.TRADE);
//        Gson gson  = new Gson();
//        return gson.toJson(tradeList);
//    }
//
//    /**
//     * 获取交易成功日志
//     * @return
//     */
//    @RequestMapping("getTradeSuccessLog")
//    @ResponseBody
//    public String getTradeSuccessLog(){
//        List<UserLog> tradeList  = userService.getTradeSuccessLog(Dict.LOGTYPE.TRADE);
//        Gson gson  = new Gson();
//        return gson.toJson(tradeList);
//    }


}
