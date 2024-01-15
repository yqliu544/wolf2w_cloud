package cn.wolfcode.wolf2w.user.controller;

import cn.wolfcode.wolf2w.redis.core.utils.R;
import cn.wolfcode.wolf2w.user.dto.UserInfoDTO;
import cn.wolfcode.wolf2w.user.service.UserInfoService;
import cn.wolfcode.wolf2w.user.domain.UserInfo;
import cn.wolfcode.wolf2w.user.vo.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
}
