package cn.wolfcode.wolf2w.user.service;

import cn.wolfcode.wolf2w.user.domain.UserInfo;
import cn.wolfcode.wolf2w.user.dto.UserInfoDTO;
import cn.wolfcode.wolf2w.user.vo.RegisterRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface UserInfoService extends IService<UserInfo> {

    UserInfo findByPhone(String phone);

    void register(RegisterRequest registerRequest);

    Map<String, Object> login(String username, String password);

    UserInfoDTO getDtoById(Long id);

    List<Long> getFavorStrategyIdList(Long userId);

    boolean favoriteStrategy(Long sid);
}
