package kr.co.bit.user_service.service;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.bit.user_service.model.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserMapper mapper;
    @Override
    public UserDetails loadUserByUsername( String id ) throws UsernameNotFoundException {
        // orElseThrow() 사용하려면 Optinal<user> getUser( String id ) 구현
        User user = mapper.getUser( id );
        if( user == null ) {
            new UsernameNotFoundException( "입력한 사용자가 없습니다");
        }
        String role = user.getRole();
        List<GrantedAuthority> authorities =  Collections.singletonList( new SimpleGrantedAuthority(role));
        return new org.springframework.security.core.userdetails.User(
            user.getId(),
            user.getPasswd(),
            authorities
        );
    }
}
