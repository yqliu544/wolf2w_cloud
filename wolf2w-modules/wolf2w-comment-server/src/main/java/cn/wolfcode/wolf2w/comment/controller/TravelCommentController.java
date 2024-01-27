package cn.wolfcode.wolf2w.comment.controller;

import cn.wolfcode.wolf2w.auth.anno.RequireLogin;
import cn.wolfcode.wolf2w.comment.domain.StrategyComment;
import cn.wolfcode.wolf2w.comment.domain.TravelComment;
import cn.wolfcode.wolf2w.comment.service.StrategyCommentService;
import cn.wolfcode.wolf2w.comment.service.TravelCommentService;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travels/comments")
public class TravelCommentController {

    @Autowired
    private TravelCommentService travelCommentService;

    @RequireLogin
    @PostMapping("/save")
    public R<?> save(TravelComment comment){
        travelCommentService.save(comment);
        return R.ok();
    }

    @GetMapping("/query")
    public R<List<TravelComment>> query(Long travelId){
        return  R.ok(travelCommentService.findList(travelId));
    }

}
