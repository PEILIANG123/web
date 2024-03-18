package com.example.web.controller;

import com.example.web.pojo.Result;
import com.example.web.pojo.User;
import com.example.web.service.UserService;
import com.example.web.utils.JwtUtil;
import com.example.web.utils.Md5Util;
import com.example.web.utils.ThreadLocalUtil;

import jakarta.annotation.Resource;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Validated
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {

        User u = userService.findByUserName(username);
        if (u == null) {
            System.out.println(username+password);
            userService.register(username, password);
            return Result.success();
        } else {
            return Result.error("用户名已经被占用!");
        }
    }
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password){
        User loginUser=userService.findByUserName(username);

        if (loginUser==null){
            return Result.error("用户名错误!");
        }
        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())){
            Map<String,Object> claims=new HashMap<>();
            claims.put("id",loginUser.getId());
            claims.put("username",loginUser.getUsername());
            String token = JwtUtil.genToken(claims);
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,1, TimeUnit.HOURS);
            return Result.success(token);
        }
        return Result.error("密码错误!");
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(){
//        @RequestHeader(name="Authorization") String token
//        Map<String, Object> map = JwtUtil.parseToken(token);
        Map<String,Object> map=ThreadLocalUtil.get();
        String username = (String) map.get("username");
//        Object username = map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }
    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user){
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params,@RequestHeader("Authorization") String token) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//        校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        //判断是否为空
        if (!StringUtils.hasLength(oldPwd) ||
                !StringUtils.hasLength(newPwd) ||
                !StringUtils.hasLength(rePwd)
        ) {
           return Result.error("缺少必要参数!");
        }
        //判断原密码是否正确
        if (!user.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
           return Result.error("原密码错误!");
        }
        //判断新密码是否一样
        if (!rePwd.equals(newPwd)) {
           return Result.error("两次新密码输入不一样!");
        }
        //调用service完成密码更新
        userService.updatePwd(newPwd);
        operations.getOperations().delete(token);
        return Result.success();
    }
}
