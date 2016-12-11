package com.awinson.service;

import com.awinson.Entity.User;
import com.awinson.Entity.UserRole;
import com.awinson.repository.UserRepository;
import com.awinson.repository.UserRoleRepository;
import com.awinson.valid.RegisterValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 10228 on 2016/12/11.
 */
@Service
public class UserServiceImpl implements UserService{


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public Map<String, Object> register(RegisterValid registerValid) {
        Map<String,Object> map = new HashMap();
        if (userRepository.findByUsername(registerValid.getUsername()) != null) {
            map.put("code","0");
            map.put("msg","该用户名已存在");
        }else {
            User user = new User();
            String userId= UUID.randomUUID().toString();
            user.setId(userId);
            user.setUsername(registerValid.getUsername());

            //盐值
            String salt = UUID.randomUUID().toString();
            user.setSalt(salt);
            String password = registerValid.getPassword();
            user.setPassword(password);

            user.setEnable("1");
            userRepository.save(user);

            //保存权限
            UserRole userRole = new UserRole();
            userRole.setId(UUID.randomUUID().toString());
            userRole.setUserId(userId);
            userRole.setRoleId("00024bd8-dfdf-4dbf-8a76-1507008fe402");
            userRoleRepository.save(userRole);

            map.put("code","1");
            map.put("msg","注册成功");
        }
        return map;
    }
}
