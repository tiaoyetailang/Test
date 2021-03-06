package com.service.mapper;

import com.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BrandMapper  extends Mapper<Brand> {
    @Insert("insert into tb_category_brand(category_id,brand_id) values(#{cid},#{id})")
    void insertCategoryAndBrand(@Param("cid") Long cid,@Param("id") Long id);

    @Select("select * from tb_brand where id in" +
            "(select brand_id from tb_category_brand where  category_id=#{cid})")
    List<Brand> selectBrandByCid(Long cid);
}
