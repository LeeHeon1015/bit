package kr.co.bit.user_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.bit.user_service.model.JwtResponseDto;
import kr.co.bit.user_service.model.LoginRequestDto;
import kr.co.bit.user_service.model.User;
import kr.co.bit.user_service.model.UserSignupRequestDto;
import kr.co.bit.user_service.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor                // final @NonNull 자동으로 생성자를 생성
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger( AuthService.class );
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    // 회원 가입
    @Transactional
    public User registerUser( UserSignupRequestDto signupDto ) {
        // 아이디 중복 확인
        if( mapper.existsUserById( signupDto.getId() ) > 0 ) {
            // 아이디가 있다
            logger.warn( "입력한 아이디가 있습니다" );
            throw new IllegalArgumentException( "입력한 아이디가 있습니다");
        }
        // ROLE_ADMIN 권한 가입 제한
        String role = signupDto.getRole();
        if( role.equals( "ROLE_ADMIN" ) 
            && ! "admin".equals( signupDto.getId() ) ) {
            logger.warn( "ADMIN 권한은 admin만 가능합니다." );
            role = "ROLE_USER";
        }
        User user = User.builder()
            .id(signupDto.getId())
            .passwd( passwordEncoder.encode( signupDto.getPasswd() ) )
            .name( signupDto.getName() )
            .tel( signupDto.getTel() )
            .role( role )
            .build();
        mapper.insertUser( user );
        logger.info( "회원가입 성공" );
        return user;
    }
    // 사용자 인증
    public JwtResponseDto authenticateUser( LoginRequestDto requestDto ) {
        // 인증 실패시 AuthenticationException 발생
        Authentication authentication = authenticationManager.authenticate( 
            new UsernamePasswordAuthenticationToken( requestDto.getId(), requestDto.getPasswd() )
        );
        // 인증됐음을 시스템에 알려주는 역할
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = mapper.getUser( requestDto.getId() );
        String jwt = jwtTokenProvider.generateToken( user );
        logger.info( "토큰 생성 성공" );
        return new JwtResponseDto( jwt, user.getId(),user.getName(), user.getRole(), "Bearer" );
    }
}
