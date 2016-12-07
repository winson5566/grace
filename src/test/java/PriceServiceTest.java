import com.awinson.SpringBootStart;
import com.awinson.config.Dict;
import com.awinson.service.PriceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by winson on 2016/12/6.
 */
//这是JUnit的注解，通过这个注解让SpringJUnit4ClassRunner这个类提供Spring测试上下文。
@RunWith(SpringJUnit4ClassRunner.class)
//这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringBootTest(classes = SpringBootStart.class)
@WebAppConfiguration
public class PriceServiceTest {
    @Autowired
    private PriceService priceService;

    /**
     * OkcoinBtc价格获取测试用例
     */
    @Test
    public void getDepthTest000(){
        Map<String,BigDecimal> map = priceService.getDepth(Dict.Platform.OKCOIN_CN,Dict.Coin.BTC);
    }

    /**
     * OkcoinLtc价格获取测试用例
     */
    @Test
    public void getDepthTest001(){
        Map<String,BigDecimal> map = priceService.getDepth(Dict.Platform.OKCOIN_CN,Dict.Coin.BTC);
    }

    /**
     * BitvcBtc价格获取测试用例
     */
    @Test
    public void getDepthTest100(){
        Map<String,BigDecimal> map = priceService.getDepth(Dict.Platform.OKCOIN_CN,Dict.Coin.BTC);
    }

    /**
     * BitvcLtc价格获取测试用例
     */
    @Test
    public void getDepthTest101(){
        Map<String,BigDecimal> map = priceService.getDepth(Dict.Platform.OKCOIN_CN,Dict.Coin.BTC);
    }

    /**
     * 计算价差
     */
    @Test
    public void calculationMargin(){
        priceService.calculationMargin();
    }
}
