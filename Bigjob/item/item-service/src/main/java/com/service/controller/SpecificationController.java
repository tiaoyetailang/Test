package com.service.controller;

import com.item.pojo.SpecGroup;
import com.item.pojo.SpecParam;
import com.service.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    SpecificationService specificationService;

    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("id")Long id){


        specificationService.deleteGroup(id);


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("group")
    public ResponseEntity<Void> saveGroup(@RequestParam Long cid,@RequestParam String name){


        specificationService.saveGroup(cid,name);


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PutMapping("group")
    public ResponseEntity<Void> edit(@RequestParam Long id,@RequestParam String name){


           specificationService.reviseGroup(id,name);


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsById(@PathVariable("cid") Long cid){
        List<SpecGroup> groups=      specificationService.queryGroupsById(cid);
        if(CollectionUtils.isEmpty(groups)){
            return      ResponseEntity.notFound().build();
        };
        return ResponseEntity.ok(groups);


    }

    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParams(@RequestParam(value = "gid",required = false) Long gid,
                                                       @RequestParam(value = "cid",required = false)Long cid,
                                                       @RequestParam(value = "generic",required = false)Boolean generic,
                                                       @RequestParam(value = "searching",required = false)Boolean searching){
        List<SpecParam> params=   this.specificationService.queryParams(gid,cid,generic,searching);
        if(CollectionUtils.isEmpty(params)){
            return      ResponseEntity.notFound().build();
        };
        return ResponseEntity.ok(params);


    }
     @GetMapping("group/param/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsWithParam(@PathVariable("cid")Long cid){

     List<SpecGroup> groups=    this.specificationService.queryGroupsWithParam(cid);
         if(CollectionUtils.isEmpty(groups)){
             return      ResponseEntity.notFound().build();
         };
         return ResponseEntity.ok(groups);
    }



}
