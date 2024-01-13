package cn.wolfcode.wolf2w.article.service;

import cn.wolfcode.wolf2w.article.domain.Strategy;
import cn.wolfcode.wolf2w.article.domain.StrategyCatalog;
import cn.wolfcode.wolf2w.article.domain.StrategyContent;
import cn.wolfcode.wolf2w.article.qo.StrategyQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月11日 下午 4:47
 */
public interface StrategyService extends IService<Strategy> {

    List<StrategyCatalog> findGroupsByDestId(Long dest);

    StrategyContent getContentById(Long id);

    List<Strategy> findViewnumTop3ByDestId(Long destId);

    Page<Strategy> pageList(StrategyQuery query);
}
