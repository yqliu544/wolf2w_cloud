package cn.wolfcode.wolf2w.user.service.impl;

import cn.wolfcode.wolf2w.user.mapper.UserInfoMapper;
import cn.wolfcode.wolf2w.user.service.UserInfoService;
import cn.wolfcode.wolf2w.user.domain.UserInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserInfoSeriveImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
