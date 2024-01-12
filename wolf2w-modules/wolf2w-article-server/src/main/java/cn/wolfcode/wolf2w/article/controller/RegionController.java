package cn.wolfcode.wolf2w.article.controller;

import cn.wolfcode.wolf2w.article.domain.Destination;
import cn.wolfcode.wolf2w.article.domain.Region;
import cn.wolfcode.wolf2w.article.service.DestinationService;
import cn.wolfcode.wolf2w.article.service.RegionService;
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
@RequestMapping("/regions")
public class RegionController {
    @Autowired
    private RegionService regionService;
    @Autowired
    private DestinationService destinationService;

    @GetMapping
    public R<Page<Region>> pageList(Page<Region> page){
        Page<Region> resultPage = regionService.page(page);
        return R.ok(resultPage);
    }
    @RequireLogin
    @GetMapping("/detail")
    public R<Region> getById(Long id){
        return R.ok(regionService.getById(id));
    }

    @GetMapping("/hotList")
    public R<List<Region>> hotList(){
        return R.ok(regionService.findHotList());
    }



    @PostMapping("/save")
    public R<?> save(Region region){
        regionService.save(region);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> updateById(Region region){
        regionService.updateById(region);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id){
        regionService.removeById(id);
        return R.ok();
    }

    @GetMapping("/{id}/destination")
    public R<List<Destination>> getDestinationByRegionId(@PathVariable Long id){
        List<Destination> list=destinationService.getDestinationByRegionId(id);
        return R.ok(list);
    }
}
