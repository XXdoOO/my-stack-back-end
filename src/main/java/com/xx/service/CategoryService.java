package com.xx.service;

import com.xx.mapper.CategoryMapper;
import com.xx.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> getCategories() {
        return categoryMapper.selectList(null);
    }

    public Category getCategory(String name) {
        return categoryMapper.selectById(name);
    }
}
