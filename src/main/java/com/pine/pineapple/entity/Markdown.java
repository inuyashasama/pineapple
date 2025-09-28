package com.pine.pineapple.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("markdown")
public class Markdown {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String content;

    /** 是否加密存储 */
    private Boolean encrypted;
}
