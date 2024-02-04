package cn.wolfcode.wolf2w.search.controller;

import cn.wolfcode.wolf2w.redis.core.exception.BusinessException;
import cn.wolfcode.wolf2w.redis.core.qo.QueryObject;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import cn.wolfcode.wolf2w.redis.core.utils.RedisCache;
import cn.wolfcode.wolf2w.search.domain.DestinationEs;
import cn.wolfcode.wolf2w.search.domain.StrategyEs;
import cn.wolfcode.wolf2w.search.domain.TravelEs;
import cn.wolfcode.wolf2w.search.domain.UserInfoEs;
import cn.wolfcode.wolf2w.search.feign.ArticleFeignService;
import cn.wolfcode.wolf2w.search.feign.UserInfoFeignService;
import cn.wolfcode.wolf2w.search.service.ElasticsearchService;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年02月02日 下午 5:29
 */
@RestController
@RequestMapping("/init")
@ApiIgnore
@RefreshScope
public class ElasticsearchDataInitController {

    public static final String INIT_USER = "user";
    public static final String INIT_TRAVEL = "travel";
    public static final String INIT_STRATEGY = "strategy";
    public static final String INIT_DESTINATION = "destination";
    public static final Integer BATCH_COUNT = 200;
    public static final Map<String, EsDataInitStrategy> DATA_HANDLER_STRATEGY_MAP = new HashMap<>();

    @Getter
    @Setter
    static class EsDataInitStrategy{
        private Function<QueryObject, R<List<Object>>> function;
        private Class<?> aClass;

        public EsDataInitStrategy(Function<QueryObject, R<List<Object>>> function, Class<?> aClass) {
            this.function = function;
            this.aClass = aClass;
        }
    }

    @Value("${es.init.key}")
    private String initKey;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserInfoFeignService userInfoFeignService;
    @Autowired
    private ElasticsearchService elasticsearchService;
    @Autowired
    private ArticleFeignService articleFeignService;

    @PostConstruct
    public void postCoustrust() {
        EsDataInitStrategy esDataInitStrategy = new EsDataInitStrategy(qo -> userInfoFeignService.findList(qo.getCurrent(), qo.getSize()), UserInfoEs.class);
        DATA_HANDLER_STRATEGY_MAP.put(INIT_USER,esDataInitStrategy);

        EsDataInitStrategy travelDataInitStrategy = new EsDataInitStrategy(qo -> articleFeignService.travelSearchList(qo), TravelEs.class);
        DATA_HANDLER_STRATEGY_MAP.put(INIT_TRAVEL,travelDataInitStrategy);

        EsDataInitStrategy strategyDataInitStrategy = new EsDataInitStrategy(qo -> articleFeignService.strategySearchList(qo), StrategyEs.class);
        DATA_HANDLER_STRATEGY_MAP.put(INIT_STRATEGY,strategyDataInitStrategy);

        EsDataInitStrategy distinationDataInitStrategy = new EsDataInitStrategy(qo -> articleFeignService.destinationSearchList(qo), DestinationEs.class);
        DATA_HANDLER_STRATEGY_MAP.put(INIT_DESTINATION,distinationDataInitStrategy);


    }


    @GetMapping("/{key}/{type}")
    public ResponseEntity<?> init(@PathVariable String key, @PathVariable String type) {
        if (StringUtils.isEmpty(key) || !initKey.equals(key)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String redisKey = "es:init:" + key + ":" + type;
        Boolean ret = redisCache.setnx(redisKey, "inited");
        if (ret == null || !ret) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        this.doInit(type);
        return ResponseEntity.ok().body("init success");
    }

    private void doInit(String type) {
        List<Object> dataList = null;
        Integer current = 1;
        do {
            dataList = this.handleRemoteDataList(current++, type);
            if (dataList == null || dataList.size() == 0) {
                return;
            }
            elasticsearchService.save(dataList);
        } while (true);

    }

    private List<Object> handleRemoteDataList(Integer current, String type) {
        Function<QueryObject, R<List<Object>>> function = DATA_HANDLER_STRATEGY_MAP.get(type).getFunction();
        if (function == null) {
            throw new BusinessException("初始化参数类型错误");
        }
        R<List<Object>> ret = function.apply(new QueryObject(current, BATCH_COUNT));
        List<Object> list = ret.checkAndGet();
        if (list == null || list.size() == 0) {
            return list;
        }
        try {
            List<Object>dataList = new ArrayList<>(list.size());
            Class<?> clazz = DATA_HANDLER_STRATEGY_MAP.get(type).getAClass();
            for (Object dto : list) {
                Object es = clazz.newInstance();
                BeanUtils.copyProperties(es,dto);
                dataList.add(es);
            }
            return  dataList;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;


    }
}
