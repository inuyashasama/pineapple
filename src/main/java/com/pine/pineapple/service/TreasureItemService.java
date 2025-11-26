package com.pine.pineapple.service;

import com.pine.pineapple.entity.TreasureItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ThinkPad
* @description 针对表【treasure_item】的数据库操作Service
* @createDate 2025-11-25 14:11:32
*/
public interface TreasureItemService extends IService<TreasureItem> {

    List<TreasureItem> getAllTreasureItems();
    void saveTreasureItem(TreasureItem item);
    void deleteTreasureItem(String id);

}
