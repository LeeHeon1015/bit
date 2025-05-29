package kr.co.bit.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.bit.user_service.model.User;
import kr.co.bit.user_service.service.UserDao;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping( "/user" )
public class UserController {
    @Autowired
    private UserDao userDao;
    @GetMapping
    public Mono<Resource> user() {
        return Mono.just( new ClassPathResource( "static/index.html" ) );
    }    
    @GetMapping( "/{id}" )
    public User getUser ( @PathVariable String id ) {
        return userDao.getUser( id );
    }
}
