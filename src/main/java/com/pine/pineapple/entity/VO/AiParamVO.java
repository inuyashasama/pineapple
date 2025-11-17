package com.pine.pineapple.entity.VO;

import lombok.Data;

@Data
public class AiParamVO {
    private String prompt;
    private String model;
    private Long userId;
    private String message;
    private String imageUrl;
}
