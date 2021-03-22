package com.item.api;

import com.cl.pojo.PageResult;
import com.item.pojo.Brand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("brand")
public interface BrandApi {





    @GetMapping("{id}")
    public Brand queryBrandByIds(@PathVariable("id")Long id);


}
