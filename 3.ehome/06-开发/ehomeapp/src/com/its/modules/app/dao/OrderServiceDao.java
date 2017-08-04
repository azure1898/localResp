package com.its.modules.app.dao;

import org.apache.ibatis.annotations.Param;

import com.its.common.persistence.CrudDao;
import com.its.common.persistence.annotation.MyBatisDao;
import com.its.modules.app.bean.OrderServiceBean;
import com.its.modules.app.entity.OrderService;

/**
 * 订单-服务类DAO接口
 * 
 * @author sushipeng
 * 
 * @version 2017-07-07
 */
@MyBatisDao
public interface OrderServiceDao extends CrudDao<OrderService> {

	/**
	 * 获取某预约服务已购数量
	 * 
	 * @param serviceInfoId
	 *            服务ID
	 * @return 已购数量
	 */
	public Integer getCountByServiceInfoId(String serviceInfoId);

	/**
	 * * 获取某预约服务用户已购数量
	 * 
	 * @param serviceInfoId
	 *            服务ID
	 * @param accountId
	 *            用户ID
	 * @return 已购数量
	 */
	public Integer getCountByServiceInfoIdAndAccountId(@Param("serviceInfoId") String serviceInfoId, @Param("accountId") String accountId);

	/**
	 * 根据订单号获取预约服务订单
	 * 
	 * @param orderNo
	 *            订单号
	 * @return OrderService
	 */
	public OrderService getByOrderNo(String orderNo);

	/**
	 * 根据订单ID和用户ID获取订单信息
	 * 
	 * @param orderId
	 *            订单ID
	 * @param accountId
	 *            用户ID
	 * @return OrderServiceBean
	 */
	public OrderServiceBean getOrderServiceByOrderIdAndAccountId(@Param("orderId") String orderId, @Param("accountId") String accountId);

	/**
	 * 判断某用户是否可以取消某订单
	 * 
	 * @param orderId
	 *            订单ID
	 * @param accountId
	 *            用户ID
	 * @return OrderService
	 */
	public OrderService judgeOrderServiceByOrderIdAndAccountId(@Param("orderId") String orderId, @Param("accountId") String accountId);
}