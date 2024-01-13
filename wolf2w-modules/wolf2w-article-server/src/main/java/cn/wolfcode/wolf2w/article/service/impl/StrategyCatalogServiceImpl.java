package cn.wolfcode.wolf2w.article.service.impl;

import cn.wolfcode.wolf2w.article.domain.StrategyCatalog;
import cn.wolfcode.wolf2w.article.mapper.RegionMapper;
import cn.wolfcode.wolf2w.article.mapper.StrategyCatalogMapper;
import cn.wolfcode.wolf2w.article.service.StrategyCatalogService;
import cn.wolfcode.wolf2w.article.vo.StrategyCatalogGroup;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StrategyCatalogServiceImpl extends ServiceImpl<StrategyCatalogMapper, StrategyCatalog> implements StrategyCatalogService {
    @Override
    public List<StrategyCatalogGroup> findGroupList() {

        return getBaseMapper().selectGroupList();
    }
}
