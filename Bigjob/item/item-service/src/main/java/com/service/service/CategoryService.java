package com.service.service;

import com.item.pojo.Category;
import com.service.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
private CategoryMapper categoryMapper;


    public List<Category> queryCategoryById(Long pid) {
        Category category = new Category();
        category.setParentId(pid);

        return  categoryMapper.select(category);

    }

     public List<String>  queryNamesByIds(List<Long> ids){
         List<Category> categories = this.categoryMapper.selectByIdList(ids);
      return   categories.stream().map(category -> category.getName()).collect(Collectors.toList());

     }
}
