package kr.co.bit.user_service.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
// react front 에서 로그인 정보로 요청을 보내면 받을 용도
public class LoginRequestDto {
    @NotBlank( message = "아이디를 입력해 주세요" )
    private String id;
    @NotBlank( message = "비밀번호를 입력해 주세요" )
    private String passwd;
}
