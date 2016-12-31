package com.awinson.dictionary;

/**
 * Created by winson on 2016/12/6.
 */
public class Dict {
    public static class ENABLE{
        public static final String NO = "0";    //不可用
        public static final String YES = "1";    //可用
    }
    public static class COIN {
        public static final String BTC = "0";    //比特币
        public static final String LTC = "1";    //莱特币
    }
    public static class PLATFORM {
        public static final String OKCOIN_CN = "00";     //Okcoin中国站
        public static final String OKCOIN_UN = "01";     //Okcoin国际站
        public static final String BITVC_CN = "10";      //Bitvc中国站
        public static final String BITVC_UN = "11";      //Bitvc国际站
    }
    public static class TYPE{
        public static final String PRICE = "P";     //价格
        public static final String MARGIN = "M";     //价差
        public static final String ASSETS = "2";     //账户信息
        public static final String SETTING = "s";     //交易设置
        public static final String LOG = "l";     //日志

    }
    public static class TRADE_TYPE{
        public static final String TAKER = "0";     //委托
        public static final String MARKET = "1";     //市价
    }
    public static class DIRECTION{
        public static final String BUY = "0";     //买
        public static final String SELL = "1";     //卖
        public static final String LAST = "2";     //last
    }
    public static class KEY{
        public static final String API = "0";     //apiKey
        public static final String SECRET = "1";     //secretKey
    }
    public static class DICTYPE{
        public static final String COIN = "0";
        public static final String PLATFORM = "1";
    }

    public static class LOGTYPE{
        public static final String USER = "0";
        public static final String THRESHOLD = "1";
        public static final String ANALYSE = "2";
        public static final String TRADE = "3";
    }

    public static class QUEUE{
        public static final String PRICE = "/price";
        public static final String ASSETS = "/assets";
        public static final String LOG = "/log";
        public static final String SETTING = "/setting";
    }

    //字典code转换Name
    public static String translateDicName(String type, String code) {
        if (Dict.DICTYPE.COIN.equals(type)) {
            switch (code) {
                case Dict.COIN.BTC:
                    return ("BTC");
                case Dict.COIN.LTC:
                    return ("LTC");
            }
        } else if (Dict.DICTYPE.PLATFORM.equals(type)) {
            switch (code) {
                case Dict.PLATFORM.OKCOIN_CN:
                    return ("Okcoin中国站");
                case Dict.PLATFORM.OKCOIN_UN:
                    return ("Okcoin国际站");
                case Dict.PLATFORM.BITVC_CN:
                    return ("Bitvc中国站");
                case Dict.PLATFORM.BITVC_UN:
                    return ("Bitvc国际站");
                default:break;
            }
        }
        return "DIC_CODE不存在";
    }


}
