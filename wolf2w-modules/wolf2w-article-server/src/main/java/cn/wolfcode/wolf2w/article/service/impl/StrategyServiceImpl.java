package cn.wolfcode.wolf2w.article.service.impl;

import cn.wolfcode.wolf2w.article.domain.*;
import cn.wolfcode.wolf2w.article.feign.UserInfoFeignService;
import cn.wolfcode.wolf2w.article.mapper.StrategyContentMapper;
import cn.wolfcode.wolf2w.article.mapper.StrategyMapper;
import cn.wolfcode.wolf2w.article.qo.StrategyQuery;
import cn.wolfcode.wolf2w.article.redis.key.StrategyRedisKeyPrefix;
import cn.wolfcode.wolf2w.article.service.DestinationService;
import cn.wolfcode.wolf2w.article.service.StrategyCatalogService;
import cn.wolfcode.wolf2w.article.service.StrategyService;
import cn.wolfcode.wolf2w.article.service.StrategyThemeService;
import cn.wolfcode.wolf2w.article.utils.OssUtil;
import cn.wolfcode.wolf2w.article.vo.StrategyCondition;
import cn.wolfcode.wolf2w.auth.util.AuthenticationUtils;
import cn.wolfcode.wolf2w.redis.core.utils.DateUtils;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import cn.wolfcode.wolf2w.redis.core.utils.RedisCache;
import cn.wolfcode.wolf2w.user.vo.LoginUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class StrategyServiceImpl extends ServiceImpl<StrategyMapper, Strategy> implements StrategyService {
    @Autowired
    private StrategyCatalogService strategyCatalogService;
    @Autowired
    private DestinationService destinationService;
    @Autowired
    private StrategyThemeService strategyThemeService;
    @Autowired
    private StrategyContentMapper strategyContentMapper;
    @Autowired
    private RedisCache redisCache;
    @Lazy
    @Autowired
    private UserInfoFeignService userInfoFeignService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean save(Strategy entity) {
        return dosaveOrUpdate(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateById(Strategy entity) {
        return dosaveOrUpdate(entity);
    }

    @Override
    public Strategy getById(Serializable id) {
        Strategy strategy = super.getById(id);
        StrategyContent content = strategyContentMapper.selectById(id);
        strategy.setContent(content);
        LoginUser user = AuthenticationUtils.getUser();
        if (user!=null){
            R<List<Long>> favoriteStrategyList=userInfoFeignService.getFavorStrategyIdList(user.getId());
            List<Long> list = favoriteStrategyList.checkAndGet();
            strategy.setFavorite(list.contains(id));
        }
        Map<String, Object> statmap = redisCache.getCacheMap(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(id + ""));
        if (statmap!=null){
            strategy.setViewnum((Integer) statmap.get("viewnum"));
            strategy.setReplynum((Integer) statmap.get("replynum"));
            strategy.setFavornum((Integer) statmap.get("favornum"));
            strategy.setSharenum((Integer) statmap.get("sharenum"));
            strategy.setThumbsupnum((Integer) statmap.get("thumbsupnum"));
        }
        return strategy;
    }

    private boolean dosaveOrUpdate(Strategy entity){
        if (!StringUtils.isEmpty(entity.getCoverUrl()) || !entity.getCoverUrl().startsWith("http")){
            String uuid = UUID.randomUUID().toString();
            String url = OssUtil.uploadImgByBase64("images/strategies", uuid + ".jpg", entity.getCoverUrl());
            entity.setCoverUrl(url);
        }

        StrategyCatalog catalog = strategyCatalogService.getById(entity.getCatalogId());
        entity.setCatalogName(catalog.getName());
        entity.setDestId(catalog.getDestId());
        entity.setDestName(catalog.getDestName());
        List<Destination> toasts =destinationService.findToasts(catalog.getDestId());
        Destination dest = toasts.get(0);
        if (dest.getId()==1){
            entity.setIsabroad(Strategy.ABROAD_NO);
        }else {
            entity.setIsabroad(Strategy.ABROAD_YES);
        }
        StrategyTheme theme = strategyThemeService.getById(entity.getThemeId());
        entity.setThemeName(theme.getName());
        if (entity.getId()==null){
            entity.setCreateTime(new Date());
            entity.setViewnum(0);
            entity.setSharenum(0);
            entity.setThumbsupnum(0);
            entity.setReplynum(0);
            entity.setFavornum(0);
            entity.setState(Strategy.STATE_NORMAL);
            boolean save = super.save(entity);
            StrategyContent content = entity.getContent();
            content.setId(entity.getId());
            return save && strategyContentMapper.insert(content) > 0;
        }
        boolean b = super.updateById(entity);
        StrategyContent content = entity.getContent();
        content.setId(entity.getId());
        int row = strategyContentMapper.updateById(entity.getContent());
        return row >0 && b;

    }

    @Override
    public List<StrategyCatalog> findGroupsByDestId(Long destId) {

        return getBaseMapper().selectGroupsByDestId(destId);
    }

    @Override
    public StrategyContent getContentById(Long id) {
        return strategyContentMapper.selectById(id);
    }

    @Override
    public List<Strategy> findViewnumTop3ByDestId(Long destId) {
        QueryWrapper<Strategy> wrapper = new QueryWrapper<Strategy>()
                .eq("dest_id", destId)
                .orderByDesc("viewnum")
                .last("limit 3");
        return list(wrapper);
    }

    @Override
    public Page<Strategy> pageList(StrategyQuery query) {
        if ((query.getType()!=null && query.getType() !=-1)&& (query.getRefid()!=null && query.getRefid()!=-1)){
            if (query.getType()==3){
                query.setThemeId(query.getRefid());
            }else {
                query.setDestId(query.getRefid());
            }
        }
        QueryWrapper<Strategy> wrapper = new QueryWrapper<Strategy>()
                .eq(query.getDestId()!=null,"dest_id",query.getDestId())
                .eq(query.getThemeId()!=null,"theme_id",query.getThemeId())
                .orderByDesc(!StringUtils.isEmpty(query.getOrderBy()),query.getOrderBy());
        return super.page(new Page<>(query.getCurrent(),query.getSize()),wrapper);
    }

    @Override
    public List<StrategyCondition> findDestCondition(int abroadNo) {
        return getBaseMapper().selectDestCondition(abroadNo);
    }

    @Override
    public List<StrategyCondition> findThemeCondition() {
        return getBaseMapper().selectThemeCondition();
    }

    @Override
    public void viewnumIncr(Long id) {
        statDataIncr("viewnum",id);
    }

    @Override
    public boolean thumbnumIncr(Long sid) {
        LoginUser user = AuthenticationUtils.getUser();
        StrategyRedisKeyPrefix keyPrefix = StrategyRedisKeyPrefix.STRATEGIES_TOP_MAP;
        String fullKey = keyPrefix.fullKey(sid + "");
//        Integer count = redisCache.getCacheMapValue(fullKey, user.getId() + "");
//        if (count!=null&&count>0){
//            return false;
//        }
        keyPrefix.setTimeout(DateUtils.getLastMillisSeconds());
        keyPrefix.setUnit(TimeUnit.MILLISECONDS);
        Long ret = redisCache.hashIncrement(keyPrefix, user.getId() + "", 1, sid + "");
        if (ret>1){
            return false;
        }
        statDataIncr("thumbsupnum",sid);
        return true;
    }

    @Override
    public Map<String, Object> getStatData(Long id) {

        return redisCache.getCacheMap(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP.fullKey(id+""));
    }


    private void statDataIncr(String hashKey,Long sid){
        redisCache.hashIncrement(StrategyRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP,hashKey,1,sid+"");
        redisCache.zsetIncrement(StrategyRedisKeyPrefix.STRATEGIES_STAT_COUNT_RANK_ZSET,1,sid+"");
    }
}
