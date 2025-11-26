package com.pine.pineapple.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pine.pineapple.entity.TreasureItem;
import com.pine.pineapple.service.TreasureItemService;
import com.pine.pineapple.mapper.TreasureItemMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author ThinkPad
* @description 针对表【treasure_item】的数据库操作Service实现
* @createDate 2025-11-25 14:11:32
*/
@Service
public class TreasureItemServiceImpl extends ServiceImpl<TreasureItemMapper, TreasureItem>
    implements TreasureItemService{

    @Override
    public List<TreasureItem> getAllTreasureItems(){
        return list();
    }

    @Override
    public void saveTreasureItem(TreasureItem item){
        saveOrUpdate(item);
    }
    @Override
    public void deleteTreasureItem(String id){
        removeById(id);
    }
}




