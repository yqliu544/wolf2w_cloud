package cn.wolfcode.wolf2w.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value = "注册请求")
public class RegisterRequest {
    @ApiModelProperty(name = "phone",notes = "手机号")
    private String phone;
    private String nickname;
    private String password;
    private String verifyCode;

}
