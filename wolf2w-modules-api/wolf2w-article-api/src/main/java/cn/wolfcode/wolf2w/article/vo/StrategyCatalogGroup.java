package cn.wolfcode.wolf2w.article.vo;

import cn.wolfcode.wolf2w.article.domain.StrategyCatalog;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class StrategyCatalogGroup {

    private Long destId;
    private String destName;
    private List<StrategyCatalog> catalogList = new ArrayList<>();
}
