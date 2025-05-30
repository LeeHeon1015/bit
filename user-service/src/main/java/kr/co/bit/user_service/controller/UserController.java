package kr.co.bit.user_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.co.bit.user_service.model.JwtResponseDto;
import kr.co.bit.user_service.model.LoginRequestDto;
import kr.co.bit.user_service.model.MessageResponseDto;
import kr.co.bit.user_service.model.User;
import kr.co.bit.user_service.model.UserSignupRequestDto;
import kr.co.bit.user_service.service.AuthService;
import kr.co.bit.user_service.service.UserDao;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping( "/user" )
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger( UserController.class );
    @Autowired
    private UserDao userDao;
    @Autowired
    private AuthService authService;
    @GetMapping
    public Mono<Resource> user() {
        return Mono.just( new ClassPathResource( "static/index.html" ) );
    }    
    @GetMapping( "/{id}" )
    public User getUser ( @PathVariable String id ) {
        return userDao.getUser( id );
    }
    @PostMapping("/input")
    public ResponseEntity<?> inputUser( @Valid @RequestBody UserSignupRequestDto signupDto ) {
        try {
            authService.registerUser( signupDto );
            return ResponseEntity.ok( new MessageResponseDto( "회원 가입 성공") );
        } catch ( IllegalArgumentException e ) {
            logger.warn( "아이디 중복" );
            return ResponseEntity.status( HttpStatus.BAD_REQUEST )     //404 에러 
            .body( new MessageResponseDto("아이디 중복") );
        } catch ( Exception e ) {
            logger.warn( "예외 발생" );
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ) //500 에러
            .body( new MessageResponseDto( "회원 가입 오류" ) );
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser( @Valid @RequestBody LoginRequestDto requestDto ) {
        try {
            JwtResponseDto jwtResponseDto = authService.authenticateUser(requestDto);
            return ResponseEntity.ok( jwtResponseDto );
        } catch ( AuthenticationException e ) {
            logger.warn( "로그인 실패 : 아이디와 비밀번호가 다릅니다");
            return ResponseEntity.status( HttpStatus.UNAUTHORIZED )     //401 에러
            .body( new MessageResponseDto( "인증 실패" ) );
        } catch ( Exception e ) {
            logger.warn( "로그인 오류");
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ) //500 에러
            .body( new MessageResponseDto( "로그인 실패" ) );
        }
    }
}
