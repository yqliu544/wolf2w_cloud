package cn.wolfcode.wolf2w.user.controller;

import cn.wolfcode.wolf2w.auth.anno.RequireLogin;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import cn.wolfcode.wolf2w.user.dto.UserInfoDTO;
import cn.wolfcode.wolf2w.user.service.UserInfoService;
import cn.wolfcode.wolf2w.user.domain.UserInfo;
import cn.wolfcode.wolf2w.user.vo.RegisterRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Api(value = "用户模块")
@RestController
@RequestMapping("/users")
public class UserInfoController {
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping
    public List<UserInfo> all() {
        return userInfoService.list();
    }

    @GetMapping("/phone/exists")
    public R<Boolean> checkPhoneExists(String phone) {
        return R.ok(userInfoService.findByPhone(phone)!=null);
    }


    @PostMapping("/register")
    public R<?> register(RegisterRequest registerRequest){
        userInfoService.register(registerRequest);
        return R.ok();
    }
    @ApiOperation(value = "登录功能",notes = "基于jwt实现用户登录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username",value = "用户名"),
            @ApiImplicitParam(name = "password",value = "密码")
    })
    @ApiResponses(value = {
            @ApiResponse(code=200,message = "登录成功"),
            @ApiResponse(code=401,message = "用户名或密码错误")
    })
    @PostMapping("/login")
    public R<Map<String,Object>> login(String username,String password){
        Map<String,Object> map=userInfoService.login(username,password);
        return R.ok(map);
    }

    @GetMapping("/getById")
    public R<UserInfoDTO> getById(Long id){
        UserInfoDTO userInfo = userInfoService.getDtoById(id);
        return R.ok(userInfo);
    }

    @GetMapping("/favor/strategies")
    public R<List<Long>> getFavorStrategyIdList(Long userId){
        List<Long> list=userInfoService.getFavorStrategyIdList(userId);
        return R.ok(list);
    }
    @RequireLogin
    @PostMapping("/favor/strategies")
    public R<Boolean> favoriteStrategy(Long sid){
        boolean ret=userInfoService.favoriteStrategy(sid);
        return  R.ok(ret);
    }

    @GetMapping
    public R<List<UserInfoDTO>> findList(Integer current,Integer limit){
        int offset=(current-1)*limit;
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<UserInfo>().last("limit " + offset + ", " + limit);
        List<UserInfoDTO> collect = userInfoService.list(wrapper).stream().map(UserInfo::toDto).collect(Collectors.toList());
        return R.ok(collect);
    }

    @GetMapping("/findByDestName")
    public R<List<UserInfo>> findUserByDestName(@RequestParam String destName){
        return R.ok(userInfoService.list(new QueryWrapper<UserInfo>().eq("city",destName)));
    }
}
