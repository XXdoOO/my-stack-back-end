package com.xx.service;

import com.xx.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
    @Autowired
    private CommonMapper commonMapper;

    public boolean enabledItem(String table, long id) {
        return commonMapper.enableItem(table, id) == 1;
    }

    public boolean deleteItem(String table, long id) {
        return commonMapper.deleteItem(table, id) == 1;
    }
}
