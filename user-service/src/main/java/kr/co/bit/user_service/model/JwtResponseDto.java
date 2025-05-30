package kr.co.bit.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
// 로그인 성공시 react로 응답할 데이터들 담는 용도
public class JwtResponseDto {
    private String token;
    private String id;
    private String name;
    private String role;
    private String type="Bearer";
}
