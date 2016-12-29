package com.awinson.dictionary;

/**
 * Created by winson on 2016/12/6.
 */
public class Dict {
    public static class Enable{
        public static final String NO = "0";    //不可用
        public static final String YES = "1";    //可用
    }
    public static class Coin {
        public static final String BTC = "0";    //比特币
        public static final String LTC = "1";    //莱特币
    }
    public static class Platform {
        public static final String OKCOIN_CN = "00";     //Okcoin中国站
        public static final String OKCOIN_UN = "01";     //Okcoin国际站
        public static final String BITVC_CN = "10";      //Bitvc中国站
        public static final String BITVC_UN = "11";      //Bitvc国际站
    }
    public static class Type{
        public static final String price = "0";     //价格
        public static final String margin = "1";     //价差
        public static final String ASSETS = "2";     //账户信息
        public static final String SETTING = "s";     //交易设置
        public static final String LOG = "l";     //日志

    }
    public static class TradeType{
        public static final String TAKER = "0";     //委托
        public static final String MARKET = "1";     //市价
    }
    public static class direction{
        public static final String buy = "0";     //买
        public static final String sell = "1";     //卖
        public static final String last = "2";     //last
    }
    public static class key{
        public static final String api = "0";     //apiKey
        public static final String secret = "1";     //secretKey
    }
    public static class DICTYPE{
        public static final String COIN = "0";
        public static final String PLATFORM = "1";
    }

    public static class LOGTYPE{
        public static final String USER = "0";
        public static final String THRESHOLD = "1";
        public static final String TRADE = "2";
    }

    //字典code转换Name
    public static String translateDicName(String type, String code) {
        if (Dict.DICTYPE.COIN.equals(type)) {
            switch (code) {
                case Dict.Coin.BTC:
                    return ("BTC");
                case Dict.Coin.LTC:
                    return ("LTC");
            }
        } else if (Dict.DICTYPE.PLATFORM.equals(type)) {
            switch (code) {
                case Dict.Platform.OKCOIN_CN:
                    return ("Okcoin中国站");
                case Dict.Platform.OKCOIN_UN:
                    return ("Okcoin国际站");
                case Dict.Platform.BITVC_CN:
                    return ("Bitvc中国站");
                case Dict.Platform.BITVC_UN:
                    return ("Bitvc国际站");
                default:break;
            }
        }
        return "DIC_CODE不存在";
    }


}
