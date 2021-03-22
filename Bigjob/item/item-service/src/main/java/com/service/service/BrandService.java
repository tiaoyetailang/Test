package com.service.service;

import com.cl.pojo.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.item.pojo.Brand;
import com.service.mapper.BrandMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class BrandService {
    @Autowired
    BrandMapper brandMapper;


    public PageResult<Brand> queryBrandByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(key)){
            criteria.andLike("name","%"+key+"%").orEqualTo("letter",key);


        }

        PageHelper.startPage(page,rows);

        if(StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+" " +(desc?"desc":"asc"));
        }

        List<Brand> brands = brandMapper.selectByExample(example);
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);

        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());
    }

    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        brandMapper.insertSelective(brand);
        cids.forEach(cid->{
            this.brandMapper.insertCategoryAndBrand(cid,brand.getId());
        });
    }

    public List<Brand> queryBrandByCid(Long cid) {


      return  this.brandMapper.selectBrandByCid(cid);

    }

    public Brand queryBrandById(Long id) {

         return  this.brandMapper.selectByPrimaryKey(id);
    }
}
