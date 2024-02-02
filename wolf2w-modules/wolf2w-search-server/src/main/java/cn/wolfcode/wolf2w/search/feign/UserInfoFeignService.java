package cn.wolfcode.wolf2w.search.feign;

import cn.wolfcode.wolf2w.redis.core.utils.R;
import cn.wolfcode.wolf2w.user.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient("user-service")
public interface UserInfoFeignService {
    R<List<UserInfoDTO>> findList(Integer limit);
}
