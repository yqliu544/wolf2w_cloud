package cn.wolfcode.wolf2w.comment.repository;

import cn.wolfcode.wolf2w.comment.domain.StrategyComment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StrategyCommentRepository extends MongoRepository<StrategyComment,String> {
}
