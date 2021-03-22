package com.cl.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {
            static  final List<String> content_types= Arrays.asList("image/gif","image/jpeg");

            static  final Logger logger= LoggerFactory.getLogger(UploadService.class);

    public String uploadImage(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
         if(!content_types.contains(contentType)){
             logger.info("文件类型不合法："+originalFilename);
             return null;
         }
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
         if(bufferedImage==null){

             logger.info("文件内容不合法");
             return null;
         }

         file.transferTo(new File("D:\\毕业设计\\image"+originalFilename));

              return originalFilename;
    }
}
