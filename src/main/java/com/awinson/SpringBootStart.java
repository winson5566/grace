package com.awinson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by winson on 16/11/9.
 */
//@Configuration//配置控制
//@ComponentScan//组件扫描
//@EnableAutoConfiguration//启用自动配置
@SpringBootApplication //相当于上面三个
public class SpringBootStart {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootStart.class, args);
    }
}
