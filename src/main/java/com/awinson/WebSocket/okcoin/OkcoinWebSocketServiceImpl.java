package com.awinson.WebSocket.okcoin;

import com.awinson.WebSocket.web.WebSocketMessage;
import com.awinson.service.PriceService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by 10228 on 2016/12/19.
 */
@Service
public class OkcoinWebSocketServiceImpl implements OkcoinWebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(OkcoinWebSocketServiceImpl.class);

    @Autowired
    private SimpMessagingTemplate messaging;

    @Autowired
    private PriceService priceService;

    public void broadcast(String topic, String context) {
        WebSocketMessage message = new WebSocketMessage(context);
        messaging.convertAndSend("/topic/" + topic, message);
    }


    @Override
    public void onReceive(String msg) {

//        logger.info("WebSocket Client" +
//                " received message: " + msg);
        Gson gson = new Gson();
        if (msg.indexOf("event")<0) {
            List<Map> list = gson.fromJson(msg, List.class);
            for (Map map : list) {
                if (map.containsKey("data")) {
                    String channel = map.get("channel").toString();
                    Map data = (Map) map.get("data");
                    String buy = data.get("buy").toString();
                    String sell = data.get("sell").toString();
                    String last = data.get("last").toString();
                    BigDecimal buyPrice = new BigDecimal(Double.parseDouble(buy));
                    BigDecimal sellPrice = new BigDecimal(Double.parseDouble(sell));
                    BigDecimal lastPrice = new BigDecimal(Double.parseDouble(last));

                    String timestamp = data.get("timestamp").toString();
                    String coinType = null;
                    if (("ok_sub_spotcny_btc_ticker").equals(channel)) {
                        coinType = "0";
                    } else if (("ok_sub_spotcny_ltc_ticker").equals(channel)) {
                        coinType = "1";
                    }
                    //写入缓存
                    priceService.savePrice2Cache("00", coinType, sellPrice, buyPrice,lastPrice,timestamp);

                    //保存如数据库
                    priceService.savePrice2DB("00", coinType, sellPrice, buyPrice,lastPrice,timestamp);
                }
            }
        }
    }
}
