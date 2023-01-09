package com.xx.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xx.mapper.DictDataMapper;
import com.xx.mapper.DictTypeMapper;
import com.xx.pojo.dto.DictDTO;
import com.xx.pojo.entity.DictData;
import com.xx.pojo.entity.DictType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictService {
    @Autowired
    private DictTypeMapper dictTypeMapper;

    @Autowired
    private DictDataMapper dictDataMapper;

    public List<DictType> getDictType(DictDTO dto) {
        DictType type = new DictType();
        type.setName(dto.getDictName());
        return dictTypeMapper.selectList(new QueryWrapper<>(type));
    }

    public boolean postDictType(DictDTO dto) {
        DictType type = new DictType();
        type.setName(dto.getDictName());
        return dictTypeMapper.insert(type) == 1;
    }

    public boolean deleteDictType(long id) {
        return dictTypeMapper.deleteById(id) == 1;
    }

    public boolean putDictType(DictDTO dto) {
        DictType type = new DictType();
        type.setName(dto.getDictName());
        return dictTypeMapper.updateById(type) == 1;
    }


    public List<DictData> getDictData(DictDTO dto) {
        DictData data = new DictData();
        data.setDictName(dto.getDictName());
        data.setLabel(dto.getLabel());
        data.setValue(dto.getValue());
        return dictDataMapper.selectList(new QueryWrapper<>(data));
    }

    public boolean postDictData(DictDTO dto) {
        DictData data = new DictData();
        data.setDictName(dto.getDictName());
        data.setValue(dto.getValue());
        data.setLabel(dto.getLabel());
        return dictDataMapper.insert(data) == 1;
    }

    public boolean deleteDictData(long id) {
        return dictDataMapper.deleteById(id) == 1;
    }

    public boolean putDictData(DictDTO dto) {
        DictData data = new DictData();
        data.setValue(dto.getValue());
        data.setLabel(dto.getLabel());
        return dictDataMapper.updateById(data) == 1;
    }
}
