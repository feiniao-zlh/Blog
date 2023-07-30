package com.feiniao.sys.mapper;

import com.feiniao.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author feiniao
 * @since 2023-07-24
 */
public interface UserMapper extends BaseMapper<User> {
    List<String> getRoleNamesByUserId(Integer userId);

}
