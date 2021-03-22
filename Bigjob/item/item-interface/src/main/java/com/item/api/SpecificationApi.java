package com.item.api;

import com.item.pojo.SpecGroup;
import com.item.pojo.SpecParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("spec")
public interface SpecificationApi {





    @GetMapping("params")
    public List<SpecParam> queryParams(@RequestParam(value = "gid",required = false) Long gid,
                                                       @RequestParam(value = "cid",required = false)Long cid,
                                                       @RequestParam(value = "generic",required = false)Boolean generic,
                                                       @RequestParam(value = "searching",required = false)Boolean searching);



    @GetMapping("group/param/{cid}")
    public List<SpecGroup> queryGroupsWithParam(@PathVariable("cid")Long cid);



}
