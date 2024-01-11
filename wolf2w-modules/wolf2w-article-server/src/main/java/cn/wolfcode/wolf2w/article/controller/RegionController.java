package cn.wolfcode.wolf2w.article.controller;

import cn.wolfcode.wolf2w.article.domain.Region;
import cn.wolfcode.wolf2w.article.service.RegionService;
import cn.wolfcode.wolf2w.auth.anno.RequireLogin;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月11日 下午 4:51
 */
@RestController
@RequestMapping("/regions")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @GetMapping
    public R<Page<Region>> pageList(Page<Region> page){
        Page<Region> resultPage = regionService.page(page);
        return R.ok(resultPage);
    }
    @RequireLogin
    @GetMapping("/{id}")
    public R<Region> pageList(@PathVariable Long id){
        return R.ok(regionService.getById(id));
    }
}
