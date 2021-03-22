package com.cl.service;

import com.cl.cliect.BrandClient;
import com.cl.cliect.CategoryClient;
import com.cl.cliect.GoodsClient;
import com.cl.cliect.SpecificationClient;
import com.cl.pojo.Goods;
import com.cl.pojo.PageResult;
import com.cl.pojo.SearchRequest;
import com.cl.pojo.SearchResult;
import com.cl.repository.GoodsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.item.pojo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.apache.lucene.search.BooleanQuery;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {
    @Autowired
    CategoryClient categoryClient;

    @Autowired
    BrandClient brandClient;

    @Autowired
    SpecificationClient specificationClient;

    @Autowired
    GoodsRepository goodsRepository;

    final  static ObjectMapper MAPPER=new ObjectMapper();

    @Autowired
    GoodsClient goodsClient;

    public Goods buildGoods(Spu spu) throws Exception {

        // 创建goods对象
        Goods goods = new Goods();

        // 查询品牌
        Brand brand = this.brandClient.queryBrandByIds(spu.getBrandId());

        // 查询分类名称
        List<String> names = this.categoryClient.queryNameByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        // 查询spu下的所有sku
        List<Sku> skus = this.goodsClient.querySkuBySpuId(spu.getId());
        List<Long> prices = new ArrayList<>();
        List<Map<String, Object>> skuMapList = new ArrayList<>();
        // 遍历skus，获取价格集合
        skus.forEach(sku ->{
            prices.add(sku.getPrice());
            Map<String, Object> skuMap = new HashMap<>();
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());
            skuMap.put("image", StringUtils.isNotBlank(sku.getImages()) ? StringUtils.split(sku.getImages(), ",")[0] : "");
            skuMapList.add(skuMap);
        });

        // 查询出所有的搜索规格参数
        List<SpecParam> params = this.specificationClient.queryParams(null, spu.getCid3(), null, true);
        // 查询spuDetail。获取规格参数值
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spu.getId());
        // 获取通用的规格参数
        Map<Long, Object> genericSpecMap = MAPPER.readValue(spuDetail.getGenericSpec(), new TypeReference<Map<Long, Object>>() {
        });
        // 获取特殊的规格参数
        Map<Long, List<Object>> specialSpecMap = MAPPER.readValue(spuDetail.getSpecialSpec(), new TypeReference<Map<Long, List<Object>>>() {
        });
        // 定义map接收{规格参数名，规格参数值}
        Map<String, Object> paramMap = new HashMap<>();
        params.forEach(param -> {
            // 判断是否通用规格参数
            if (param.getGeneric()) {
                // 获取通用规格参数值
                String value = genericSpecMap.get(param.getId()).toString();
                // 判断是否是数值类型
                if (param.getNumeric()){
                    // 如果是数值的话，判断该数值落在那个区间
                    value = chooseSegment(value, param);
                }
                // 把参数名和值放入结果集中
                paramMap.put(param.getName(), value);
            } else {
                paramMap.put(param.getName(), specialSpecMap.get(param.getId()));
            }
        });

        // 设置参数
        goods.setId(spu.getId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setBrandId(spu.getBrandId());
        goods.setCreateTime(spu.getCreateTime());
        goods.setSubTitle(spu.getSubTitle());
        goods.setAll(spu.getTitle() + brand.getName() + StringUtils.join(names, " "));
        goods.setPrice(prices);
        goods.setSkus(MAPPER.writeValueAsString(skuMapList));
        goods.setSpecs(paramMap);

        return goods;


    }
    private String chooseSegment(String value, SpecParam p) {
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : p.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + p.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + p.getUnit() + "以下";
                }else{
                    result = segment + p.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public SearchResult search(SearchRequest request) {
        if(StringUtils.isBlank(request.getKey())){
            return null;
        }
        //自定义查询
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        //查询条件
       // QueryBuilder basicQuery = QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND);
    BoolQueryBuilder basicQuery=buildBoolQueryBuilder(request);
        builder.withQuery(basicQuery);
            //分页
            builder.withPageable(PageRequest.of(request.getPage()-1,request.getSize()));
            //结果过滤
            builder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));
            //分类和品牌过滤
        String categoryName="categories";
        String brandName="brands";
           builder.addAggregation(AggregationBuilders.terms(categoryName).field("cid3"));
        builder.addAggregation(AggregationBuilders.terms(brandName).field("brandId"));

        AggregatedPage<Goods> goodsPage =(AggregatedPage<Goods>) goodsRepository.search(builder.build());
        //获取聚合结果集
       List<Map<String,Object>> categories= getCategoryAggResult(goodsPage.getAggregation(categoryName));
          List<Brand> brands=getBrandAggResult(goodsPage.getAggregation(brandName));
        List<Map<String, Object>> specs=null;
          if(!CollectionUtils.isEmpty(categories)&&categories.size()==1){
                specs=  getParamAggResult((Long)categories.get(0).get("id"),basicQuery);

          }
        return new SearchResult(goodsPage.getTotalElements(),goodsPage.getTotalPages(),goodsPage.getContent(),categories,brands,specs);


    }

    private BoolQueryBuilder buildBoolQueryBuilder(SearchRequest request) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("all",request.getKey()).operator(Operator.AND));
        Map<String, Object> filter = request.getFilter();
        for (Map.Entry<String, Object> entry : filter.entrySet()) {
            String key = entry.getKey();
            if(StringUtils.equals("品牌",key)){
                key="brandId";
            }else   if(StringUtils.equals("分类",key)){
                key="cid3";

            }else {
                key="specs."+key+".keyword";
            }
            boolQueryBuilder.filter(QueryBuilders.termQuery(key,entry.getValue()));
        }

        return boolQueryBuilder;
    }

    //查询条件聚合参数
    private List<Map<String, Object>> getParamAggResult(Long cid, QueryBuilder basicQuery) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        queryBuilder.withQuery(basicQuery);

        List<SpecParam> params = this.specificationClient.queryParams(null, cid, null, true);

        params.forEach(param->{
            queryBuilder.addAggregation(AggregationBuilders.
                    terms(param.getName()).field("specs."+param.getName()+".keyword"));
        });
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{},null));

        AggregatedPage<Goods> goodsPage =(AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());

        List<Map<String, Object>> specs=new ArrayList<>();
        Map<String, Aggregation> aggregationMap = goodsPage.getAggregations().asMap();
        for (Map.Entry<String, Aggregation> entry : aggregationMap.entrySet()) {
            Map<String, Object> map=new HashMap<>();
            map.put("k",entry.getKey());
            List<String > options=new ArrayList<>();
            StringTerms value = (StringTerms)entry.getValue();
            value.getBuckets().forEach(bucket -> {
                      options.add(bucket.getKeyAsString());
            });
            map.put("options",options);
            specs.add(map);
        }


        return specs;
    }


    //品牌结果集
    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        LongTerms terms = (LongTerms) aggregation;
         List<Brand > brands =new ArrayList<>();

        terms.getBuckets().forEach(bucket -> {
            Brand brand = this.brandClient.queryBrandByIds(bucket.getKeyAsNumber().longValue());
            brands.add(brand);


        });
        return brands;
    }

    //分类聚合结果集
    private List<Map<String, Object>> getCategoryAggResult(Aggregation aggregation) {
        LongTerms terms = (LongTerms) aggregation;

          return      terms.getBuckets().stream().map(bucket -> {
              Map<String,Object> map=new HashMap<>();
                    long id = bucket.getKeyAsNumber().longValue();
                    List<String> names = this.categoryClient.queryNameByIds(Arrays.asList(id));
                    map.put("id",id);
                    map.put("name",names.get(0));
                    return map;
                }).collect(Collectors.toList());
    }

    public void save(Long id) throws  Exception{
        Spu spu = this.goodsClient.querySpuById(id);

        Goods goods = this.buildGoods(spu);
        this.goodsRepository.save(goods);
    }
}
