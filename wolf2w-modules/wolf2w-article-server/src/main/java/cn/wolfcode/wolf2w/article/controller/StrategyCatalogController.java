package cn.wolfcode.wolf2w.article.controller;

import cn.wolfcode.wolf2w.article.domain.Destination;
import cn.wolfcode.wolf2w.article.domain.StrategyCatalog;
import cn.wolfcode.wolf2w.article.service.DestinationService;
import cn.wolfcode.wolf2w.article.service.StrategyCatalogService;
import cn.wolfcode.wolf2w.article.vo.StrategyCatalogGroup;
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
@RequestMapping("/strategies/calalogs")
public class StrategyCatalogController {
    @Autowired
    private StrategyCatalogService strategyCatalogService;

    @GetMapping("/query")
    public R<Page<StrategyCatalog>> pageList(Page<StrategyCatalog> page){
        Page<StrategyCatalog> resultPage = strategyCatalogService.page(page);
        return R.ok(resultPage);
    }

    @GetMapping("/groups")
    public R<List<StrategyCatalogGroup>> groupList(){
        return R.ok(strategyCatalogService.findGroupList());
    }

    @GetMapping("/detail")
    public R<StrategyCatalog> getById(Long id){
        return R.ok(strategyCatalogService.getById(id));
    }

    @PostMapping("/save")
    public R<?> save(StrategyCatalog strategyCatalog){
        strategyCatalogService.save(strategyCatalog);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> updateById(StrategyCatalog strategyCatalog){
        strategyCatalogService.updateById(strategyCatalog);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id){
        strategyCatalogService.removeById(id);
        return R.ok();
    }

}
