package com.its.modules.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.its.common.persistence.Page;
import com.its.common.service.CrudService;

import com.its.modules.app.entity.OrderGroupPurcList;
import com.its.modules.app.dao.OrderGroupPurcListDao;

/**
 * 订单-团购类子表-团购券清单Service
 * 
 * @author sushipeng
 * 
 * @version 2017-07-13
 */
@Service
@Transactional(readOnly = true)
public class OrderGroupPurcListService extends CrudService<OrderGroupPurcListDao, OrderGroupPurcList> {

	public OrderGroupPurcList get(String id) {
		return super.get(id);
	}

	public List<OrderGroupPurcList> findList(OrderGroupPurcList orderGroupPurcList) {
		return super.findList(orderGroupPurcList);
	}

	public Page<OrderGroupPurcList> findPage(Page<OrderGroupPurcList> page, OrderGroupPurcList orderGroupPurcList) {
		return super.findPage(page, orderGroupPurcList);
	}

	@Transactional(readOnly = false)
	public void save(OrderGroupPurcList orderGroupPurcList) {
		super.save(orderGroupPurcList);
	}

	@Transactional(readOnly = false)
	public void delete(OrderGroupPurcList orderGroupPurcList) {
		super.delete(orderGroupPurcList);
	}

	/**
	 * 获取某团购订单中某种状态的团购券列表
	 * 
	 * @param orderGroupPurcId
	 *            团购订单ID
	 * @param groupPurcState
	 *            团购券状态:0未消费1已消费2退款处理中3已退款
	 * @return List<OrderGroupPurcList>
	 */
	public List<OrderGroupPurcList> getOrderGroupPurcList(String orderGroupPurcId, String groupPurcState) {
		return dao.getOrderGroupPurcList(orderGroupPurcId, groupPurcState);
	}
}