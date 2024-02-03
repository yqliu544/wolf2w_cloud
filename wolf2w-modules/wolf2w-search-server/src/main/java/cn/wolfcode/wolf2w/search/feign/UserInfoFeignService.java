package cn.wolfcode.wolf2w.search.feign;

import cn.wolfcode.wolf2w.redis.core.utils.R;
import cn.wolfcode.wolf2w.user.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("user-service")
public interface UserInfoFeignService {
    @GetMapping("/users")
    R<List<Object>> findList(@RequestParam Integer current,@RequestParam Integer limit);
}
