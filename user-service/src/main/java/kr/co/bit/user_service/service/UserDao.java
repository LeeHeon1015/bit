package kr.co.bit.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.bit.user_service.model.User;

@Service
public class UserDao {
    @Autowired
    private UserMapper mapper;
    public User getUser( String id ) {
        return mapper.getUser( id );
    }
}
