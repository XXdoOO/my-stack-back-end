package com.xx.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xx.pojo.dto.DictDTO;
import com.xx.pojo.entity.DictType;
import com.xx.service.DictService;
import com.xx.util.MyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class DictController {
    @Autowired
    private DictService dictService;

    @ResponseBody
    @GetMapping("dictType")
    public MyResponse getDictType(DictDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        return MyResponse.success(new PageInfo<>(dictService.getDictType(dto)));
    }

    @ResponseBody
    @PostMapping("dictType")
    public MyResponse postDictType(DictDTO dto) {
        return dictService.postDictType(dto) ? MyResponse.success() : MyResponse.fail();
    }

    @ResponseBody
    @PutMapping("dictType")
    public MyResponse putDictType(DictDTO dto) {
        return dictService.putDictType(dto) ? MyResponse.success() : MyResponse.fail();
    }

    @ResponseBody
    @DeleteMapping("dictType")
    public MyResponse deleteDictType(@RequestParam long id) {
        return dictService.deleteDictType(id) ? MyResponse.success() : MyResponse.fail();
    }

    @ResponseBody
    @GetMapping("dictData")
    public MyResponse getDictData(DictDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        return MyResponse.success(new PageInfo<>(dictService.getDictData(dto)));
    }

    @ResponseBody
    @PostMapping("dictData")
    public MyResponse postDictData(DictDTO dto) {
        return dictService.postDictData(dto) ? MyResponse.success() : MyResponse.fail();
    }

    @ResponseBody
    @PutMapping("dictData")
    public MyResponse putDictData(DictDTO dto) {
        return dictService.putDictData(dto) ? MyResponse.success() : MyResponse.fail();
    }

    @ResponseBody
    @DeleteMapping("dictData")
    public MyResponse deleteDictData(@RequestParam long id) {
        return dictService.deleteDictData(id) ? MyResponse.success() : MyResponse.fail();
    }
}
