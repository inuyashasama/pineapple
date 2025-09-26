package com.pine.pineapple.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pine.pineapple.entity.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where username = #{username} limit 1")
    User findByUsername(String username);
}

