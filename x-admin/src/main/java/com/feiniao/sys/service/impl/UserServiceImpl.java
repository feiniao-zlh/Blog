package com.feiniao.sys.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.feiniao.sys.entity.User;
import com.feiniao.sys.mapper.UserMapper;
import com.feiniao.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author feiniao
 * @since 2023-07-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Override
//    public Map<String, Object> login(User user) {
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
//        wrapper.eq(User::getUsername, user.getUsername());
//        User loginUser = this.getOne(wrapper);
////&& passwordEncoder.matches(user.getPassword(),loginUser.getPassword())
//        if (loginUser != null ) {
//            Map<String, Object> data = new HashMap<>();
//            String key = "user::" + UUID.randomUUID();
//            data.put("token", key);    // 待优化，最终方案jwt
//            loginUser.setPassword(null);
//            redisTemplate.opsForValue().set(key, loginUser, 30, TimeUnit.MINUTES);
//            return data;
//        }
//        return null;
//    }

    @Override
    public Map<String, Object> login(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
        wrapper.eq(User::getUsername, user.getUsername());
        wrapper.eq(User::getPassword, user.getPassword());
        User loginUser = this.getOne(wrapper);

        if (loginUser != null) {
            Map<String, Object> data = new HashMap<>();
            String key = "user::" + UUID.randomUUID();
            data.put("token", key);    // 待优化，最终方案jwt
            loginUser.setPassword(null);
            redisTemplate.opsForValue().set(key, loginUser, 30, TimeUnit.MINUTES);
            return data;
        }
        return null;
    }

    @Override
    public Map<String, Object> getUserInfo(String token) {
        Object obj = redisTemplate.opsForValue().get(token);
        if (obj != null) {
            // 反序列化
            User user = JSON.parseObject(JSON.toJSONString(obj), User.class);
            Map<String, Object> data = new HashMap<>();
            data.put("name", user.getUsername());
            data.put("avatar", user.getAvatar());

            List<String> roleList = this.baseMapper.getRoleNamesByUserId(user.getId());
            data.put("roles", roleList);
            return data;
        }

        return null;
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(token);
    }

}
