package cn.wolfcode.wolf2w.comment.controller;

import cn.wolfcode.wolf2w.auth.anno.RequireLogin;
import cn.wolfcode.wolf2w.comment.domain.StrategyComment;
import cn.wolfcode.wolf2w.comment.service.StrategyCommentService;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/strategies/comments")
public class StrategyCommentController {

    @Autowired
    private StrategyCommentService strategyCommentService;

    @RequireLogin
    @PostMapping("/save")
    public R<?> save(StrategyComment comment){
        strategyCommentService.save(comment);
        return R.ok();
    }

    @RequireLogin
    @PostMapping("/save")
    public R<?> likes(String cid){
        strategyCommentService.dolike(cid);
        return R.ok();
    }
}
