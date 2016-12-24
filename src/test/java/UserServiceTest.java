import com.awinson.Entity.UserApi;
import com.awinson.SpringBootStart;
import com.awinson.repository.UserApiRepository;
import com.awinson.repository.UserRepository;
import com.awinson.service.UserService;
import com.awinson.okcoin.WebSocketServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

/**
 * Created by 10228 on 2016/12/11.
 */
//这是JUnit的注解，通过这个注解让SpringJUnit4ClassRunner这个类提供Spring测试上下文。
@RunWith(SpringJUnit4ClassRunner.class)
//这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringBootTest(classes = SpringBootStart.class)
@WebAppConfiguration
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserApiRepository userApiRepository;

    @Autowired
    private  UserService userService;

    @Autowired
    private WebSocketServiceImpl greetingService;
    @Test
    public void savaApiKeyTest(){
        UserApi userApi = new UserApi();
        userApi.setId(UUID.randomUUID().toString());
        userApi.setApi("123");
        userApi.setUserId("123");
        userApi.setApiType("0");
        userApi.setPlatform("00");
        userApiRepository.save(userApi);
    }

//    @Test
//    public void getUserAllApiTest(){
//        List<UserApi> list = userService.getUserAllApi();
//        for (UserApi userApi: list) {
//            System.out.println(userApi.getApi());
//        }
//    }

}
