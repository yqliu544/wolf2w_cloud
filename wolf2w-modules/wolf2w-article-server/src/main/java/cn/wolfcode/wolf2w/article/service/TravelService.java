package cn.wolfcode.wolf2w.article.service;

import cn.wolfcode.wolf2w.article.domain.Region;
import cn.wolfcode.wolf2w.article.domain.Travel;
import cn.wolfcode.wolf2w.article.qo.TravelQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月11日 下午 4:47
 */
public interface TravelService extends IService<Travel> {

    Page<Travel> pageList(TravelQuery query);
}
