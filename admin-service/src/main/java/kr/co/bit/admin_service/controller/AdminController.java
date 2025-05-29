package kr.co.bit.admin_service.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping( "/admin" )
public class AdminController {
    @GetMapping
    public Mono<Resource> admin() {
        return Mono.just( new ClassPathResource( "static/index.html" ) );
    }    
}
