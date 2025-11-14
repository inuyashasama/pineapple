package com.pine.pineapple.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;


@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    @Value("${qianwen.api-key}")
    private String apiKey;

    @Resource
    RedissonClient redissonClient;


    /**
     * 聊天接口
     *
     * @param message 输入内容
     * @param model 大模型
     * @return 响应结果
     */
    @PostMapping(value = "/chat")
    public String aiChat(@RequestParam(value = "message", defaultValue = "你是谁？") String message, @RequestParam(value = "model", defaultValue = "qwen-turbo") String model, @RequestParam(value = "userId") Long userId) throws NoApiKeyException, InputRequiredException {
        String redisKey = "user:free:" + userId + ":" + DateUtil.format(new Date(), "yyyyMMdd");

        if (isFree(redisKey) && !userId.equals(1000001L)){
            return "您已使用完免费次数，请充值";
        }

        Generation gen = new Generation();
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant.")
                .build();
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content(message)
                .build();
        GenerationParam param = GenerationParam.builder()
                .apiKey(apiKey)
                .model(model)
                .messages(Arrays.asList(systemMsg, userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .build();


        updateUsageCount(redisKey);

        return gen.call(param).getOutput().getChoices().get(0).getMessage().getContent();
    }


    /**
     * 聊天接口
     *
     * @param message 输入内容
     * @param model 大模型
     * @return 响应结果
     */
    @GetMapping(value = "/chat/stream", produces = "text/html;charset=UTF-8")
    public Flux<String> aiChatStream(@RequestParam(value = "message", defaultValue = "你是谁？") String message, @RequestParam(value = "model", defaultValue = "qwen-turbo") String model,@RequestParam(value = "userId") Long userId) throws NoApiKeyException, InputRequiredException {
        String redisKey = "user:free:" + userId + ":" + DateUtil.format(new Date(), "yyyyMMdd");

        if (isFree(redisKey) && !userId.equals(1000001L)){
            throw new RuntimeException("您已使用完免费次数，请充值");
        }

        Generation gen = new Generation();
        Message userMsg = Message.builder().role(Role.USER.getValue()).content(message).build();
        GenerationParam param = GenerationParam.builder()
                .apiKey(apiKey)
                .model(model)
                .messages(Collections.singletonList(userMsg))
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .incrementalOutput(true)
                .build();

        updateUsageCount(redisKey);

        return Flux.from(gen.streamCall(param)
                .map(GenerationResult::getOutput)
                .map(GenerationOutput::getChoices)
                .map(s -> s.get(0))
                .map(GenerationOutput.Choice::getMessage)
                .map(Message::getContent));
    }


    /**
     * 判断用户是否还有免费额度
     *
     * @param key 用户ID
     * @return 是否还有免费额度
     */
    public boolean isFree(String key) {
        RBucket<Integer> bucket = redissonClient.getBucket(key);
        Integer currentCount = bucket.get();
        // 默认最大次数为10次，可根据需求调整
        int maxCount = 10;
        return (currentCount != null && currentCount >= maxCount);
    }

    /**
     * 更新用户使用次数
     *
     * @param key 用户ID + 日期
     */
    private void updateUsageCount(String key) {
        RBucket<Integer> bucket = redissonClient.getBucket(key);
        Integer currentCount = bucket.get();
        if (currentCount == null) {
            currentCount = 0;
        }
        bucket.set(currentCount + 1, Duration.ofDays(1));
    }
}
