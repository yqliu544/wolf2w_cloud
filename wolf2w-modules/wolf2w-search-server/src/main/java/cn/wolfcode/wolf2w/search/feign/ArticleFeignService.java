package cn.wolfcode.wolf2w.search.feign;

import cn.wolfcode.wolf2w.redis.core.qo.QueryObject;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("article-service")
public interface ArticleFeignService {

    @PostMapping("/travels/search")
    public R<List<Object>> travelSearchList(@RequestBody QueryObject qo);
    @PostMapping("/strategies/search")
    public R<List<Object>> strategySearchList(@RequestBody QueryObject qo);
    @PostMapping("/destinations/search")
    public R<List<Object>> destinationSearchList(@RequestBody QueryObject qo);
}
