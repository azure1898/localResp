package com.its.modules.app.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.persistence.Page;
import com.its.common.service.CrudService;
import com.its.modules.app.dao.ModuleManageDao;
import com.its.modules.app.entity.ModuleManage;

/**
 * 模块管理Service
 * 
 * @author like
 * 
 * @version 2017-07-03
 */
@Service
@Transactional(readOnly = true)
public class ModuleManageService extends CrudService<ModuleManageDao, ModuleManage> {

	public ModuleManage get(String id) {
		return super.get(id);
	}

	public List<ModuleManage> findList(ModuleManage moduleManage) {
		return super.findList(moduleManage);
	}

	public Page<ModuleManage> findPage(Page<ModuleManage> page, ModuleManage moduleManage) {
		return super.findPage(page, moduleManage);
	}

	@Transactional(readOnly = false)
	public void save(ModuleManage moduleManage) {
		super.save(moduleManage);
	}

	@Transactional(readOnly = false)
	public void delete(ModuleManage moduleManage) {
		super.delete(moduleManage);
	}

	/**
	 * 获取某楼盘下的模块列表（商户分类）
	 * 
	 * @param villageInfoId
	 *            楼盘ID
	 * @return List<ModuleManage>
	 */
	public List<ModuleManage> getModuleListByVillageInfoId(String villageInfoId) {
		return dao.getModuleListByVillageInfoId(villageInfoId);
	}

	/**
	 * 获取某商户某产品模式下的商户分类的模块ID
	 * 
	 * @param prodType
	 *            产品模式：0商品购买 1服务预约 2课程购买 3场地预约
	 * @param businessInfoId
	 *            商户ID
	 * @return 模块ID
	 */
	public String getModuleId(String prodType, String businessInfoId) {
		return dao.getModuleId(prodType, businessInfoId);
	}

	/**
	 * 获取某楼盘下某种类型的模块推荐信息
	 * 
	 * @param type
	 *            主导航类型：0首页 1社区 2生活
	 * @param villageInfoId
	 *            楼盘ID
	 * @return List<ModuleManage>
	 */
	public List<ModuleManage> getModuleList(String type, String villageInfoId) {
		return dao.getModuleList(type, villageInfoId);
	}

	/**
	 * 获取某楼盘下某推荐位置的商家列表
	 * 
	 * @param recommendPosition
	 *            推荐位置：00首页推荐商家 10社区模块推荐2 11社区更多服务推荐 20生活商家推荐1
	 * @param villageInfoId
	 *            楼盘ID
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getRecommendBusinessList(String recommendPosition, String villageInfoId) {
		return dao.getRecommendBusinessList(recommendPosition, villageInfoId);
	}
	
	/**
	 * 根据手机端模块编码获取模块信息
	 * @param phoneModuleCode
	 * @return
	 */
	public ModuleManage getModuleByPhoneCode(String phoneModuleCode){
		return dao.getModuleByPhoneCode(phoneModuleCode);
	}
}