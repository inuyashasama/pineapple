package com.pine.pineapple.controller;

import com.pine.pineapple.common.utils.Result;
import com.pine.pineapple.entity.PointRecord;
import com.pine.pineapple.service.PointRecordService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-point")
public class PointRecordController {

    @Resource
    PointRecordService pointService;

    @PostMapping("/earn")
    public Result earn(@RequestBody PointRecord dto) {
        pointService.addPoints(dto.getUserId(), dto.getPoints(), dto.getReason(), dto.getOrderId());
        return Result.ok("积分已发放");
    }

    @PostMapping("/use")
    public Result use(@RequestBody PointRecord dto) {
        boolean success = pointService.usePoints(dto.getUserId(), dto.getPoints(), dto.getOrderId());
        return success ? Result.ok("积分使用成功") : Result.fail("积分不足");
    }

    @GetMapping("/{userId}")
    public Result get(@PathVariable Long userId) {
        Integer points = pointService.getUserPoints(userId);
        return Result.ok(points);
    }

    @GetMapping("/getSignStatus/{userId}")
    public Result getSignInStatus(@PathVariable Long userId) {
        boolean signed = pointService.isSigned(userId);
        return Result.ok(signed);
    }

    @GetMapping("/getUserPointsHistory/{userId}")
    public Result getUserPointsHistory(@PathVariable Long userId) {
        List<PointRecord> userPointsHistory = pointService.getUserPointsHistory(userId);
        return Result.ok(userPointsHistory);
    }
}
