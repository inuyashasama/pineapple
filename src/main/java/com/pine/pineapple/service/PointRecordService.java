package com.pine.pineapple.service;

import com.pine.pineapple.entity.PointRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ThinkPad
* @description 针对表【point_record】的数据库操作Service
* @createDate 2025-10-11 15:26:38
*/
public interface PointRecordService extends IService<PointRecord> {
    void addPoints(Long userId, Integer points, String reason, String orderId);
    boolean usePoints(Long userId, Integer points, String orderId);
    Integer getUserPoints(Long userId);
}
