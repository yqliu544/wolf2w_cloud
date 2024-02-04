package cn.wolfcode.wolf2w.search.controller;

import cn.wolfcode.wolf2w.article.domain.Strategy;
import cn.wolfcode.wolf2w.article.dto.DestinationDto;
import cn.wolfcode.wolf2w.article.dto.StrategyDto;
import cn.wolfcode.wolf2w.article.dto.TravelDto;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import cn.wolfcode.wolf2w.search.domain.DestinationEs;
import cn.wolfcode.wolf2w.search.domain.StrategyEs;
import cn.wolfcode.wolf2w.search.domain.TravelEs;
import cn.wolfcode.wolf2w.search.domain.UserInfoEs;
import cn.wolfcode.wolf2w.search.feign.ArticleFeignService;
import cn.wolfcode.wolf2w.search.feign.UserInfoFeignService;
import cn.wolfcode.wolf2w.search.qo.SearchQueryObject;
import cn.wolfcode.wolf2w.search.service.ElasticsearchService;
import cn.wolfcode.wolf2w.search.vo.SearchResult;
import cn.wolfcode.wolf2w.user.dto.UserInfoDTO;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
@RestController
@RequestMapping("/q")
public class SearchController {

    @Autowired
    private ArticleFeignService articleFeignService;
    @Autowired
    private UserInfoFeignService userInfoFeignService;
    @Autowired
    private ElasticsearchService elasticsearchService;
    @GetMapping
    public R<?> search(SearchQueryObject qo){
        try {
            qo.setKeyword( URLDecoder.decode(qo.getKeyword(), "UTF-8"));
            switch (qo.getType()){
                case 0:
                    return this.searchForDest(qo);
                case 1:
                    return this.searchForStrategy(qo);
                case 2:
                    return this.searchForTravel(qo);
                case 3:
                    return this.searchForUser(qo);
                default:
                    return this.searchForAll(qo);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return R.ok();
    }

    private R<?> searchForAll(SearchQueryObject qo) {
        SearchResult result = new SearchResult();

        Page<StrategyDto> strategypage = elasticsearchService.searchWithHighlight(StrategyEs.class, StrategyDto.class, qo, (clazz, id) -> articleFeignService.getById(id), "title", "subTitle", "summary");
        result.setStrategies(strategypage.getContent());
        result.setTotal(strategypage.getTotalElements());
        Page<UserInfoDTO> userInfopage = elasticsearchService.searchWithHighlight(UserInfoEs.class, UserInfoDTO.class, qo, (clazz, id) -> userInfoFeignService.getById(id).checkAndGet(), "city", "info");
        result.setUsers(userInfopage.getContent());
        result.setTotal(result.getTotal()+ userInfopage.getTotalElements());
        Page<TravelDto> travelpage = elasticsearchService.searchWithHighlight(TravelEs.class, TravelDto.class, qo, (clazz, id) -> articleFeignService.getTravelById(id).checkAndGet(), "title", "summary");
        result.setTravels(travelpage.getContent());
        result.setTotal(result.getTotal()+ travelpage.getTotalElements());
        Page<DestinationDto> destpage = elasticsearchService.searchWithHighlight(DestinationEs.class, DestinationDto.class, qo, (clazz, id) -> articleFeignService.getDestById(id).checkAndGet(), "name", "info");
        result.setDests(destpage.getContent());
        result.setTotal(result.getTotal()+ destpage.getTotalElements());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result",result);
        jsonObject.put("qo",qo);
        return R.ok(jsonObject);
    }

    private R<?> searchForUser(SearchQueryObject qo) {
        Page<UserInfoDTO> page = elasticsearchService.searchWithHighlight(UserInfoEs.class, UserInfoDTO.class, qo, (clazz, id) -> userInfoFeignService.getById(id).checkAndGet(), "city", "info");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page",page);
        jsonObject.put("qo",qo);
        return R.ok(jsonObject);
    }

    private R<?> searchForTravel(SearchQueryObject qo) {
        Page<TravelDto> page = elasticsearchService.searchWithHighlight(TravelEs.class, TravelDto.class, qo, (clazz, id) -> articleFeignService.getTravelById(id).checkAndGet(), "title", "summary");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page",page);
        jsonObject.put("qo",qo);
        return R.ok(jsonObject);
    }

    private R<?> searchForStrategy(SearchQueryObject qo) {
        Page<StrategyDto> page = elasticsearchService.searchWithHighlight(StrategyEs.class, StrategyDto.class, qo, (clazz, id) -> articleFeignService.getById(id), "title", "subTitle", "summary");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("page",page);
        jsonObject.put("qo",qo);
        return R.ok(jsonObject);
    }

    private R<?> searchForDest(SearchQueryObject qo) {
        SearchResult result = new SearchResult();
        R<DestinationDto> destByName = articleFeignService.getDestByName(qo.getKeyword());
        DestinationDto destinationDto = destByName.checkAndGet();
        if (destinationDto!=null){
            result.setTotal(1l);
            R<List<StrategyDto>> strategyByDestName = articleFeignService.findStrategyByDestName(qo.getKeyword());
            List<StrategyDto> strategyDtos = strategyByDestName.checkAndGet();
            result.setStrategies(strategyDtos);
            result.setTotal(result.getTotal()+strategyDtos.size());
            R<List<TravelDto>> travelByDestName = articleFeignService.findTravelByDestName(qo.getKeyword());
            List<TravelDto> travelDtos = travelByDestName.checkAndGet();
            result.setTravels(travelDtos);
            result.setTotal(result.getTotal()+travelDtos.size());
            R<List<UserInfoDTO>> userByDestName = userInfoFeignService.findUserByDestName(qo.getKeyword());
            List<UserInfoDTO> userInfoDTOS = userByDestName.checkAndGet();
            result.setUsers(userInfoDTOS);
            result.setTotal(result.getTotal()+userInfoDTOS.size());
        }
        JSONObject json = new JSONObject();
        json.put("qo",qo);
        json.put("result",result);
        json.put("dest",destinationDto);
        return R.ok(json);
    }
}
