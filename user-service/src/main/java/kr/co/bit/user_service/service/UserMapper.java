package kr.co.bit.user_service.service;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bit.user_service.model.User;

@Mapper
public interface UserMapper {
    public User getUser(String id);
    public int insertUser(User user);
    public int existsUserById(String id);       // 있으면 1 없으면 0
} 
