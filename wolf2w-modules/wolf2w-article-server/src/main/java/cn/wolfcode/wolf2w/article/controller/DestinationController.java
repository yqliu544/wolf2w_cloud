package cn.wolfcode.wolf2w.article.controller;

import cn.wolfcode.wolf2w.article.domain.Destination;
import cn.wolfcode.wolf2w.article.domain.Region;
import cn.wolfcode.wolf2w.article.service.DestinationService;
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
@RequestMapping("/destinations")
public class DestinationController {
    @Autowired
    private DestinationService destinationService;

    @GetMapping
    public R<Page<Destination>> pageList(Page<Destination> page){
        Page<Destination> resultPage = destinationService.page(page);
        return R.ok(resultPage);
    }

    @GetMapping("/list")
    public R<List<Destination>> listAll(){
        return R.ok(destinationService.list());
    }

    @RequireLogin
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
}
