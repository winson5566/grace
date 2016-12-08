package com.awinson.dictionary;

/**
 * Created by winson on 2016/12/6.
 */
public class Dict {
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
    }
    public static class direction{
        public static final String buy = "0";     //买
        public static final String sell = "1";     //卖
    }

}
