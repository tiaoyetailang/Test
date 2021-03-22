package com.cl.controller;

import com.cl.pojo.Goods;
import com.cl.pojo.PageResult;
import com.cl.pojo.SearchRequest;
import com.cl.pojo.SearchResult;
import com.cl.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    SearchService searchService;

    @PostMapping("page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest    request){
        SearchResult result=  searchService.search(request);
           if(StringUtils.isEmpty(request)){
               return  ResponseEntity.notFound().build();
           }
           return ResponseEntity.ok(result);
    }



}
