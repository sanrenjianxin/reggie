package com.lrt.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lrt.reggie.dto.DishDto;
import com.lrt.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    // 新增菜品，需要操作两张表：dish，dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
}
