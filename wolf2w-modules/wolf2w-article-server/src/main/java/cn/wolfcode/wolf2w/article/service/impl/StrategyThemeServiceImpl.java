package cn.wolfcode.wolf2w.article.service.impl;

import cn.wolfcode.wolf2w.article.domain.StrategyCatalog;
import cn.wolfcode.wolf2w.article.domain.StrategyTheme;
import cn.wolfcode.wolf2w.article.mapper.StrategyCatalogMapper;
import cn.wolfcode.wolf2w.article.mapper.StrategyThemeMapper;
import cn.wolfcode.wolf2w.article.service.StrategyThemeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class StrategyThemeServiceImpl extends ServiceImpl<StrategyThemeMapper, StrategyTheme> implements StrategyThemeService {
}
