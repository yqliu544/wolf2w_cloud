package cn.wolfcode.wolf2w.article.service.impl;

import cn.wolfcode.wolf2w.article.domain.*;
import cn.wolfcode.wolf2w.article.mapper.StrategyContentMapper;
import cn.wolfcode.wolf2w.article.mapper.StrategyMapper;
import cn.wolfcode.wolf2w.article.mapper.StrategyThemeMapper;
import cn.wolfcode.wolf2w.article.service.DestinationService;
import cn.wolfcode.wolf2w.article.service.StrategyCatalogService;
import cn.wolfcode.wolf2w.article.service.StrategyService;
import cn.wolfcode.wolf2w.article.service.StrategyThemeService;
import cn.wolfcode.wolf2w.article.utils.OssUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
}
