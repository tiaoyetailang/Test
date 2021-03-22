package com.service.controller;

import com.cl.pojo.PageResult;
import com.item.pojo.Brand;
import com.service.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("brand")
public class BrandController {
    @Autowired
    BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value ="key",required = false) String key,
            @RequestParam(value ="page",defaultValue = "1") Integer page,
            @RequestParam(value ="rows",defaultValue = "5") Integer rows,
            @RequestParam(value ="sortBy",required = false) String sortBy,
            @RequestParam(value ="desc",required =false ) Boolean desc
    ){
        PageResult<Brand> result=    brandService.queryBrandByPage(key,page,rows,sortBy,desc);
        if(result==null|| CollectionUtils.isEmpty(result.getItems())){
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(result);


    }

    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam List<Long> cids ){
        this.brandService.saveBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid")Long cid){
        List<Brand> brands=  this.brandService.queryBrandByCid(cid);
        if(CollectionUtils.isEmpty(brands)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brands);

    }
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandByIds(@PathVariable("id")Long id){
      Brand brand=  this.brandService.queryBrandById(id);
      if(StringUtils.isEmpty(brand)){
          return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(brand);
    }
}
