package cn.wolfcode.wolf2w.comment.service;

import cn.wolfcode.wolf2w.comment.domain.StrategyComment;
import cn.wolfcode.wolf2w.comment.domain.TravelComment;
import cn.wolfcode.wolf2w.comment.qo.CommentQuery;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TravelCommentService {

    Page<TravelComment> page(CommentQuery q);

    void save(TravelComment comment);

    List<TravelComment> findList(Long travelId);
}
