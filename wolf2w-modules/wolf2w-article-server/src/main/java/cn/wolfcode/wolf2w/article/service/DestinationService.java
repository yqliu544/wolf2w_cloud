package cn.wolfcode.wolf2w.article.service;

import cn.wolfcode.wolf2w.article.domain.Destination;
import cn.wolfcode.wolf2w.article.domain.Region;
import cn.wolfcode.wolf2w.article.qo.DestinationQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月11日 下午 4:47
 */
public interface DestinationService extends IService<Destination> {
    List<Destination> getDestinationByRegionId(Long id);

    Page<Destination> pageList(DestinationQuery quary);

    List<Destination> findToasts(Long destId);

    List<Destination> findDestByRid(Long rid);
}
