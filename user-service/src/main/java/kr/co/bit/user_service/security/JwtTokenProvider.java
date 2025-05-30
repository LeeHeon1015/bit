package kr.co.bit.user_service.security;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
}
