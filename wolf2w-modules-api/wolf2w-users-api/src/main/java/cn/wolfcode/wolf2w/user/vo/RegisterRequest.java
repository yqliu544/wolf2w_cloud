package cn.wolfcode.wolf2w.user.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequest {
    private String phone;
    private String nickname;
    private String password;
    private String verifyCode;

}
