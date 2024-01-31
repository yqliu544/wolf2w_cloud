package cn.wolfcode.wolf2w.article.service;

import cn.wolfcode.wolf2w.article.domain.Banner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BannerService extends IService<Banner> {

    List<Banner> findByType(Integer type);
}
