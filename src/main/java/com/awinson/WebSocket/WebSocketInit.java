package com.awinson.WebSocket;

import com.awinson.okcoin.WebSocketService;
import com.awinson.okcoin.WebSoketClient;
import com.google.gson.Gson;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 10228 on 2016/12/24.
 */
@Service
public class WebSocketInit {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketInit.class);

    @Autowired
    private WebSocketService webSocketService;

    @PostConstruct
    public void init() {
//        try {
//            huobiInit();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        okcoinInit();
    }

    private void okcoinInit() {
        String apiKey = "653e8232-ab6e-4523-9310-514d8f2d8f13";
        String secretKey = "9E438027B165666CBA74DED24D6F3C9D";
        String url = "wss://real.okcoin.cn:10440/websocket/okcoinapi";   // 国内站WebSocket地址
        WebSoketClient client = new WebSoketClient(url, webSocketService);  // WebSocket客户端
        client.start();  // 启动客户端
        // 添加订阅
        client.addChannel("ok_sub_spotcny_btc_ticker");
        client.addChannel("ok_sub_spotcny_ltc_ticker");

        // 获取用户信息
        //client.getUserInfo(apiKey,secretKey);

        // 删除定订阅
        // client.removeChannel("ok_sub_spotusd_btc_ticker");

        // 合约下单交易
        // client.futureTrade(apiKey, secretKey, "btc_usd", "this_week", 2.3, 2,
        // 1, 0, 10);

        // 实时交易数据 apiKey
        // client.futureRealtrades(apiKey, secretKey);

        // 取消合约交易
        // client.cancelFutureOrder(apiKey, secretKey, "btc_usd", 123456L,
        // "this_week");

        // 现货下单交易
        // client.spotTrade(apiKey, secretKey, "btc_usd", 3.2, 2.3, "buy");

        // 现货交易数据
        // client.realTrades(apiKey, secretKey);

        // 现货取消订单
        // client.cancelOrder(apiKey, secretKey, "btc_usd", 123L);
    }

    private void huobiInit() throws URISyntaxException, InterruptedException {
        final BlockingQueue<Object> values = new LinkedBlockingQueue<Object>();
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = true;
        Socket socket = IO.socket("hq.huobi.com:80", opts);
        String datetime = String.valueOf(System.currentTimeMillis());
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                logger.info("EVENT_CONNECT");
                String strMsg = "{\"symbolId\":\"btccny\",\"version\":1,\"msgType\":\"reqMarketDepthTop\",\"requestIndex\":"+datetime+"}";
                JSONObject jsonObject = new JSONObject(strMsg);
                socket.emit("request",jsonObject);
            }
        }).on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                values.offer(args);
            }
        });
        socket.connect();

        Object[] object = (Object[])values.take();
        logger.info(object.toString());
    }
}
