package kr.co.bit.user_service.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// 회원가입 용도
@Getter
@Setter
public class UserSignupRequestDto {
    @NotBlank( message = "아이디는 필수항목 입니다" )
    @Size( min = 2, max = 20, message = "아이디는 2자 이상 20자 이하로 입력해 주세요" )
    private String id;
    @NotBlank( message = "비밀번호는 필수항목 입니다" )
    @Size( min = 2, message = "비밀번호는 2자 이상으로 입력해 주세요" )
    private String passwd;
    @NotBlank( message = "이름은 필수항목 입니다" )
    private String name;
    private String tel;
    @NotBlank( message = "권한은 필수항목 입니다." )
    private String role;
}
