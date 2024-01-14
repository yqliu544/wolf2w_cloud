package cn.wolfcode.wolf2w.data.job;

import cn.wolfcode.wolf2w.article.domain.Strategy;
import cn.wolfcode.wolf2w.article.domain.StrategyRank;
import cn.wolfcode.wolf2w.data.mapper.StrategyMapper;
import cn.wolfcode.wolf2w.data.mapper.StrategyRankMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Component
public class StrategyRankStatisJob {
    @Autowired
    private StrategyMapper strategyMapper;
    @Autowired
    private StrategyRankMapper strategyRankMapper;
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "0 */30 * * * *")
    public void statisRank(){
        Date now = new Date();
        List<StrategyRank> strategyRanks = strategyMapper.selectStrategyRankByAbroad(Strategy.ABROAD_NO);
        for (StrategyRank rank : strategyRanks) {
            rank.setType(StrategyRank.TYPE_CHINA);
            rank.setStatisTime(now);
        }
        strategyRankMapper.batchInsert(strategyRanks);

        strategyRanks = strategyMapper.selectStrategyRankByAbroad(Strategy.ABROAD_YES);
        for (StrategyRank rank : strategyRanks) {
            rank.setType(StrategyRank.TYPE_ABROAD);
            rank.setStatisTime(now);
        }
        strategyRankMapper.batchInsert(strategyRanks);

        strategyRanks = strategyMapper.selectStrategyRankHotList();
        for (StrategyRank rank : strategyRanks) {
            rank.setType(StrategyRank.TYPE_HOT);
            rank.setStatisTime(now);
        }
        strategyRankMapper.batchInsert(strategyRanks);
        strategyRankMapper.delete(new QueryWrapper<StrategyRank>().lt("UNIX_TIMESTAMP(statis_time)",(now.getTime()-200)/1000));
    }
}
