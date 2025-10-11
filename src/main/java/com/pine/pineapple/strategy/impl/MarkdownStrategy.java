package com.pine.pineapple.strategy.impl;

import com.pine.pineapple.strategy.BaseStrategy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MarkdownStrategy implements BaseStrategy {

    @Override
    public String getType() {
        return "md";
    }

    @Override
    public void init(Map<String, Object> config) {}

    @Override
    public String getContent(Long id){
        return "markdown content";
    }

    @Override
    public void saveContent(Long id, String content){}
}
