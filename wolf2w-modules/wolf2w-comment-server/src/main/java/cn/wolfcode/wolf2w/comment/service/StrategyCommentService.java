package cn.wolfcode.wolf2w.comment.service;

import cn.wolfcode.wolf2w.comment.domain.StrategyComment;
import cn.wolfcode.wolf2w.comment.qo.CommentQuery;
import cn.wolfcode.wolf2w.comment.qo.QueryObject;
import org.springframework.data.domain.Page;

public interface StrategyCommentService {

    Page<StrategyComment> page(CommentQuery q);

    void save(StrategyComment comment);

    void dolike(String cid);

    void replyNumIncr(Long strategyId);
}
