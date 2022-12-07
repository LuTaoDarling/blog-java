package com.xxxx.blogjava.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.blogjava.vo.CategoryVo;
import com.xxxx.blogjava.vo.Result;

import java.util.List;

public interface CategoryService {

    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoriesDetailById(Long id);
}
