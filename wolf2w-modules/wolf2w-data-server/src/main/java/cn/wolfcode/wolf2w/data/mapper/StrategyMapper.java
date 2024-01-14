package cn.wolfcode.wolf2w.data.mapper;

import cn.wolfcode.wolf2w.article.domain.Strategy;
import cn.wolfcode.wolf2w.article.domain.StrategyCatalog;
import cn.wolfcode.wolf2w.article.domain.StrategyRank;
import cn.wolfcode.wolf2w.article.vo.StrategyCondition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月11日 下午 4:39
 */
public interface StrategyMapper extends BaseMapper<Strategy> {
    List<StrategyRank> selectStrategyRankByAbroad(Integer abroad);
    List<StrategyRank> selectStrategyRankHotList();

}
