package cn.wolfcode.wolf2w.article.mapper;

import cn.wolfcode.wolf2w.article.domain.StrategyCatalog;
import cn.wolfcode.wolf2w.article.domain.StrategyTheme;
import cn.wolfcode.wolf2w.article.vo.StrategyCatalogGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月11日 下午 4:39
 */
public interface StrategyCatalogMapper extends BaseMapper<StrategyCatalog> {

    List<StrategyCatalogGroup> selectGroupList();
}
