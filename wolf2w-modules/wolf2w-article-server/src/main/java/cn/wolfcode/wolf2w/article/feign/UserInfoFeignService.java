package cn.wolfcode.wolf2w.article.feign;

import cn.wolfcode.wolf2w.redis.core.utils.R;
import cn.wolfcode.wolf2w.user.dto.UserInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("user-service")
public interface UserInfoFeignService {
    @GetMapping("/users/getById")
    public R<UserInfoDTO> getById(@RequestParam Long id);

    @GetMapping("/users/favor/strategies")
    public R<List<Long>> getFavorStrategyIdList(@RequestParam Long userId);
}
