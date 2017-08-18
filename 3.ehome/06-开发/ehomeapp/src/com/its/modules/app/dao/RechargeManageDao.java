package com.its.modules.app.dao;

import java.util.List;

import com.its.common.persistence.CrudDao;
import com.its.common.persistence.annotation.MyBatisDao;

import com.its.modules.app.entity.RechargeManage;

/**
 * 充值管理DAO接口
 * 
 * @author like
 * 
 * @version 2017-07-18
 */
@MyBatisDao
public interface RechargeManageDao extends CrudDao<RechargeManage> {

	/**
	 * 获取楼盘下的充值计划集合
	 * 
	 * @param villageInfoId
	 *            楼盘ID
	 * @return List<RechargeManage>
	 */
	public List<RechargeManage> getVillageRechargeList(String villageInfoId);
}