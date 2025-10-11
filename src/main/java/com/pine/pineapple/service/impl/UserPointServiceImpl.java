package com.pine.pineapple.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pine.pineapple.entity.PointRecord;
import com.pine.pineapple.entity.UserPoint;
import com.pine.pineapple.mapper.PointRecordMapper;
import com.pine.pineapple.service.UserPointService;
import com.pine.pineapple.mapper.UserPointMapper;
import org.redisson.api.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
* @author ThinkPad
* @description 针对表【user_point】的数据库操作Service实现
* @createDate 2025-10-11 15:28:33
*/
@Service
public class UserPointServiceImpl extends ServiceImpl<UserPointMapper, UserPoint>
    implements UserPointService{



}




