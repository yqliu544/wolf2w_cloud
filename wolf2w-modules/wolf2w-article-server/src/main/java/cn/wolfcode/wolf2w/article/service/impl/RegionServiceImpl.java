package cn.wolfcode.wolf2w.article.service.impl;

import cn.wolfcode.wolf2w.article.domain.Region;
import cn.wolfcode.wolf2w.article.mapper.RegionMapper;
import cn.wolfcode.wolf2w.article.service.RegionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月11日 下午 4:48
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService   {

    @Override
    public List<Region> findHotList() {
        return list(new QueryWrapper<Region>()
                .eq("ishot",Region.STATE_HOT)
                .orderByAsc("seq")
        );
    }
}
