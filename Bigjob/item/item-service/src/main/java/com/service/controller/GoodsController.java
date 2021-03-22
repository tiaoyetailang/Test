package com.service.controller;

import com.cl.pojo.PageResult;
import com.item.pojo.Sku;
import com.item.pojo.Spu;
import com.item.pojo.SpuDetail;
import com.service.service.GoodsService;
import com.item.pojo.vo.SpuBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GoodsController {

    @Autowired
    GoodsService goodsService;


    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>>  querySpuByPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "saleable",required = false)Boolean saleable,
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows
    ){
        PageResult<SpuBo> result=this.goodsService.querySpuByPage(key,saleable,page,rows);
        if(StringUtils.isEmpty(result)||CollectionUtils.isEmpty(result.getItems())){
            return ResponseEntity.notFound().build();

        }


        return ResponseEntity.ok(result);
    }
    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        this.goodsService.saveGoods(spuBo);

     return     ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId")Long spuId){
        SpuDetail spuDetail=    this.goodsService.querySpuDetailBySpuId(spuId);
        if(StringUtils.isEmpty(spuDetail)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail);

    }

    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id")Long id){
        List<Sku> skus=    this.goodsService.querySkuBySpuId(id);
        if(StringUtils.isEmpty(skus)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(skus);

    }

    @GetMapping("{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
          Spu spu=  this.goodsService.querySpuById(id);
        if(StringUtils.isEmpty(spu)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spu);

    }

    @GetMapping("sku/{id}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("id")Long id){
        Sku sku = this.goodsService.querySkuById(id);
        if (sku == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(sku);
    }

}
