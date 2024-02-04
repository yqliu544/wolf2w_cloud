package cn.wolfcode.wolf2w.search.feign;

import cn.wolfcode.wolf2w.article.dto.DestinationDto;
import cn.wolfcode.wolf2w.article.dto.StrategyDto;
import cn.wolfcode.wolf2w.article.dto.TravelDto;
import cn.wolfcode.wolf2w.redis.core.qo.QueryObject;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("article-service")
public interface ArticleFeignService {

    @PostMapping("/travels/search")
    public R<List<Object>> travelSearchList(@RequestBody QueryObject qo);
    @PostMapping("/strategies/search")
    public R<List<Object>> strategySearchList(@RequestBody QueryObject qo);
    @PostMapping("/destinations/search")
    public R<List<Object>> destinationSearchList(@RequestBody QueryObject qo);


    @GetMapping("/destinations/getByName")
    public R<DestinationDto> getDestByName(@RequestParam String name);

    @GetMapping("/travels/findByDestName")
    public R<List<TravelDto>> findTravelByDestName(@RequestParam String destName);

    @GetMapping("/strategies/findByDestName")
    public R<List<StrategyDto>> findStrategyByDestName(@RequestParam String destName);

    @GetMapping("/strategies/getById")
    StrategyDto getById(@RequestParam String id);

    @GetMapping("/travels/detail")
    R<TravelDto> getTravelById(@RequestParam String id);

    @GetMapping("/destinations/detail")
    R<DestinationDto> getDestById(@RequestParam String id);
}
