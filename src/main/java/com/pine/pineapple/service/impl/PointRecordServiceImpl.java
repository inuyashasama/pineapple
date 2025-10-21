package com.pine.pineapple.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pine.pineapple.entity.PointRecord;
import com.pine.pineapple.entity.UserPoint;
import com.pine.pineapple.mapper.UserPointMapper;
import com.pine.pineapple.service.PointRecordService;
import com.pine.pineapple.mapper.PointRecordMapper;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RFuture;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author ThinkPad
* @description 针对表【point_record】的数据库操作Service实现
* @createDate 2025-10-11 15:26:38
*/
@Service
public class PointRecordServiceImpl extends ServiceImpl<PointRecordMapper, PointRecord>
    implements PointRecordService{

    @Resource
    RedissonClient redisson;

    @Resource
    PointRecordMapper pointRecordMapper;
    @Resource
    UserPointMapper userPointMapper;

    private static final String POINT_KEY_PREFIX = "points:user:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPoints(Long userId, Integer points, String reason, String orderId) {
        // 1. 更新用户总积分
        UserPoint userPoint = userPointMapper.selectById(userId);
        int totalPoints;
        if (userPoint == null) {
            totalPoints = points;
            userPoint = new UserPoint();
            userPoint.setId(userId);
            userPoint.setTotalPoints(points);
            userPoint.setUserId(userId);
            userPointMapper.insert(userPoint);
        } else {
            totalPoints = userPoint.getTotalPoints() + points;
            userPoint.setTotalPoints(totalPoints);
            userPointMapper.updateById(userPoint);
        }

        // 2. 插入签到记录
        PointRecord record = new PointRecord();
        record.setUserId(userId);
        record.setOrderId(orderId);
        record.setChangeType("EARNED");
        record.setPoints(points);
        record.setReason(reason);
        record.setStatus("SUCCESS");

        record.setCreatedAt(new Date());
        pointRecordMapper.insert(record);

        // 3. 异步更新缓存
        String key = POINT_KEY_PREFIX + userId;
        RBucket<Integer> bucket = redisson.getBucket(key);
        RFuture<Integer> future = bucket.getAsync();
        future.whenComplete((current, throwable) -> {
            bucket.set(totalPoints, Duration.ofHours(1));
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean usePoints(Long userId, Integer points, String orderId) {
        RLock lock = redisson.getLock("point:lock:" + userId);
        try {
            lock.lock(10, TimeUnit.SECONDS);

            UserPoint userPoint = userPointMapper.selectById(userId);
            if (userPoint == null || userPoint.getTotalPoints() < points) {
                return false;
            }

            QueryWrapper<UserPoint> query = new QueryWrapper<>();
            query.eq("id", userId);
            userPoint.setTotalPoints(userPoint.getTotalPoints() - points);
            int updated = userPointMapper.update(query);
            if (updated == 0) throw new RuntimeException("乐观锁失败，积分已被修改");

            PointRecord record = new PointRecord();
            record.setUserId(userId);
            record.setOrderId(orderId);
            record.setChangeType("USED");
            record.setPoints(-points);
            record.setReason("积分兑换");
            record.setStatus("SUCCESS");
            record.setCreatedAt(new Date());
            pointRecordMapper.insert(record);

            // 更新缓存
            // 3. 异步更新缓存
            String key = POINT_KEY_PREFIX + userId;
            RBucket<Integer> bucket = redisson.getBucket(key);
            RFuture<Integer> future = bucket.getAsync();
            future.whenComplete((current, throwable) -> {
                Integer newPoints = (current == null ? 0 : current) - points;
                bucket.set(newPoints, Duration.ofHours(1));
            });

            return true;
        } catch (Exception e) {
            throw new RuntimeException("扣减积分失败", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public Integer getUserPoints(Long userId) {
        String key = POINT_KEY_PREFIX + userId;
        RBucket<Integer> bucket = redisson.getBucket(key);
        Integer cache = bucket.get();
        if (cache != null) {
            return cache;
        }
        // 缓存未命中
        UserPoint userPoint = userPointMapper.selectById(userId);
        if (userPoint != null) {
            bucket.set(userPoint.getTotalPoints(), Duration.ofHours(1));
            return userPoint.getTotalPoints();
        } else {
            // 用户不存在，缓存空值，防止穿透
            bucket.set(0, Duration.ofMinutes(10));
            return 0;
        }
    }

    @Override
    public boolean isSigned(Long userId) {
        QueryWrapper<PointRecord> query = new QueryWrapper<>();
        query.eq("user_id", userId);
        query.eq("change_type", "EARNED");
        query.eq("status", "SUCCESS");
        query.eq("reason", "DAILY_SIGN_IN");
        query.eq("created_at", new Date());
        return pointRecordMapper.selectCount(query) > 0;
    }

    @Override
    public List<PointRecord> getUserPointsHistory(Long userId) {
        QueryWrapper<PointRecord> query = new QueryWrapper<>();
        query.eq("user_id", userId);
        return pointRecordMapper.selectList(query);
    }
}




