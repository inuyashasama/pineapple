package com.pine.pineapple.factory;

import com.pine.pineapple.strategy.BaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class ArticleFactory {
    private final Map<String, BaseStrategy> moduleMap = new HashMap<>();

    public ArticleFactory(List<BaseStrategy> modules) {
        modules.forEach(m -> moduleMap.put(m.getType(), m));
    }

    public BaseStrategy getModule(String vendor) {
        BaseStrategy module = moduleMap.get(vendor);
        if (module == null) {
            throw new IllegalArgumentException("Unsupported vendor: " + vendor);
        }
        return module;
    }
}
