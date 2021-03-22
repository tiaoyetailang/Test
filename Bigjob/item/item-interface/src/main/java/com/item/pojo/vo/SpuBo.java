package com.item.pojo.vo;

import com.item.pojo.Sku;
import com.item.pojo.Spu;
import com.item.pojo.SpuDetail;

import java.util.List;

public class SpuBo extends Spu {
    String cname;
    String bname;

    SpuDetail spuDetail;

    List<Sku> skus;

    public SpuDetail getSpuDetail() {
        return spuDetail;
    }

    public void setSpuDetail(SpuDetail spuDetail) {
        this.spuDetail = spuDetail;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }
}
