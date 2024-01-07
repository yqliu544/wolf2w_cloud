package cn.wolfcode.wolf2w.redis.core.exception;

import cn.wolfcode.wolf2w.redis.core.utils.R;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

    private Integer code= R.CODE_ERROR;
    public BusinessException() {
        super(R.MSG_ERROR);
    }

    public BusinessException(String message) {
        super(message);
    }
    public BusinessException(Integer code,String message) {
        super(message);
        this.code=code;
    }
}
