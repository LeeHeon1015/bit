package kr.co.bit.order_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.bit.order_service.model.User;
import kr.co.bit.order_service.user_service.UserServiceClient;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping( "/order" )
public class OrderController {
    @Autowired
    private UserServiceClient userServiceClient;
    @GetMapping
    public Mono<Resource> order() {
        return Mono.just( new ClassPathResource( "static/index.html" ) );
    }    

    @GetMapping("/{id}")
    public User getOrderUserInfo( @PathVariable String id ) {
        return userServiceClient.getUser( id );
    }
}
    

