package com.feiniao.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.feiniao.com.vo.Result;
import com.feiniao.sys.entity.User;
import com.feiniao.sys.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author feiniao
 * @since 2023-07-24
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/findAll")
    public List<User> findAll() {
        return userService.list();
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        Map<String, Object> data = userService.login(user);
        if (data != null) {
            return Result.success(data);
        }
        return Result.fail(20002, "用户名或密码错误");
    }

    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo(@RequestParam("token") String token) {
        Map<String, Object> data = userService.getUserInfo(token);
        if (data != null) {
            return Result.success(data);
        }
        return Result.fail(20003, "用户信息获取失败");
    }

    @PostMapping("/logout")
    public Result<?> logout(@RequestHeader("X-Token") String token) {
        userService.logout(token);
        return Result.success("注销成功");
    }

    @GetMapping("/getUserList")
    public Result<Map<String, Object>> getUserList(@RequestParam(value = "username", required = false) String username,
                                                   @RequestParam(value = "phone", required = false) String phone,
                                                   @RequestParam("pageNo") Long pageNo,
                                                   @RequestParam("pageSize") Long pageSize) {

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasLength(username), User::getUsername, username);
        wrapper.eq(StringUtils.hasLength(phone), User::getPhone, phone);
//        降序
//        wrapper.orderByDesc(User::getId);


        Page<User> page = new Page<>(pageNo, pageSize);
        userService.page(page, wrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("total", page.getTotal());
        data.put("rows", page.getRecords());

        System.out.println(data.get("rows"));

        return Result.success(data);
    }

    @PostMapping("/addUser")
    public Result<?> addUser(@RequestBody User user) {
//        加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return Result.success("新增用户成功");
    }

    @PutMapping("/updateUser")
    public Result<?> updateUser(@RequestBody User user) {
//        此处设为空之后不会不会被更新
        user.setPassword(null);
        userService.updateById(user);

        return Result.success("修改用户成功");
    }

    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Integer id) {
        User user = userService.getById(id);
        return Result.success(user);
    }

    @DeleteMapping("/{id}")
    public Result<User> deleteUserById(@PathVariable Integer id) {
        userService.removeById(id);
        return Result.success("删除成功");
    }
}
