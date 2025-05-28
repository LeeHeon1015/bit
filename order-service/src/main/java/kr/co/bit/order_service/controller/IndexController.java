package kr.co.bit.order_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping
@RefreshScope               // Config Server 값 변경시 자동 갱신
public class IndexController {
    @Value( "${spring.application.name}" )
    private String appName;
    @Value( "${my.config.custom.property}" )
    private String customProp;
    @Value( "${my.config.order.property}" )
    private String orderProp;
    @GetMapping( "/" )
    public String index( Model model ) {
        System.out.println( "Application Name : " + appName );
        System.out.println( "Custome Property : " + customProp );
        System.out.println( "Order Property : " + orderProp );
        return "forward:index.html";
    }    
}
