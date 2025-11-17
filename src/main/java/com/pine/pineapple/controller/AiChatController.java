package com.pine.pineapple.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam;
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesis;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesisParam;
import com.alibaba.dashscope.aigc.videosynthesis.VideoSynthesisResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.MultiModalMessage;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.exception.UploadFileException;
import com.alibaba.dashscope.utils.JsonUtils;
import com.pine.pineapple.common.utils.Result;
import com.pine.pineapple.entity.VO.AiParamVO;
import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.*;


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
     * 图片转文字
     *
     * @return 响应结果
     */
    @RequestMapping(value = "/image2Text")
    public String aiImage2Text(@RequestBody AiParamVO aiParamVO) throws NoApiKeyException, UploadFileException {
        MultiModalConversation conv = new MultiModalConversation();
        Map<String, Object> map = new HashMap<>();
        map.put("image", aiParamVO.getImageUrl());
        map.put("max_pixels", "1003520");
        map.put("min_pixels", "3136");
        MultiModalMessage userMessage = MultiModalMessage.builder().role(Role.USER.getValue())
                .content(Arrays.asList(
                        map,
                        //为保证识别效果，目前模型内部会统一使用"Read all the text in the image."进行识别，用户输入的文本不会生效。
                        Collections.singletonMap("text", "Read all the text in the image."))).build();
        MultiModalConversationParam param = MultiModalConversationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(apiKey)
                .model("qwen-vl-ocr")
                .message(userMessage)
                .build();
        MultiModalConversationResult result = conv.call(param);
        return result.getOutput().getChoices().get(0).getMessage().getContent().get(0).get("text").toString();
    }

    /**
     * 文本转视频
     *
     * @param aiParamVO 参数
     * @return 响应结果
     */
    @RequestMapping(value = "/text2video")
    public Result<String> text2video(@RequestBody AiParamVO aiParamVO) throws NoApiKeyException, InputRequiredException {
        String redisKey = "user:free:" + aiParamVO.getUserId() + ":" + DateUtil.format(new Date(), "yyyyMMdd");
        if (isFree(redisKey) && !aiParamVO.getUserId().equals(1000001L)){
            throw new RuntimeException("您已使用完免费次数，请充值");
        }

        updateUsageCount(redisKey);
        return Result.ok(text2Video(aiParamVO.getPrompt()));
    }


    /**
     * 文本转视频
     *
     * @param prompt 文本
     * @return 视频地址
     */
    public String text2Video(String prompt) throws ApiException, NoApiKeyException, InputRequiredException {
        VideoSynthesis vs = new VideoSynthesis();
        VideoSynthesisParam param =
                VideoSynthesisParam.builder()
                        .apiKey(apiKey)
                        .model("wan2.5-t2v-preview")
                        .prompt(prompt)
                        .size("1280*720")
                        .build();
        System.out.println("please wait...");
        VideoSynthesisResult result = vs.call(param);
        System.out.println(JsonUtils.toJson(result));
        String videoUrl = result.getOutput().getVideoUrl();
        String taskStatus = result.getOutput().getTaskStatus();
        if (taskStatus.equals("SUCCEEDED")){
            return videoUrl;
        }else {
            return JsonUtils.toJson(result);
        }
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
