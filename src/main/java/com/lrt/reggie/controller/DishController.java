package com.lrt.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lrt.reggie.common.R;
import com.lrt.reggie.dto.DishDto;
import com.lrt.reggie.entity.Dish;
import com.lrt.reggie.service.CategoryService;
import com.lrt.reggie.service.DishFlavorService;
import com.lrt.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品信息成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        // 构造dish分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        // 构造dishDto分页构造器
        Page<DishDto> dishDtoPage = new Page<>();
        // 分页查询出dish数据
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(name != null, Dish::getName, name);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, dishLambdaQueryWrapper);
        // 拷贝dish分页构造器中数据到dto分页构造器
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        // 获取查询出的dish数据
        List<Dish> records = pageInfo.getRecords();
        // 将dish数据转换成dto数据（多加了一个从category表查处的categoryName字段）
        List<DishDto> dtoRecords = new ArrayList<>();
        for (Dish record : records) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(record, dishDto);
            String categoryName = categoryService.getById(record.getCategoryId()).getName();
            dishDto.setCategoryName(categoryName);
            dtoRecords.add(dishDto);
        }
        dishDtoPage.setRecords(dtoRecords);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        return R.success(dishService.getByIdWithFlavor(id));
    }

}
