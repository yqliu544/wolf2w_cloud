package cn.wolfcode.wolf2w.article.controller;

import cn.wolfcode.wolf2w.article.domain.Destination;
import cn.wolfcode.wolf2w.article.domain.Strategy;
import cn.wolfcode.wolf2w.article.domain.Travel;
import cn.wolfcode.wolf2w.article.qo.QueryObject;
import cn.wolfcode.wolf2w.article.qo.TravelQuery;
import cn.wolfcode.wolf2w.article.service.DestinationService;
import cn.wolfcode.wolf2w.article.service.TravelService;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
@RequestMapping("/travels")
public class TravelController {
    @Autowired
    private TravelService travelService;

    @PostMapping("/search")
    public R<List<Travel>> searchList(@RequestBody QueryObject queryObject){
        return R.ok(travelService.list(new QueryWrapper<Travel>().last("limit "+queryObject.getOffset()+", "+queryObject.getSize())));
    }
    @GetMapping("/query")
    public R<Page<Travel>> pageList(TravelQuery query){
        Page<Travel> resultPage = travelService.pageList(query);
        return R.ok(resultPage);
    }

    @GetMapping("/detail")
    public R<Travel> getById(Long id){
        return R.ok(travelService.getById(id));
    }


    @PostMapping("/save")
    public R<?> save(Travel travel){
        travelService.save(travel);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> updateById(Travel travel){
        travelService.updateById(travel);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id){
        travelService.removeById(id);
        return R.ok();
    }

    @GetMapping("/findByDestName")
    public R<List<Travel>> findTravelByDestName(@RequestParam String destName){
        return R.ok(travelService.list(new QueryWrapper<Travel>().eq("dest_name",destName)));
    }

}
