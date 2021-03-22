package com.cl.cliect;

import com.item.api.GoodsApi;
import com.item.pojo.SpuDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {


}
