import com.awinson.Entity.User;
import com.awinson.SpringBootStart;
import com.awinson.dictionary.Dict;
import com.awinson.repository.UserRepository;
import com.awinson.service.OkcoinService;
import com.awinson.service.TradeService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by 10228 on 2016/12/27.
 */
//这是JUnit的注解，通过这个注解让SpringJUnit4ClassRunner这个类提供Spring测试上下文。
@RunWith(SpringJUnit4ClassRunner.class)
//这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringBootTest(classes = SpringBootStart.class)
@WebAppConfiguration
public class TradeServiceTest {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private OkcoinService okcoinService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void TradeStartTest(){
        tradeService.tradeStart();
    }

    @Test
    public void okcoinTradeTest(){
        User user = userRepository.findByUsername("winson");
        try {
            Map<String, Object> map = tradeService.trade(user,Dict.Platform.OKCOIN_CN,Dict.Coin.LTC,Dict.direction.buy,Dict.TradeType.MARKET,"0.1","20.56");

            String result = map.get("result").toString();
            Gson gson = new Gson();
            Map<String, String> resultmMap =gson.fromJson(result, new TypeToken<Map<String, String>>() { }.getType());
            String orderId =resultmMap.get("order_id").toString();
            Map<String, Object> map2=(Map<String, Object>)okcoinService.order_info(user,Dict.Platform.OKCOIN_CN,Dict.Coin.LTC,orderId.toString());
            String result2 = map2.get("result").toString();
            List<Map> orders=gson.fromJson(result2, new TypeToken<List<Map>>() { }.getType());
            String status =orders.get(0).get("status").toString();
            System.out.print(status);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bitvcTradeTest(){
        User user = userRepository.findByUsername("winson");
        try {
//            //bitvc
//            //委托买
//            Map<String, Object> map10 = tradeService.trade(user,Dict.Platform.BITVC_CN,Dict.Coin.LTC,Dict.direction.buy,Dict.TradeType.TAKER,"0.1","20.56");
//            //委托卖
//            Map<String, Object> map11 = tradeService.trade(user,Dict.Platform.BITVC_CN,Dict.Coin.LTC,Dict.direction.sell,Dict.TradeType.TAKER,"0.11","80.56");
//            //市价买
//            Map<String, Object> map12 = tradeService.trade(user,Dict.Platform.BITVC_CN,Dict.Coin.LTC,Dict.direction.buy,Dict.TradeType.MARKET,"1",null);
//            //市价卖
//            Map<String, Object> map13 = tradeService.trade(user,Dict.Platform.BITVC_CN,Dict.Coin.LTC,Dict.direction.sell,Dict.TradeType.MARKET,"0.01",null);
//
//            //okcoin
//            //委托买
//            Map<String, Object> map00 = tradeService.trade(user,Dict.Platform.OKCOIN_CN,Dict.Coin.LTC,Dict.direction.buy,Dict.TradeType.TAKER,"0.1","20.56");
//            //委托卖
//            Map<String, Object> map01 = tradeService.trade(user,Dict.Platform.OKCOIN_CN,Dict.Coin.LTC,Dict.direction.sell,Dict.TradeType.TAKER,"0.11","80.56");
//            //市价买
//            Map<String, Object> map02 = tradeService.trade(user,Dict.Platform.OKCOIN_CN,Dict.Coin.LTC,Dict.direction.buy,Dict.TradeType.MARKET,null,"10");
//            //市价卖
            Map<String, Object> map03 = tradeService.trade(user,Dict.Platform.OKCOIN_CN,Dict.Coin.LTC,Dict.direction.sell,Dict.TradeType.MARKET,"0.1",null);
            System.out.print(map03);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
