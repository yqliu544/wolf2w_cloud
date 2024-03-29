package cn.wolfcode.wolf2w.article.service;

import cn.wolfcode.wolf2w.article.domain.Region;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月11日 下午 4:47
 */
public interface RegionService extends IService<Region> {
    List<Region> findHotList();

}
