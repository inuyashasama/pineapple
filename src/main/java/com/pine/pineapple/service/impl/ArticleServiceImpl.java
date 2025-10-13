package com.pine.pineapple.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pine.pineapple.entity.Article;
import com.pine.pineapple.service.ArticleService;
import com.pine.pineapple.mapper.ArticleMapper;
import org.springframework.stereotype.Service;

/**
* @author ThinkPad
* @description 针对表【article】的数据库操作Service实现
* @createDate 2025-10-13 11:13:08
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

}




