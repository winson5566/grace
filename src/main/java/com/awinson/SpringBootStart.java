package com.awinson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Created by winson on 16/11/9.
 */

@SpringBootApplication
public class SpringBootStart {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootStart.class, args);
    }
}
