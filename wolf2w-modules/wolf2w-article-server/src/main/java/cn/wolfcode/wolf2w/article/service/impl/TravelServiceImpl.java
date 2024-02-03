package cn.wolfcode.wolf2w.article.service.impl;

import cn.wolfcode.wolf2w.article.domain.Travel;
import cn.wolfcode.wolf2w.article.domain.TravelContent;
import cn.wolfcode.wolf2w.article.feign.UserInfoFeignService;
import cn.wolfcode.wolf2w.article.mapper.TravelContentMapper;
import cn.wolfcode.wolf2w.article.mapper.TravelMapper;
import cn.wolfcode.wolf2w.article.qo.TravelQuery;
import cn.wolfcode.wolf2w.article.service.TravelService;
import cn.wolfcode.wolf2w.article.vo.TravelRange;
import cn.wolfcode.wolf2w.auth.util.AuthenticationUtils;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import cn.wolfcode.wolf2w.user.dto.UserInfoDTO;
import cn.wolfcode.wolf2w.user.vo.LoginUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月15日 上午 10:37
 */
@Service
public class TravelServiceImpl extends ServiceImpl<TravelMapper, Travel> implements TravelService {
    @Autowired
    private UserInfoFeignService userInfoFeignService;
    @Autowired
    private ThreadPoolExecutor businessThreadPoolExecutor;
    @Autowired
    private TravelContentMapper travelContentMapper;

    @Override
    public Page<Travel> pageList(TravelQuery query) {
        QueryWrapper<Travel> wrapper = new QueryWrapper<Travel>()
                .eq(query.getDestId()!=null,"dest_id",query.getDestId());
        if (query.getTravelTimeRange()!=null){
            TravelRange timeRange = query.getTravelTimeRange();
            wrapper.between("MONTH(travel_time)",timeRange.getMin(),timeRange.getMax());
        }
        if (query.getCostRange()!=null){
            TravelRange costRange = query.getCostRange();
            wrapper.between("avg_consume",costRange.getMin(),costRange.getMax());
        }
        if (query.getDayRange()!=null){
            TravelRange dayRange = query.getDayRange();
            wrapper.between("day",dayRange.getMin(),dayRange.getMax());
        }
        wrapper.orderByDesc(query.getOrderBy());

        LoginUser user = AuthenticationUtils.getUser();
        if (user==null){
            wrapper.eq("ispublic",Travel.ISPUBLIC_YES).eq("state",Travel.STATE_RELEASE);
        }else {
            wrapper.and(w->{
                w.eq("author_id",user.getId()).or(ww->{
                    ww.eq("ispublic",Travel.ISPUBLIC_YES).eq("state",Travel.STATE_RELEASE);
                });
            });
        }

        Page<Travel> page = super.page(new Page<>(query.getCurrent(), query.getSize()), wrapper);
        List<Travel> records = page.getRecords();
        //创建计数器
        CountDownLatch latch = new CountDownLatch(records.size());
        for (Travel t : records) {
            businessThreadPoolExecutor.execute(()->{
                R<UserInfoDTO> userInfoDTO = userInfoFeignService.getById(t.getAuthorId());
                if (userInfoDTO.getCode()!=R.CODE_SUCCESS){
                    latch.countDown();
                    return;
                }
                UserInfoDTO data = userInfoDTO.getData();
                t.setAuthor(data);
                latch.countDown();
            });

        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return page;
    }


    @Override
    public Travel getById(Serializable id) {
        Travel travel = super.getById(id);
        if (travel==null){
            return null;
        }
        TravelContent travelContent = travelContentMapper.selectById(id);
        travel.setContent(travelContent);
        R<UserInfoDTO> userInfoDTOR = userInfoFeignService.getById(travel.getAuthorId());
        UserInfoDTO data = userInfoDTOR.checkAndGet();
        travel.setAuthor(data);
        return travel;
    }
}
