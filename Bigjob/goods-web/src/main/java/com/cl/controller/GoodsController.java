package com.cl.controller;

import com.cl.service.GoodsHtmlService;
import com.cl.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class GoodsController {
    @Autowired
    GoodsService goodsService;
    @Autowired
    GoodsHtmlService goodsHtmlService;

      @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id")Long id, Model model){
          Map<String, Object> map = goodsService.loadData(id);
          model.addAllAttributes(map);
            //this.goodsHtmlService.createHtml(id);
          return "item";
      }
}
