package kr.co.bit.user_service.security;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.bit.user_service.service.CustomUserDetailsService;

@Component
public class JwtAthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger( JwtAthenticationFilter.class );
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal( HttpServletRequest request, 
        HttpServletResponse response, FilterChain filterChain )
        throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest( request );
            if( StringUtils.hasText( jwt ) && tokenProvider.validateToken( jwt ) ) {
                String userId = tokenProvider.getUserIdFromJWT( jwt );
                UserDetails userDetails = customUserDetailsService.loadUserByUsername( userId );  // id passwd Autirities
                if( userDetails != null ) {
                    UsernamePasswordAuthenticationToken authentication = 
                        //                                      인증된 사용자    비밀번호            권한들    
                        new UsernamePasswordAuthenticationToken( userDetails, null, userDetails.getAuthorities() );
                    // HTTP 요청 정보를 전달
                    authentication .setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
                    SecurityContextHolder.getContext().setAuthentication( authentication );
                    logger.info( "사용자 인증 벙보를 설정했습니다" );
                }
            }
        } catch( Exception e ) {
            logger.error( "유효하지 않은 토큰입니다" );
        }  
        filterChain.doFilter( request, response );      
    }
    public String getJwtFromRequest( HttpServletRequest request ) {
        String bearerToken = request.getHeader( "Authorization" );    
        if( StringUtils.hasText( bearerToken ) && bearerToken.startsWith( "Bearer " ) ) {
           return bearerToken.substring(7 ); 
        }
        return null;
    }
}
