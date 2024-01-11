package cn.wolfcode.wolf2w.article.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@TableName("travel_content")
public class TravelContent implements Serializable {
    private Long id;
    private String content;
}