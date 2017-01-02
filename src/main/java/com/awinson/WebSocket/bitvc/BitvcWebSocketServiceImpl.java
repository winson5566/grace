package com.awinson.WebSocket.bitvc;

import com.awinson.dictionary.Dict;
import com.awinson.service.PriceService;
import com.google.gson.Gson;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 10228 on 2016/12/19.
 */
@Service
public class BitvcWebSocketServiceImpl implements BitvcWebSocketService {
    private static final Logger logger = LoggerFactory.getLogger(BitvcWebSocketServiceImpl.class);

//    @PostConstruct
//    public void init() {
//        try {
//            bitvcInit();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public void bitvcInit() throws URISyntaxException {
        BlockingQueue<Object> values = new LinkedBlockingQueue<Object>();
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = true;
        Socket socket = IO.socket("hq.huobi.com:80", opts);
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                try {
                    socket.emit("request", new JSONObject("{'symbolId':'btccny','version':1,'msgType':'reqMarketDepthTop','requestIndex':"+ System.currentTimeMillis()+"}")
                    , new Ack() {
                        @Override
                        public void call(Object... args) {
                            values.offer(args[0]);
                        }
                    });

                } catch (JSONException e) {
                    throw new AssertionError(e);
                }
            }
        });
        socket.connect();
    }
}

