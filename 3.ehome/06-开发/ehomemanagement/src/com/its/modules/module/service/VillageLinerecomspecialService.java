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
import com.its.modules.module.dao.VillageLinerecomspecialDao;
import com.its.modules.module.entity.VillageLinerecomspecial;
import com.its.modules.village.entity.VillageLine;

/**
 * 楼盘产品线专题推荐Service
 * 
 * @author zhujiao
 * @version 2017-07-27
 */
@Service
@Transactional(readOnly = true)
public class VillageLinerecomspecialService extends CrudService<VillageLinerecomspecialDao, VillageLinerecomspecial> {

    @Autowired
    private VillageLinerecomspecialDao recomspecialDao;
    @Autowired
    private VillageLinerecomspecialdetailService recomspecialdetailService;

    public VillageLinerecomspecial get(String id) {
        return super.get(id);
    }

    public List<VillageLinerecomspecial> findList(VillageLinerecomspecial villageLinerecomspecial) {
        return super.findList(villageLinerecomspecial);
    }

    public Page<VillageLinerecomspecial> findPage(Page<VillageLinerecomspecial> page, VillageLinerecomspecial villageLinerecomspecial) {
        return super.findPage(page, villageLinerecomspecial);
    }

    @Transactional(readOnly = false)
    public void save(VillageLinerecomspecial villageLinerecomspecial) {
        super.save(villageLinerecomspecial);
    }

    @Transactional(readOnly = false)
    public void delete(VillageLinerecomspecial villageLinerecomspecial) {
        super.delete(villageLinerecomspecial);
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
    public List<VillageLinerecomspecial> getRecomSpecialList(String villageLineId) {
        List<VillageLinerecomspecial> list = new ArrayList<>();
        list = recomspecialDao.getRecomSpecialList(villageLineId);
        try {
            for (VillageLinerecomspecial item : list) {
                item.setRecomSpecialDetailList(recomspecialdetailService.getRecomSpecialDetailList(item.getId()));
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
    public void saveSpecialModule(VillageLine villageLine) {
        try {
            if (villageLine.getRecomSpecialList() != null) {
                for (int i = 0; i < villageLine.getRecomSpecialList().size(); i++) {
                    VillageLinerecomspecial recomSpecial = new VillageLinerecomspecial();
                    recomSpecial = villageLine.getRecomSpecialList().get(i);
                    recomSpecial.setSortNum(i + "");
                    recomSpecial.setRecomPosition("1");// 代表优家推荐（生活推荐）
                    recomSpecial.setVillageLineId(villageLine.getId());
                    if (!"1".equals(recomSpecial.getDelFlag())) {
                        // 保存主表
                        recomSpecial.setDelFlag("0");
                        if (recomSpecial.getId() == null||"".equals(recomSpecial.getId())) {
                            recomSpecial.preInsert();
                            recomspecialDao.insert(recomSpecial);
                            recomspecialdetailService.saveRecomSpecialDetail(recomSpecial);
                        } else {
                            recomSpecial.preUpdate();
                            recomspecialDao.update(recomSpecial);
                            recomspecialdetailService.updateRecomSpecialDetail(recomSpecial);
                        }
                    } else {
                        // 如果删除标记为1 则删除数据专题数据
                        super.delete(recomSpecial);
                        // 连带删除专题详情数据
                        recomspecialdetailService.deleteBySpecialId(recomSpecial.getId());
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
        recomspecialDao.deleteByLine(villageLineId);

    };

}