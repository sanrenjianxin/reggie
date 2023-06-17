package com.lrt.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lrt.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
