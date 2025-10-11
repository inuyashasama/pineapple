package com.pine.pineapple.strategy;

import java.util.Map;

public interface BaseStrategy {
    /** 返回文本类型，如 "md"、"txt" */
    String getType();

    /** 初始化接口，如获取token、配置参数等 */
    void init(Map<String, Object> config);

    String getContent(Long id);

    void saveContent(Long id, String content);
}
