package kr.co.bit.user_service.security;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import kr.co.bit.user_service.model.User;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger( JwtTokenProvider.class );
    private SecretKey key;
    private long jwtExprirationMs;
    public JwtTokenProvider( @Value( "${jwt.secret}" ) String secretString, 
        @Value( "${jwt.expiration}" ) long jwtExpirationMsConfig ) {
        byte[] keyBytes = Decoders.BASE64.decode( secretString );
        this.key = Keys.hmacShaKeyFor( keyBytes );
        this.jwtExprirationMs = jwtExpirationMsConfig;
        logger.info( "JWT Secket Key 초기화 : " + key.getAlgorithm() );
    }
    public String generateToken( User user ) {
        Date now = new Date();
        Date expiryDate = new Date( now.getTime() + jwtExprirationMs );
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put( "name", user.getName() );
        String role = user.getRole();           // ROLE_USER -> USER
        if( role != null && ! role.trim().isEmpty() ) {
            if( ! role.toUpperCase().startsWith( "ROLE_" ) ) {
                role = "ROLE_" + role.toUpperCase();
            }   
        } else {
            role = "ROLE_USER";
            logger.warn( "User ROLE is null" );
        }
        claims.put( "role", role );
        return Jwts.builder()
            .setSubject( user.getId() )
            .addClaims( claims )
            .setIssuedAt( now )
            .setExpiration( expiryDate )
            // 토큰 위변조를 막기 위해서
            .signWith( key, SignatureAlgorithm.HS512 )
            .compact();
    }
    // 토큰에서 사용자 ID 추출
    public String getUserIdFromJWT( String token ) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey( key )
            .build()
            .parseClaimsJws( token )
            .getBody();
        return claims.getSubject();
    }
    // 토근의 유효성 검증
    public boolean validateToken( String authToken ) {
        try {
            Jwts.parserBuilder().setSigningKey( key ).build().parseClaimsJws( authToken );
            return true;
        } catch( SignatureException e ) {
            logger.error( "유요하지 않는 JWT 서명입니다" );    
        } catch( MalformedJwtException e ) {
            logger.error( "잘못된 JWT 토큰 형식입니다" );
        } catch( ExpiredJwtException e ) {
            logger.error( "만료된 JWT 토큰입니다" );
        } catch( UnsupportedJwtException e ) {
            logger.error( "지원하지 않는 JWT 토큰입니다" );
        } catch( IllegalArgumentException e ) {
            logger.error( "JWT 클레임 문자열이 비어있습니다" );
        }
        return false;
    }

}
