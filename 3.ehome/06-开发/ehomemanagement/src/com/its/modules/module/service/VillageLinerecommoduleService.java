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
import com.its.common.utils.StringUtils;
import com.its.modules.module.dao.VillageLinerecommoduleDao;
import com.its.modules.module.entity.VillageLinerecommodule;
import com.its.modules.village.entity.VillageLine;

/**
 * 楼盘产品线推荐模块或商家Service
 * 
 * @author zhujiao
 * @version 2017-07-27
 */
@Service
@Transactional(readOnly = true)
public class VillageLinerecommoduleService extends CrudService<VillageLinerecommoduleDao, VillageLinerecommodule> {

    @Autowired
    private VillageLinerecommoduleDao recomModuleDao;

    public VillageLinerecommodule get(String id) {
        return super.get(id);
    }

    public List<VillageLinerecommodule> findList(VillageLinerecommodule villageLinerecommodule) {
        return super.findList(villageLinerecommodule);
    }

    public Page<VillageLinerecommodule> findPage(Page<VillageLinerecommodule> page, VillageLinerecommodule villageLinerecommodule) {
        return super.findPage(page, villageLinerecommodule);
    }

    @Transactional(readOnly = false)
    public void save(VillageLinerecommodule villageLinerecommodule) {
        super.save(villageLinerecommodule);
    }

    @Transactional(readOnly = false)
    public void delete(VillageLinerecommodule villageLinerecommodule) {
        super.delete(villageLinerecommodule);
    }

    /**
     * 通过楼盘线获取推荐模块的List
     * 
     * @param villageLineId
     * @return
     * @return List<VillageLinerecommodule>
     * @author zhujiao
     * @date 2017年7月28日 上午10:03:50
     */
    public List<VillageLinerecommodule> getRecomModuleList(String villageLineId) {
        return recomModuleDao.getRecomModuleList(villageLineId);
    };

    /**
     * 保存推荐模块信息
     * 
     * @param villageLine
     * @return void
     * @author zhujiao
     * @date 2017年7月28日 上午9:48:57
     */
    @Transactional(readOnly = false)
    public void saveRecomModule(VillageLine villageLine) {
        if (villageLine.getRecomModuleList() != null) {
            for (int i = 0; i < villageLine.getRecomModuleList().size(); i++) {
                VillageLinerecommodule recomModule = new VillageLinerecommodule();
                recomModule = villageLine.getRecomModuleList().get(i);
                recomModule.setSortNum(i + "");
                recomModule.setRecomPosition("20");// 代表优家推荐（生活推荐）
                recomModule.setVillageLineId(villageLine.getId());
                if (!"1".equals(recomModule.getDelFlag())) {
                    recomModule.setDelFlag("0");
                    if (recomModule.getId() == null||"".equals(recomModule.getId())) {
                        recomModule.preInsert();
                        recomModuleDao.insert(recomModule);
                    } else {
                        recomModule.preUpdate();
                        recomModuleDao.update(recomModule);
                    }
                } else {
                    super.delete(recomModule);
                }
            }
        }

    }

    /**
     * 通过楼盘线删除推荐模块数据
     * 
     * @param villageLineId
     * @return void
     * @author zhujiao
     * @date 2017年7月28日 上午9:51:15
     */
    @Transactional(readOnly = false)
    public void deleteByLine(String villageLineId) {
        recomModuleDao.deleteByLine(villageLineId);
    };

}