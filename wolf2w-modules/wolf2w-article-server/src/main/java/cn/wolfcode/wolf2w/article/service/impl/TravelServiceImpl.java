package cn.wolfcode.wolf2w.article.service.impl;

import cn.wolfcode.wolf2w.article.domain.StrategyTheme;
import cn.wolfcode.wolf2w.article.domain.Travel;
import cn.wolfcode.wolf2w.article.feign.UserInfoFeignService;
import cn.wolfcode.wolf2w.article.mapper.StrategyThemeMapper;
import cn.wolfcode.wolf2w.article.mapper.TravelMapper;
import cn.wolfcode.wolf2w.article.qo.TravelQuery;
import cn.wolfcode.wolf2w.article.service.TravelService;
import cn.wolfcode.wolf2w.article.vo.TravelRange;
import cn.wolfcode.wolf2w.redis.core.utils.R;
import cn.wolfcode.wolf2w.user.dto.UserInfoDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月15日 上午 10:37
 */
@Service
public class TravelServiceImpl extends ServiceImpl<TravelMapper, Travel> implements TravelService {
    @Autowired
    private UserInfoFeignService userInfoFeignService;

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
        Page<Travel> page = super.page(new Page<>(query.getCurrent(), query.getSize()), wrapper);
        List<Travel> records = page.getRecords();
        for (Travel t : records) {
            R<UserInfoDTO> userInfoDTO = userInfoFeignService.getById(t.getAuthorId());
            if (userInfoDTO.getCode()!=R.CODE_SUCCESS){
                continue;
            }
            UserInfoDTO data = userInfoDTO.getData();
            t.setAuthor(data);
        }
        return page;
    }
}
