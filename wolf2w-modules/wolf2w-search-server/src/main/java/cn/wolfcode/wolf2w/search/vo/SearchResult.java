package cn.wolfcode.wolf2w.search.vo;

import cn.wolfcode.wolf2w.article.dto.DestinationDto;
import cn.wolfcode.wolf2w.article.dto.StrategyDto;
import cn.wolfcode.wolf2w.article.dto.TravelDto;
import cn.wolfcode.wolf2w.user.dto.UserInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SearchResult {

    private List<DestinationDto> dests = new ArrayList<>();
    private List<StrategyDto> strategies = new ArrayList<>();
    private List<TravelDto> travels = new ArrayList<>();
    private List<UserInfoDTO> users = new ArrayList<>();
    private Long total = 0L;
}
