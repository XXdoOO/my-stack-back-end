package com.xx.service;

import com.xx.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


}
