package com.item.api;

import com.cl.pojo.PageResult;
import com.item.pojo.Sku;
import com.item.pojo.Spu;
import com.item.pojo.SpuDetail;
import com.item.pojo.vo.SpuBo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsApi {

    @GetMapping("spu/detail/{spuId}")
    public SpuDetail querySpuDetailBySpuId(@PathVariable("spuId")Long spuId);

    @GetMapping("spu/page")
    public PageResult<SpuBo> querySpuByPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "saleable",required = false)Boolean saleable,
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows
    );

    @GetMapping("sku/list")
    public List<Sku> querySkuBySpuId(@RequestParam("id")Long id);

    @GetMapping("{id}")
    public Spu querySpuById(@PathVariable("id") Long id);

    @GetMapping("sku/{id}")
    public Sku querySkuById(@PathVariable("id")Long id);


}
