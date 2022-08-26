package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xx.mapper.CategoryBlogMapper;
import com.xx.mapper.CategoryMapper;
import com.xx.pojo.Category;
import com.xx.pojo.CategoryBlog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor=Exception.class)
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryBlogMapper categoryBlogMapper;

    public List<Category> getCategories() {
        return categoryMapper.selectList(null);
    }

    public Category getCategory(String name) {
        return categoryMapper.selectById(name);
    }

    public boolean addCategoryBlog(int id, List<String> categories) {
        QueryWrapper<Category> wrapper = new QueryWrapper<>();

        List<Category> categoryList = categoryMapper.selectList(wrapper.in("name", categories));

        if (categoryList.size() == categories.size()) {
            for (String category : categories) {
                categoryBlogMapper.insert(new CategoryBlog(category, id, null));
            }

            return true;
        }
        return false;
    }

    public int deleteCategoryBlog(int id) {
        QueryWrapper<CategoryBlog> wrapper = new QueryWrapper<>();
        return categoryBlogMapper.delete(wrapper.eq("blog_id", id));
    }

    public boolean updateCategoryBlog(int id, List<String> categories) {
        deleteCategoryBlog(id);
        return addCategoryBlog(id, categories);
    }
}
