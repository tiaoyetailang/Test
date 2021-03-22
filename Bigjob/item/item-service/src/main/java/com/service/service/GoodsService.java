package com.service.service;

import com.cl.pojo.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.item.pojo.*;
import com.service.mapper.*;
import com.item.pojo.vo.SpuBo;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsService {
    @Autowired
    SpuMapper spuMapper;

    @Autowired
    SpuDetailMapper spuDetailMapper;

    @Autowired
    BrandMapper brandMapper;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    SkuMapper skuMapper;
    @Autowired
    StockMapper stockMapper;
    @Autowired
    AmqpTemplate amqpTemplate;

    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {

        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(key)){
        criteria.andLike("title","%"+key+"%");}
        if(saleable!=null){
            criteria.andEqualTo("saleable",saleable);
        }
        PageHelper.startPage(page,rows);
        List<Spu> spus = this.spuMapper.selectByExample(example);
        PageInfo<Spu> pageInfo = new PageInfo<>(spus);


        List<SpuBo> spuBos = spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);
            Brand brand = this.brandMapper.selectByPrimaryKey(spu.getBrandId());

            List<String> names = this.categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

            spuBo.setCname(StringUtils.join(names, "-"));
            spuBo.setBname(brand.getName());

            return spuBo;
        }).collect(Collectors.toList());


        return new PageResult<SpuBo>(pageInfo.getTotal(),spuBos);
    }

@Transactional
    public void saveGoods(SpuBo spuBo) {
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());

        this.spuMapper.insertSelective(spuBo);

        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insertSelective(spuDetail);
       spuBo.getSkus().forEach(sku -> {
           sku.setId(null);
           sku.setSpuId(spuBo.getId());
           sku.setCreateTime(new Date());
           sku.setLastUpdateTime(sku.getCreateTime());
           this.skuMapper.insertSelective(sku);
           Stock stock = new Stock();
           stock.setSkuId(sku.getId());
           stock.setStock(sku.getStock());
           this.stockMapper.insert(stock);

      //sendMessage("insert",spuBo.getId());



       });





    }
    public void sendMessage(String type,Long id){
          try{
              this.amqpTemplate.convertAndSend("item."+type,id);
          }catch (Exception e ){
               e.printStackTrace();
          }
    }

    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return  this.spuDetailMapper.selectByPrimaryKey(spuId);


    }

    public List<Sku> querySkuBySpuId(Long id) {
        Sku record = new Sku();
        record.setSpuId(id);
      List<Sku> skus =this.skuMapper.select( record);
      skus.forEach(sku -> {
            sku.setStock( this.stockMapper.selectByPrimaryKey(sku.getId()).getStock());
        });
        return skus;
    }

    public Spu querySpuById(Long id) {
        return spuMapper.selectByPrimaryKey(id);
    }

    public Sku querySkuById(Long id) {
        return this.skuMapper.selectByPrimaryKey(id);
    }
}
