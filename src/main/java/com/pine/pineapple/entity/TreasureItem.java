package com.pine.pineapple.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName treasure_item
 */
@TableName(value ="treasure_item")
@Data
public class TreasureItem {
    /**
     * 
     */
    @TableId
    private String id;

    /**
     * 
     */
    private String userId;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private Date purchaseDate;

    /**
     * 
     */
    private BigDecimal price;

    /**
     * 
     */
    private Integer usefulLifeYears;

    /**
     * 
     */
    private String notes;

    /**
     * 
     */
    private String imageUrl;

    /**
     * 
     */
    private String depreciationMethod;

    /**
     * 
     */
    private BigDecimal residualPercent;

    /**
     * 
     */
    private Integer totalUsageHours;

    /**
     * 
     */
    private Integer usedHours;
}