package com.lrt.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lrt.reggie.dto.DishDto;
import com.lrt.reggie.entity.Dish;
import com.lrt.reggie.entity.DishFlavor;
import com.lrt.reggie.mapper.DishMapper;
import com.lrt.reggie.service.DishFlavorService;
import com.lrt.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品
     * @param dishDto
     */
    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        // 保存菜品的基本信息到菜品表dish
        this.save(dishDto);
        // 获取菜品的id
        Long dishId = dishDto.getId();
        // 获取当前菜品的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        // 将每个口味字段的dishId属性赋值
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
        }
        // 保存菜品口味数据到dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 从dish表查询菜品基本信息
        Dish dish = this.getById(id);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(id != null, DishFlavor::getDishId, id);
        // 从dish_flavors表查询当前菜品对应的口味信息
        List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(dishFlavors);
        return dishDto;
    }
}
