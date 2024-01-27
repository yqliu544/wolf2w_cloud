package cn.wolfcode.wolf2w.comment.repository;

import cn.wolfcode.wolf2w.comment.domain.StrategyComment;
import cn.wolfcode.wolf2w.comment.domain.TravelComment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TravelCommentRepository extends MongoRepository<TravelComment,String> {
}
