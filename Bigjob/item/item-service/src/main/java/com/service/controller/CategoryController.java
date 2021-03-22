package com.service.controller;


import com.item.pojo.Category;
import com.service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("list")
    @ResponseBody
    public ResponseEntity<List<Category>> queryCategoryById(@RequestParam(value = "pid",defaultValue = "0")Long pid){
              if(pid<0||pid==null){
                  return  ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
              }
           List<Category>  categories  =this.categoryService.queryCategoryById(pid);
        System.out.println(categories);
              if(CollectionUtils.isEmpty(categories)){
                  return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

              }
              return ResponseEntity.ok(categories);
    }

    @GetMapping("names")
    public ResponseEntity<List<String>> queryNameByIds(@RequestParam("ids")List<Long> ids){
        List<String> names = this.categoryService.queryNamesByIds(ids);
        if(CollectionUtils.isEmpty(names)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
        return ResponseEntity.ok(names);

    }

}
