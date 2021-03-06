/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.module.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.persistence.Page;
import com.its.common.service.CrudService;
import com.its.modules.module.dao.VillageLinerecombusitypedetailDao;
import com.its.modules.module.entity.VillageLinerecombusitype;
import com.its.modules.module.entity.VillageLinerecombusitypedetail;
import com.its.modules.module.entity.VillageLinerecomspecial;
import com.its.modules.module.entity.VillageLinerecomspecialdetail;

/**
 * 楼盘产品线推荐商家模式Service
 * 
 * @author zhujiao
 * @version 2017-07-27
 */
@Service
@Transactional(readOnly = true)
public class VillageLinerecombusitypedetailService extends CrudService<VillageLinerecombusitypedetailDao, VillageLinerecombusitypedetail> {

    @Autowired
    private VillageLinerecombusitypedetailDao recombusitypedetailDao;

    public VillageLinerecombusitypedetail get(String id) {
        return super.get(id);
    }

    public List<VillageLinerecombusitypedetail> findList(VillageLinerecombusitypedetail villageLinerecombusitypedetail) {
        return super.findList(villageLinerecombusitypedetail);
    }

    public Page<VillageLinerecombusitypedetail> findPage(Page<VillageLinerecombusitypedetail> page, VillageLinerecombusitypedetail villageLinerecombusitypedetail) {
        return super.findPage(page, villageLinerecombusitypedetail);
    }

    @Transactional(readOnly = false)
    public void save(VillageLinerecombusitypedetail villageLinerecombusitypedetail) {
        super.save(villageLinerecombusitypedetail);
    }

    @Transactional(readOnly = false)
    public void delete(VillageLinerecombusitypedetail villageLinerecombusitypedetail) {
        super.delete(villageLinerecombusitypedetail);
    }

    /**
     * 通过专题推荐的ID获取专题推荐明细List
     * 
     * @param villageLineId
     * @return
     * @return List<VillageLinerecommodule>
     * @author zhujiao
     * @date 2017年7月28日 上午10:03:50
     */
    public List<VillageLinerecombusitypedetail> getRecomBusTypeDetailList(String businessInfoId) {
        return recombusitypedetailDao.getRecomBusTypeDetailList(businessInfoId);
    };

    /**
     * 保存推荐专题详情信息
     * 
     * @param recomBusType
     * @return void
     * @author zhujiao
     * @date 2017年8月4日 上午9:51:57
     */
    @Transactional(readOnly = false)
    public void saveRecomBusTypeDetail(VillageLinerecombusitype recomBusType) {
        if (recomBusType.getRecomBusTypeDetailList() != null) {
            for (int i = 0; i < recomBusType.getRecomBusTypeDetailList().size(); i++) {
                VillageLinerecombusitypedetail recomBusTypeDetail = new VillageLinerecombusitypedetail();
                recomBusTypeDetail = recomBusType.getRecomBusTypeDetailList().get(i);
                //String busTypeId=recomBusTypeDetail.getProdType();
                if (!"1".equals(recomBusTypeDetail.getDelFlag())) {
                    recomBusTypeDetail.preInsert();
                    recomBusTypeDetail.setSortNum(i + "");
                    recomBusTypeDetail.setDefaultFlag("0");
                    recomBusTypeDetail.setVillageLineRecomBusiTypeId(recomBusType.getId());
                    recombusitypedetailDao.insert(recomBusTypeDetail);
                }else{
                    super.delete(recomBusTypeDetail); 
                }
            }
        }
    }

    /**
     * 修改商家推荐分类详情信息
     * 
     * @param recomBusType
     * @return void
     * @author zhujiao
     * @date 2017年8月4日 上午9:51:39
     */
    @Transactional(readOnly = false)
    public void updateRecomBusTypeDetail(VillageLinerecombusitype recomBusType) {
        if (recomBusType.getRecomBusTypeDetailList() != null) {
            for (int i = 0; i < recomBusType.getRecomBusTypeDetailList().size(); i++) {
                VillageLinerecombusitypedetail recomBusTypeDetail = new VillageLinerecombusitypedetail();
                recomBusTypeDetail = recomBusType.getRecomBusTypeDetailList().get(i);
                String busTypeId=recomBusTypeDetail.getProdType();
                if (recomBusTypeDetail.getId()!=null&&!"".equals(recomBusTypeDetail.getId())) {
                    recomBusTypeDetail.preUpdate();
                    recomBusTypeDetail.setSortNum(i + "");
                    recomBusTypeDetail.setVillageLineRecomBusiTypeId(recomBusType.getId());
                    recombusitypedetailDao.update(recomBusTypeDetail);
                }else if(busTypeId!=null&&!"".equals(busTypeId)){
                    recomBusTypeDetail.preInsert();
                    recomBusTypeDetail.setSortNum(i + "");
                    recomBusTypeDetail.setDefaultFlag("0");
                    recomBusTypeDetail.setVillageLineRecomBusiTypeId(recomBusType.getId());
                    recombusitypedetailDao.insert(recomBusTypeDetail);
                }
            }
        }
    }

    /**
     * 通过商家推荐的ID删除商家推荐分类明细
     * 
     * @param businessInfoId
     * @return void
     * @author zhujiao
     * @date 2017年8月4日 上午9:50:19
     */
    @Transactional(readOnly = false)
    public void deleteByBusId(String businessInfoId) {
        recombusitypedetailDao.deleteByBusId(businessInfoId);
    };

}