package com.leyou.order.service.api;

import com.item.api.GoodsApi;
import com.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "zuul", path = "/api/item")
public interface GoodsService extends GoodsApi {
}
