package com.pine.pineapple.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pine.pineapple.entity.UserPoint;
import com.pine.pineapple.service.UserPointService;
import com.pine.pineapple.mapper.UserPointMapper;
import org.springframework.stereotype.Service;

/**
* @author ThinkPad
* @description 针对表【user_point】的数据库操作Service实现
* @createDate 2025-10-11 15:28:33
*/
@Service
public class UserPointServiceImpl extends ServiceImpl<UserPointMapper, UserPoint>
    implements UserPointService{

}




