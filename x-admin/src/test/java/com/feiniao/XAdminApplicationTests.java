package com.feiniao;

import com.feiniao.sys.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@SpringBootTest
class XAdminApplicationTests {

    @Resource
    private UserMapper userMapper;

    @Test
    void testMapper() {

    }

}
