package cn.wolfcode.wolf2w.article.service.impl;

import cn.wolfcode.wolf2w.article.domain.Destination;
import cn.wolfcode.wolf2w.article.domain.Region;
import cn.wolfcode.wolf2w.article.mapper.DestinationMapper;
import cn.wolfcode.wolf2w.article.mapper.RegionMapper;
import cn.wolfcode.wolf2w.article.qo.DestinationQuery;
import cn.wolfcode.wolf2w.article.service.DestinationService;
import cn.wolfcode.wolf2w.article.service.RegionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月11日 下午 4:48
 */
@Service
public class DestinationServiceImpl extends ServiceImpl<DestinationMapper, Destination> implements DestinationService {

    @Autowired
    private RegionService regionService;

    @Autowired
    private ThreadPoolExecutor  threadPoolExecutor;

    @Override
    public List<Destination> getDestinationByRegionId(Long id) {
        Region region = regionService.getById(id);
        if (region==null){
            return Collections.emptyList();
        }
        List<Long> ids = region.parseRefIds();
        if (ids.size()==0){
            return Collections.emptyList();
        }

        return super.listByIds(ids);
    }

    @Override
    public Page<Destination> pageList(DestinationQuery quary) {
        QueryWrapper<Destination> wrapper = new QueryWrapper<>();
        wrapper.isNull(quary.getParentId()==null,"parent_id");
        wrapper.eq(quary.getParentId()!=null,"parent_id",quary.getParentId());
        wrapper.like((!StringUtils.isEmpty(quary.getKeyword())),"name",quary.getKeyword());
        return super.page(new Page<>(quary.getCurrent(),quary.getSize()),wrapper);
    }

    @Override
    public List<Destination> findToasts(Long destId) {
        ArrayList<Destination> destinations = new ArrayList<>();
        while (destId!=null){
            Destination dest = super.getById(destId);
            if (dest==null){
                break;
            }
            destinations.add(dest);
            destId=dest.getParentId();
        }
        Collections.reverse(destinations);
        return destinations;
    }

    @Override
    public List<Destination> findDestByRid(Long rid) {
        List<Destination> destinations=new ArrayList<>();
        QueryWrapper<Destination> wrapper = new QueryWrapper<>();
        if (rid<0){
            return this.getBaseMapper().selectHotListByRid(rid,null);
        }
        Region region = regionService.getById(rid);
        if (region==null){
            return Collections.emptyList();
        }


        return this.getBaseMapper().selectHotListByRid(rid,region.parseRefIds().toArray(new Long[0]));
    }
}
