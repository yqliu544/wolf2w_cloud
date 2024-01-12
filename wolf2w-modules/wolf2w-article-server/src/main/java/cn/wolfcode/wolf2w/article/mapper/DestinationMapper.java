package cn.wolfcode.wolf2w.article.mapper;

import cn.wolfcode.wolf2w.article.domain.Destination;
import cn.wolfcode.wolf2w.article.domain.Region;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @author: 刘
 * @date: 2024年01月11日 下午 4:39
 */
public interface DestinationMapper extends BaseMapper<Destination> {
    List<Destination> selectHotListByRid(@Param("rid") Long rid, @Param("ids") Long[] ids);

}
