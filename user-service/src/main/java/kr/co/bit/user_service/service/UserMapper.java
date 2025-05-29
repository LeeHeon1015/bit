package kr.co.bit.user_service.service;

import org.apache.ibatis.annotations.Mapper;

import kr.co.bit.user_service.model.User;

@Mapper
public interface UserMapper {
    public User getUser(String id);
} 
