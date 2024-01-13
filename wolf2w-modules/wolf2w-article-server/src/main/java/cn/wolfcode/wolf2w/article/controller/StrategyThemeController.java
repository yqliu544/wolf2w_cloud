package cn.wolfcode.wolf2w.article.controller;

import cn.wolfcode.wolf2w.article.domain.Destination;
import cn.wolfcode.wolf2w.article.domain.StrategyTheme;
import cn.wolfcode.wolf2w.article.service.DestinationService;
import cn.wolfcode.wolf2w.article.service.StrategyThemeService;
import cn.wolfcode.wolf2w.auth.anno.RequireLogin;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月11日 下午 4:51
 */
@RestController
@RequestMapping("/strategies/themes")
public class StrategyThemeController {
    @Autowired
    private StrategyThemeService strategyThemeService;


    @GetMapping("/query")
    public R<Page<StrategyTheme>> pageList(Page<StrategyTheme> page){
        Page<StrategyTheme> resultPage = strategyThemeService.page(page);
        return R.ok(resultPage);
    }

    @GetMapping("/detail")
    public R<StrategyTheme> getById(Long id){
        return R.ok(strategyThemeService.getById(id));
    }

    @GetMapping("/list")
    public R<List<StrategyTheme>> listAll(){
        return R.ok(strategyThemeService.list());
    }


    @PostMapping("/save")
    public R<?> save(StrategyTheme strategyTheme){
        strategyThemeService.save(strategyTheme);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> updateById(StrategyTheme strategyTheme){
        strategyThemeService.updateById(strategyTheme);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id){
        strategyThemeService.removeById(id);
        return R.ok();
    }

}
