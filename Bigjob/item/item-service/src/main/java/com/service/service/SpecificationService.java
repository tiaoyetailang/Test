package com.service.service;

import com.item.pojo.SpecGroup;
import com.item.pojo.SpecParam;
import com.service.mapper.SpecGroupMapper;
import com.service.mapper.SpecParamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationService {
    @Autowired
    SpecGroupMapper specGroupMapper;

    @Autowired
    SpecParamMapper specParamMapper;


    public List<SpecGroup> queryGroupsById(Long cid) {
        SpecGroup record = new SpecGroup();
        record.setCid(cid);
        return specGroupMapper.select(record);

    }

    public List<SpecParam> queryParams(Long gid,Long cid,Boolean generic,Boolean searching) {

        SpecParam record=new SpecParam();
        record.setGroupId(gid);
        record.setCid(cid);
        record.setGeneric(generic);
        record.setSearching(searching);

       return  this.specParamMapper.select(record);
        }

    public void reviseGroup(Long id, String name) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setId(id);
        specGroup.setName(name);
        int i = specGroupMapper.updateByPrimaryKeySelective(specGroup);
    }

    public void saveGroup(Long cid, String name) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        specGroup.setName(name);
        specGroupMapper.insertSelective(specGroup);
    }

    public void deleteGroup(Long id) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setId(id);
        specGroupMapper.delete(specGroup);
    }

    public List<SpecGroup> queryGroupsWithParam(Long cid) {
        List<SpecGroup> groups = this.queryGroupsById(cid);
        groups.forEach(group->{
            List<SpecParam> params = this.queryParams(group.getId(), null, null, null);
            group.setParams(params);
        });
           return groups;
    }
}
