package cn.wolfcode.wolf2w.article.controller;

import cn.wolfcode.wolf2w.article.domain.Destination;
import cn.wolfcode.wolf2w.article.domain.Region;
import cn.wolfcode.wolf2w.article.dto.DestinationDto;
import cn.wolfcode.wolf2w.article.qo.DestinationQuery;
import cn.wolfcode.wolf2w.article.qo.QueryObject;
import cn.wolfcode.wolf2w.article.service.DestinationService;
import cn.wolfcode.wolf2w.auth.anno.RequireLogin;
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
@RequestMapping("/destinations")
public class DestinationController {
    @Autowired
    private DestinationService destinationService;

    @GetMapping
    public R<Page<Destination>> pageList(DestinationQuery quary){
        Page<Destination> resultPage = destinationService.pageList(quary);
        return R.ok(resultPage);
    }

    @GetMapping("/list")
    public R<List<Destination>> listAll(){
        return R.ok(destinationService.list());
    }

    @PostMapping("/search")
    public R<List<Destination>> searchList(@RequestBody QueryObject queryObject){
        return R.ok(destinationService.list(new QueryWrapper<Destination>().last("limit "+queryObject.getOffset()+", "+queryObject.getSize())));
    }


    @GetMapping("/detail")
    public R<Destination> getById(Long id){
        return R.ok(destinationService.getById(id));
    }


    @PostMapping("/save")
    public R<?> save(Destination destination){
        destinationService.save(destination);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> updateById(Destination destination){
        destinationService.updateById(destination);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id){
        destinationService.removeById(id);
        return R.ok();
    }

    @GetMapping("/toasts")
    public R<List<Destination>> toasts(Long destId){
        return R.ok(destinationService.findToasts(destId));
    }

    @GetMapping("/hotList")
    public R<List<Destination>> hotList(Long rid){
        return R.ok(destinationService.findDestByRid(rid));
    }

    @GetMapping("/getByName")
    public R<Destination> getDestByName(@RequestParam String name){
       return R.ok(destinationService.getOne(new QueryWrapper<Destination>().eq("name",name)));
    }

}
