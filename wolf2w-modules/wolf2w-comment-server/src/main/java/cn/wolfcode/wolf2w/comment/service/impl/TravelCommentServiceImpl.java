package cn.wolfcode.wolf2w.comment.service.impl;

import cn.wolfcode.wolf2w.auth.util.AuthenticationUtils;
import cn.wolfcode.wolf2w.comment.domain.StrategyComment;
import cn.wolfcode.wolf2w.comment.domain.TravelComment;
import cn.wolfcode.wolf2w.comment.qo.CommentQuery;
import cn.wolfcode.wolf2w.comment.repository.StrategyCommentRepository;
import cn.wolfcode.wolf2w.comment.repository.TravelCommentRepository;
import cn.wolfcode.wolf2w.comment.service.StrategyCommentService;
import cn.wolfcode.wolf2w.comment.service.TravelCommentService;
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
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TravelCommentServiceImpl implements TravelCommentService {
    @Autowired
    private TravelCommentRepository travelCommentRepository;
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public Page<TravelComment> page(CommentQuery q) {
        Criteria criteria = Criteria.where("travelId").is(q.getArticleId());
        Query query = new Query();
        query.addCriteria(criteria);
        long total = mongoTemplate.count(query, TravelComment.class);
        if (total==0){
            return Page.empty();
        }

        int skip=(q.getCurrent()-1)*q.getSize();
        query.skip(skip).limit(q.getSize());
        query.with(Sort.by(Sort.Direction.DESC,"createTime"));
        List<TravelComment> list = mongoTemplate.find(query, TravelComment.class);
        return new PageImpl<>(list,PageRequest.of(q.getCurrent() -1,q.getSize()),total);
    }

    @Override
    public void save(TravelComment comment) {
        LoginUser user = AuthenticationUtils.getUser();
        comment.setUserId(user.getId());
        comment.setNickname(user.getNickname());
        comment.setCity(user.getCity());
        comment.setLevel(user.getLevel());
        comment.setHeadImgUrl(user.getHeadImgUrl());
        comment.setCreateTime(new Date());
        if (comment.getRefComment()!=null&& StringUtils.hasLength(comment.getRefComment().getId())){
            comment.setType(TravelComment.TRAVLE_COMMENT_TYPE);
        }else {
            comment.setType(TravelComment.TRAVLE_COMMENT_TYPE_COMMENT);
        }
        travelCommentRepository.save(comment);
    }

    @Override
    public List<TravelComment> findList(Long travelId) {
        Query query = new Query().with(Sort.by(Sort.Direction.DESC, "travelId")).addCriteria(Criteria.where("travelId").is(travelId));
        List<TravelComment> comments = mongoTemplate.find(query, TravelComment.class);
        for (TravelComment comment : comments) {
            TravelComment refComment = comment.getRefComment();
            if (refComment!=null&&refComment.getId()!=null) {
                Optional<TravelComment> refCommentOptional = travelCommentRepository.findById(refComment.getId());
                comment.setRefComment(refCommentOptional.orElse(null));
            }
        }
        return comments;
    }
}
