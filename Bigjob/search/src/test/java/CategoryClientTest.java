import com.cl.SearchApplication;
import com.cl.cliect.CategoryClient;
import com.cl.cliect.GoodsClient;
import com.cl.pojo.Goods;
import com.cl.pojo.PageResult;
import com.cl.repository.GoodsRepository;
import com.cl.service.SearchService;
import com.item.pojo.Spu;
import com.item.pojo.vo.SpuBo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SearchApplication.class)
public class CategoryClientTest {

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    GoodsRepository goodsReponsitory;

    @Autowired
    SearchService searchService;

    @Autowired
    GoodsClient goodsClient;


    @Test
    public void testQueryCategories() {
        List<String> names = this.categoryClient.queryNameByIds(Arrays.asList(1L, 2L, 3L));
        names.forEach(System.out::println);
    }
    @Test
    public  void haha (){
        this.elasticsearchTemplate.createIndex(Goods.class);
        // 配置映射
        this.elasticsearchTemplate.putMapping(Goods.class);
        Integer page = 1;
        Integer rows = 100;

        do {
            // 分批查询spuBo
            PageResult<SpuBo> pageResult = this.goodsClient.querySpuByPage(null, true, page, rows);
            // 遍历spubo集合转化为List<Goods>
            List<Goods> goodsList = pageResult.getItems().stream().map(spuBo -> {
                try {
                    return this.searchService.buildGoods((Spu) spuBo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());

            //this.goodsReponsitory.saveAll(goodsList);

            // 获取当前页的数据条数，如果是最后一页，没有100条
            rows = pageResult.getItems().size();
            // 每次循环页码加1
            page++;
        } while (rows == 100);



    }
}