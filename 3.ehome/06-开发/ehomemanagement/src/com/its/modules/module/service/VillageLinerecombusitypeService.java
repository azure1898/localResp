/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.module.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.persistence.Page;
import com.its.common.service.CrudService;
import com.its.modules.module.entity.VillageLinerecombusitype;
import com.its.modules.module.entity.VillageLinerecomspecial;
import com.its.modules.village.entity.VillageLine;
import com.its.modules.module.dao.VillageLinerecombusitypeDao;

/**
 * 楼盘产品线推荐商家模式Service
 * 
 * @author zhujiao
 * @version 2017-07-27
 */
@Service
@Transactional(readOnly = true)
public class VillageLinerecombusitypeService extends CrudService<VillageLinerecombusitypeDao, VillageLinerecombusitype> {

    @Autowired
    private VillageLinerecombusitypeDao recomBusiTypeDao;
    @Autowired
    private VillageLinerecombusitypedetailService recomBusiTypeDetailService;
    
    
    
    public VillageLinerecombusitype get(String id) {
        return super.get(id);
    }

    public List<VillageLinerecombusitype> findList(VillageLinerecombusitype villageLinerecombusitype) {
        return super.findList(villageLinerecombusitype);
    }

    public Page<VillageLinerecombusitype> findPage(Page<VillageLinerecombusitype> page, VillageLinerecombusitype villageLinerecombusitype) {
        return super.findPage(page, villageLinerecombusitype);
    }

    @Transactional(readOnly = false)
    public void save(VillageLinerecombusitype villageLinerecombusitype) {
        super.save(villageLinerecombusitype);
    }

    @Transactional(readOnly = false)
    public void delete(VillageLinerecombusitype villageLinerecombusitype) {
        super.delete(villageLinerecombusitype);
    }

    /**
     * 通过楼盘线获取专题推荐列表
     * 
     * @param villageLineId
     * @return
     * @return List<VillageLinerecommodule>
     * @author zhujiao
     * @date 2017年7月28日 上午10:03:50
     */
    public List<VillageLinerecombusitype> getRecomBusTypeList(String villageLineId) {
        List<VillageLinerecombusitype> list = new ArrayList<>();
        list = recomBusiTypeDao.getRecomBusTypeList(villageLineId);
        try {
            for (VillageLinerecombusitype item : list) {
                item.setRecomBusTypeDetailList(recomBusiTypeDetailService.getRecomBusTypeDetailList(item.getId()));
            }

        } catch (Exception e) {
            System.out.println("获取专题详情失败：" + e.getMessage());
        }
        return list;
    };

    /**
     * 保存推荐专题信息
     * 
     * @param villageLine
     * @return void
     * @author zhujiao
     * @date 2017年7月28日 上午9:48:57
     */
    @Transactional(readOnly = false)
    public void saveBusTypeModule(VillageLine villageLine) {
        try {
            if (villageLine.getRecomBusTypeList()!= null) {
                for (int i = 0; i < villageLine.getRecomBusTypeList().size(); i++) {
                    VillageLinerecombusitype recomBusType = new VillageLinerecombusitype();
                    recomBusType = villageLine.getRecomBusTypeList().get(i);
                    recomBusType.setSortNum(i + "");
                    recomBusType.setRecomPosition("1");// 代表优家推荐（生活推荐）
                    recomBusType.setVillageLineId(villageLine.getId());
                    if (!"1".equals(recomBusType.getDelFlag())) {
                        // 保存主表
                        recomBusType.setDelFlag("0");
                        if (recomBusType.getId() == null || "".equals(recomBusType.getId())) {
                            recomBusType.preInsert();
                            recomBusiTypeDao.insert(recomBusType);
                            recomBusiTypeDetailService.saveRecomBusTypeDetail(recomBusType);
                        } else {
                            recomBusType.preUpdate();
                            recomBusiTypeDao.update(recomBusType);
                            recomBusiTypeDetailService.updateRecomBusTypeDetail(recomBusType);
                        }
                    } else {
                        // 如果删除标记为1 则删除数据商家数据
                        super.delete(recomBusType);
                        // 连带删除专题详情数据
                        recomBusiTypeDetailService.deleteByBusId(recomBusType.getId());
                    }
                }
            }
        } catch (

        Exception e) {
            System.out.println("专题推荐保存失败：" + e.getMessage());
        }
    }

    /**
     * 通过楼盘线删除专题推荐模块数据
     * 
     * @param villageLineId
     * @return void
     * @author zhujiao
     * @date 2017年7月28日 上午9:51:15
     */
    @Transactional(readOnly = false)
    public void deleteByLine(String villageLineId) {
        recomBusiTypeDao.deleteByLine(villageLineId);

    };

}