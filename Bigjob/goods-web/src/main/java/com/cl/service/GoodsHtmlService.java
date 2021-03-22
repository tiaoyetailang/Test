package com.cl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Service
public class GoodsHtmlService {

    @Autowired
    TemplateEngine templateEngine;



    @Autowired
    GoodsService goodsService;
    public  void createHtml(Long spuId){
        Context context = new Context();
        context.setVariables(this.goodsService.loadData(spuId));
        PrintWriter printWriter=null;
        try {
            File file = new File("D:\\nginx-1.14.0\\html\\item"+spuId+".html");
            printWriter = new PrintWriter(file);

            this.templateEngine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(printWriter!=null){
                printWriter.close();
            }

        }

    }
}
