package kr.co.bit.user_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RefreshScope               // Config Server 값 변경시 자동 갱신
public class IndexController {
    @Value( "${spring.application.name}" )
    private String appName;
    @Value( "${my.config.custom.property}" )
    private String customProp;
    @Value( "${my.config.user.property}" )
    private String userProp;
    @GetMapping( "/" )
    public String index( Model model ) {
        System.out.println( "Application Name : " + appName );
        System.out.println( "Custome Property : " + customProp );
        System.out.println( "User Property : " + userProp );
        return "forward:index.html";
    }    
}
