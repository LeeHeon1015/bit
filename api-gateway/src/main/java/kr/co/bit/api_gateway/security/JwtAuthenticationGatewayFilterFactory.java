package kr.co.bit.api_gateway.security;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import reactor.core.publisher.Mono;

// 인증 처리 filter
@Component
public class JwtAuthenticationGatewayFilterFactory 
    extends AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilterFactory.Config> {
    private static final Logger logger = LoggerFactory.getLogger( JwtAuthenticationGatewayFilterFactory.class );
    private SecretKey key;
    private String tokenHeader;
    private String tokenPrefix;
    private String rolesClaimName;
    public JwtAuthenticationGatewayFilterFactory(
        @Value( "${jwt.secret}" ) String key, @Value( "${jwt.header}" ) String tokenHeader, 
        @Value( "${jwt.prefix}" ) String tokenPrefix, @Value( "${jwt.claims.roles}" ) String rolesClaimName ) {
        super( Config.class );
        byte[] keyBytes = Decoders.BASE64.decode( key );
        this.key = Keys.hmacShaKeyFor( keyBytes );
        this.tokenHeader = tokenHeader;
        this.tokenPrefix = tokenPrefix.endsWith( " " ) ? tokenPrefix : tokenPrefix + " ";
        this.rolesClaimName = rolesClaimName;
    }   
    // 실제 적용될 필터. GatewayFilter 인스턴스를 반환     
    @Override
    public GatewayFilter apply( Config config ) {
        return ( exchange, chain ) -> {
            ServerHttpRequest request = exchange.getRequest();
            // JWT 토큰이 없거나 front에서 JWT 없이 요청
            if( ! request.getHeaders().containsKey( tokenHeader ) ) {
                logger.warn( "인증 정보가 없습니다. 로그인을 하세요" );
                return onError( exchange, "미인증", HttpStatus.UNAUTHORIZED );  // 401
            }
            String header = request.getHeaders().getFirst( tokenHeader );
            if( ! StringUtils.hasText( header) || ! header.startsWith( tokenPrefix ) ) {
                logger.warn( "인증 정보가 없습니다. 로그인을 하세요" );
                return onError( exchange, "미인증", HttpStatus.UNAUTHORIZED );  // 401
            }
            String jwt = header.substring( tokenPrefix.length() );
            try {
                Claims claims = Jwts.parserBuilder()
                    .setSigningKey( key )
                    .build()
                    .parseClaimsJws( jwt )
                    .getBody();                          // 페이로드 추출
                String userId = claims.getSubject();     // id
                Object rolesObject = claims.get( rolesClaimName );
                List<String> roles = new ArrayList<String> ();
                if( rolesObject instanceof List ) {                    
                    // @SuppressWarnings( "unchecked" )
                    roles.addAll( (List<String>) rolesObject );                    
                } else if( rolesObject instanceof String ) {
                    // USER,ADMIN
                    roles.add( (String) rolesObject );
                } else if( rolesObject != null ) {
                    // 권한이 처리할 수 없는 형식
                    logger.warn( "처리할 수 없는 형식의 권한입니다" );
                    return onError( exchange, "권한없음", HttpStatus.FORBIDDEN );  // 403        
                }
                exchange.getAttributes().put( "userId", userId );
                exchange.getAttributes().put( "userRoles", roles );
                logger.info("JWT 인증 성공" );
            } catch( SignatureException e ) {
                logger.error( "유요하지 않는 JWT 서명입니다" );    
                return onError( exchange, "유효하지 않은 JWT 서명입니다", HttpStatus.UNAUTHORIZED );  // 401
            } catch( MalformedJwtException e ) {
                logger.error( "잘못된 JWT 토큰 형식입니다" );
                return onError( exchange, "잘못된 JWT 토큰 형식입니다", HttpStatus.UNAUTHORIZED );  // 401
            } catch( ExpiredJwtException e ) {
                logger.error( "만료된 JWT 토큰입니다" );
                return onError( exchange, "만료된 JWT 토큰입니다", HttpStatus.UNAUTHORIZED );  // 401
            } catch( UnsupportedJwtException e ) {
                logger.error( "지원하지 않는 JWT 토큰입니다" );
                return onError( exchange, "지원하지 않는 JWT 토큰입니다", HttpStatus.UNAUTHORIZED );  // 401
            } catch( IllegalArgumentException e ) {
                logger.error( "JWT 클레임 문자열이 비어있습니다" );
                return onError( exchange, "JWT 클레임 문자열이 비어있습니다", HttpStatus.UNAUTHORIZED );  // 401
            }
            return chain.filter( exchange );
        };
    }
    private Mono<Void> onError( ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus ) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode( httpStatus );
        response.getHeaders().setContentType( MediaType.APPLICATION_JSON);
        String errorJson = String.format( 
           "{"
                + "\"timestamp\":\"%s\","
                + "\"status\":\"%d\","
                + "\"error\":\"%s\","
                + "\"errorMessage\":\"%s\","
                + "\"path\":\"%s\","
                + "}",
           OffsetDateTime.now().toString(),             // ISO 8601 형식의 타밍스탬프
           httpStatus.value(),
           httpStatus.getReasonPhrase(),
           errorMessage,
           exchange.getRequest().getPath().value()      // 요청 URL
        );
        DataBuffer buffer = response.bufferFactory().wrap( errorJson.getBytes( StandardCharsets.UTF_8 ) ); 
        return response.writeWith( Mono.just( buffer ) );
    }
    // application.properties args를 통해서 전달
    static class Config {
        // 필터 설정을 위한 멤버 변수 정의
        // private boolean requireSpecificClaim;
    }
}
