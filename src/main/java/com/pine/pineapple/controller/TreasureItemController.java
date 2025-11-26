package com.pine.pineapple.controller;

import com.pine.pineapple.common.utils.Result;
import com.pine.pineapple.entity.TreasureItem;
import com.pine.pineapple.service.TreasureItemService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/treasure")
public class TreasureItemController {

    @Resource
    TreasureItemService treasureItemService;

    @RequestMapping("/getAllTreasureItems")
    public Result<List<TreasureItem>> getAllTreasureItems() {
        return Result.ok(treasureItemService.getAllTreasureItems());
    }

    @RequestMapping("/saveTreasureItem")
    public Result<String> saveTreasureItem(@RequestBody TreasureItem item) {
        treasureItemService.saveTreasureItem(item);
        return Result.ok("保存成功");
    }

    @RequestMapping("/deleteTreasureItem/{id}")
    public Result<String> deleteTreasureItem(@PathVariable String id) {
        treasureItemService.deleteTreasureItem(id);
        return Result.ok("删除成功");
    }
}
