package cn.wolfcode.wolf2w.search.controller;

import cn.wolfcode.wolf2w.redis.core.exception.BusinessException;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import cn.wolfcode.wolf2w.redis.core.utils.RedisCache;
import cn.wolfcode.wolf2w.search.domain.UserInfoEs;
import cn.wolfcode.wolf2w.search.feign.UserInfoFeignService;
import cn.wolfcode.wolf2w.search.service.ElasticsearchService;
import cn.wolfcode.wolf2w.user.dto.UserInfoDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年02月02日 下午 5:29
 */
@RestController
@RequestMapping
public class ElasticsearchDataInitController {

    public static final String INIT_USER="user";
    public static final String INIT_TRAVEL="travel";
    public static final String INIT_STRATEGY="strategy";
    public static final String INIT_DESTINATION="destination";
    public static final Integer BATCH_COUNT=200;


    @Value("${es.init.key}")
    private String initKey;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserInfoFeignService userInfoFeignService;
    @Autowired
    private ElasticsearchService elasticsearchService;

    @GetMapping("/{key}/{type}")
    public ResponseEntity<?> init(@PathVariable String key,@PathVariable String type){
        if (StringUtils.isEmpty(key)||!initKey.equals(key)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String redisKey="es:init:"+key+":"+type;
        Boolean ret = redisCache.setnx(redisKey, "inited");
        if (ret==null||!ret){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        this.doInit(type);
        return ResponseEntity.ok().body("init success");
    }

    private void doInit(String type) {
        List<Object> dataList=null;
        do {
            switch (type) {
                case INIT_USER:
                    R<List<UserInfoDTO>> ret = userInfoFeignService.findList(BATCH_COUNT);
                    List<UserInfoDTO> list = ret.getAndCheck();
                    if (list == null || list.size() == 0) {
                        return;
                    }
                    dataList = new ArrayList<>(list.size());
                    for (UserInfoDTO dto : list) {
                        UserInfoEs es = new UserInfoEs();
                        BeanUtils.copyProperties(dto, es);
                        dataList.add(es);
                    }
                    break;
                case INIT_TRAVEL:
                case INIT_STRATEGY:
                case INIT_DESTINATION:
                default:
                    throw new BusinessException("类型错误");
            }

            elasticsearchService.save(dataList);
        }while (true);

    }
}
