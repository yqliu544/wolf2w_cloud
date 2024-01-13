package cn.wolfcode.wolf2w.article.controller;

import cn.wolfcode.wolf2w.article.domain.Strategy;
import cn.wolfcode.wolf2w.article.service.StrategyService;
import cn.wolfcode.wolf2w.article.utils.OssUtil;
import cn.wolfcode.wolf2w.auth.anno.RequireLogin;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月11日 下午 4:51
 */
@RestController
@RequestMapping("/strategies")
public class StrategyController {
    @Autowired
    private StrategyService strategyService;


    @GetMapping("/query")
    public R<Page<Strategy>> pageList(Page<Strategy> page){
        Page<Strategy> resultPage = strategyService.page(page);
        return R.ok(resultPage);
    }

    @GetMapping("/detail")
    public R<Strategy> getById(Long id){
        return R.ok(strategyService.getById(id));
    }



    @PostMapping("/save")
    public R<?> save(Strategy strategy){
        strategyService.save(strategy);
        return R.ok();
    }

    @PostMapping("/update")
    public R<?> updateById(Strategy strategy){
        strategyService.updateById(strategy);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    public R<?> deleteById(@PathVariable Long id){
        strategyService.removeById(id);
        return R.ok();
    }

    @PostMapping("/uploadImg")
    public JSONObject uploadImg(MultipartFile upload){
        JSONObject result = new JSONObject();
        if (upload==null){
            result.put("uploaded",0);
            JSONObject error = new JSONObject();
            error.put("message","请选择要上传的文件！");
            result.put("error",error);
            return result;
        }
        String fileName=upload.getOriginalFilename().substring(0,upload.getOriginalFilename().lastIndexOf("."))+"_"+System.currentTimeMillis();
        String url = OssUtil.upload4SpecialName("images", fileName, upload);
        result.put("uploaded",1);
        result.put("fileName",upload.getOriginalFilename());
        result.put("url",url);
        return result;
    }

}
