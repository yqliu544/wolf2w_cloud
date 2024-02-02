package cn.wolfcode.wolf2w.comment.service.impl;

import cn.wolfcode.wolf2w.auth.util.AuthenticationUtils;
import cn.wolfcode.wolf2w.comment.domain.StrategyComment;
import cn.wolfcode.wolf2w.comment.qo.CommentQuery;
import cn.wolfcode.wolf2w.comment.redis.key.CommentRedisKeyPrefix;
import cn.wolfcode.wolf2w.comment.repository.StrategyCommentRepository;
import cn.wolfcode.wolf2w.comment.service.StrategyCommentService;
import cn.wolfcode.wolf2w.redis.core.utils.RedisCache;
import cn.wolfcode.wolf2w.user.vo.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StrategyCommentServiceImpl implements StrategyCommentService {
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private StrategyCommentRepository strategyCommentRepository;
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public Page<StrategyComment> page(CommentQuery q) {
        Criteria criteria = Criteria.where("strategyId").is(q.getArticleId());
        Query query = new Query();
        query.addCriteria(criteria);
        long total = mongoTemplate.count(query, StrategyComment.class);
        if (total==0){
            return Page.empty();
        }

        int skip=(q.getCurrent()-1)*q.getSize();
        query.skip(skip).limit(q.getSize());
        query.with(Sort.by(Sort.Direction.DESC,"createTime"));
        List<StrategyComment> list = mongoTemplate.find(query, StrategyComment.class);
        return new PageImpl<>(list,PageRequest.of(q.getCurrent() -1,q.getSize()),total);
    }

    @Override
    public void save(StrategyComment comment) {
        LoginUser user = AuthenticationUtils.getUser();
        comment.setUserId(user.getId());
        comment.setNickname(user.getNickname());
        comment.setCity(user.getCity());
        comment.setLevel(user.getLevel());
        comment.setHeadImgUrl(user.getHeadImgUrl());
        comment.setCreateTime(new Date());
        strategyCommentRepository.save(comment);
    }

    @Override
    public void dolike(String cid) {
        Optional<StrategyComment> optional = strategyCommentRepository.findById(cid);
        if (optional.isPresent()){
            StrategyComment strategyComment = optional.get();
            LoginUser user = AuthenticationUtils.getUser();
            if (strategyComment.getThumbuplist().contains(user.getId())) {
                strategyComment.setThumbupnum(strategyComment.getThumbupnum()-1);
                strategyComment.getThumbuplist().remove(user.getId());
            }else {
                strategyComment.setThumbupnum(strategyComment.getThumbupnum()+1);
                strategyComment.getThumbuplist().remove(user.getId());
            }
            strategyCommentRepository.save(strategyComment);
        }
    }

    @Override
    public void replyNumIncr(Long strategyId) {
        redisCache.hashIncrement(CommentRedisKeyPrefix.STRATEGIES_STAT_DATA_MAP,"replynum",1,strategyId+"");
    }
}
