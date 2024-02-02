package cn.wolfcode.wolf2w.search.qo;

import cn.wolfcode.wolf2w.redis.core.qo.QueryObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchQueryObject extends QueryObject {

    private Integer type = -1;
}
