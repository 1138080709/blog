package com.wu.blog.system.dao;

import com.wu.blog.system.entity.Article;

public interface ArticleMapper {
    Article selectByPrimaryKey(Integer articleId);
}