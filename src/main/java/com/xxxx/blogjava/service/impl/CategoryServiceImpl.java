package com.xxxx.blogjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxxx.blogjava.dao.mapper.CategoryMapper;
import com.xxxx.blogjava.dao.pojo.Category;
import com.xxxx.blogjava.service.CategoryService;
import com.xxxx.blogjava.vo.CategoryVo;
import com.xxxx.blogjava.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId, Category::getCategoryName);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        return Result.success(copyList(categories));
    }


    /**
     * 查询文章分类
     *
     * @return {@link Result}
     */
    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        //页面交互的对象
        return Result.success(copyList(categories));
    }

    //    转换功能
    public CategoryVo copy(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }

    public List<CategoryVo> copyList(List<Category> categoryList) {
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }

    @Override
    public Result categoriesDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        return Result.success(copy(category));
    }
}
