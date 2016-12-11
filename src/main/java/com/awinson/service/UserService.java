package com.awinson.service;

import com.awinson.valid.RegisterValid;

import java.util.Map;
import java.util.Objects;

/**
 * Created by 10228 on 2016/12/11.
 */
public interface UserService {
    /**
     * 注册
     * @param registerValid
     * @return
     */
    Map<String,Object> register(RegisterValid registerValid);
}
