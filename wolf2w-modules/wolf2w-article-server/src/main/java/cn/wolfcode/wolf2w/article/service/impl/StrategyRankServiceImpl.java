package cn.wolfcode.wolf2w.article.service.impl;

import cn.wolfcode.wolf2w.article.domain.Region;
import cn.wolfcode.wolf2w.article.domain.StrategyRank;
import cn.wolfcode.wolf2w.article.mapper.RegionMapper;
import cn.wolfcode.wolf2w.article.mapper.StrategyRankMapper;
import cn.wolfcode.wolf2w.article.service.RegionService;
import cn.wolfcode.wolf2w.article.service.StrategyRankService;
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
public class StrategyRankServiceImpl extends ServiceImpl<StrategyRankMapper, StrategyRank> implements StrategyRankService {


    @Override
    public List<StrategyRank> selectLastRanksByType(int type) {
        QueryWrapper<StrategyRank> wrapper = new QueryWrapper<StrategyRank>().eq("type", type).orderByDesc("statis_time").last("limit 10");
        return list(wrapper);
    }
}
