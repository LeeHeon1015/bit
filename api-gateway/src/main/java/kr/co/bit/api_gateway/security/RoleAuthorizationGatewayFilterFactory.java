package kr.co.bit.api_gateway.security;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Component
public class RoleAuthorizationGatewayFilterFactory 
    extends AbstractGatewayFilterFactory <RoleAuthorizationGatewayFilterFactory.Config> {
    private static final Logger logger = LoggerFactory.getLogger( RoleAuthorizationGatewayFilterFactory.class );
    public RoleAuthorizationGatewayFilterFactory() {
        super( Config.class );
    }
    @Override
    public GatewayFilter apply(Config config) {
        return ( exchange, chain ) -> {
            List<String> userRoles = (List<String>) exchange.getAttributes().get( "userRoles" );
            String userId = (String) exchange.getAttributes().get( "userId" );
            String requiredRoleFromConfig = config.getRequiredRole();
            if( requiredRoleFromConfig == null || requiredRoleFromConfig.trim().isEmpty() ) {
                logger.error( "권한 설정이 없습니다." );
                onError( exchange, "권한 설정이 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR );  // 500
            }
            String roleToCheck = requiredRoleFromConfig.toUpperCase().startsWith( "ROLE_" )
                ? requiredRoleFromConfig.toUpperCase()
                : "ROLE_" + requiredRoleFromConfig.toUpperCase();
            if( userRoles.stream().anyMatch( role -> role.equalsIgnoreCase( roleToCheck ) ) ) {
                logger.info( "권한 설정 확인 성공" );
                return chain.filter( exchange );            // 다음 필터나 서비스로 요청 전달
            } else {
                logger.warn( "권한이 적절하지 않습니다" );
                return onError( exchange, "권한이 적절하지 않습니다", HttpStatus.FORBIDDEN );
            }
        };
    }
    private Mono<Void> onError( ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus ) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode( httpStatus )      ;
        response.getHeaders().setContentType( MediaType.APPLICATION_JSON );
        String errorJson = String.format( 
            "{" 
                + "\"timestamp\": \"%s\"," 
                + "\"status\": \"%d\"," 
                + "\"error\": \"%s\"," 
                + "\"errorMessage\": \"%s\"," 
                + "\"path\": \"%s\"," 
                + "}",
            OffsetDateTime.now().toString(),            // ISO 8601 형식의 타임스탬프
            httpStatus.value(),
            httpStatus.getReasonPhrase(),
            errorMessage,               
            exchange.getRequest().getPath().value()     // 요청 URL
        );
        DataBuffer buffer = response.bufferFactory().wrap( errorJson.getBytes( StandardCharsets.UTF_8 ) );
        return response.writeWith( Mono.just( buffer ) );
    }

    @Setter
    @Getter
    static class Config {
        private String requiredRole;        
    }        
}
