package cn.wolfcode.wolf2w.article.service;

import cn.wolfcode.wolf2w.article.domain.StrategyCatalog;
import cn.wolfcode.wolf2w.article.domain.StrategyTheme;
import cn.wolfcode.wolf2w.article.vo.StrategyCatalogGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月11日 下午 4:47
 */
public interface StrategyCatalogService extends IService<StrategyCatalog> {


    List<StrategyCatalogGroup> findGroupList();
}
