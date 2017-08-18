package com.its.modules.app.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.its.common.config.Global;
import com.its.common.web.BaseController;
import com.its.modules.app.bean.MyOrderViewBean;
import com.its.modules.app.bean.OrderFieldBean;
import com.its.modules.app.bean.OrderGoodsBean;
import com.its.modules.app.bean.OrderGroupPurcBean;
import com.its.modules.app.bean.OrderLessonBean;
import com.its.modules.app.bean.OrderServiceBean;
import com.its.modules.app.common.AppUtils;
import com.its.modules.app.common.CommonGlobal;
import com.its.modules.app.common.OrderGlobal;
import com.its.modules.app.common.ValidateUtil;
import com.its.modules.app.entity.Account;
import com.its.modules.app.entity.BusinessInfo;
import com.its.modules.app.entity.ModuleManage;
import com.its.modules.app.entity.OrderFieldList;
import com.its.modules.app.entity.OrderGoodsList;
import com.its.modules.app.entity.OrderGroupPurcList;
import com.its.modules.app.entity.OrderLessonList;
import com.its.modules.app.entity.OrderServiceList;
import com.its.modules.app.entity.OrderTrack;
import com.its.modules.app.service.AccountService;
import com.its.modules.app.service.BusinessInfoService;
import com.its.modules.app.service.ModuleManageService;
import com.its.modules.app.service.OrderFieldService;
import com.its.modules.app.service.OrderGoodsService;
import com.its.modules.app.service.OrderGroupPurcService;
import com.its.modules.app.service.OrderLessonService;
import com.its.modules.app.service.OrderServiceService;
import com.its.modules.app.service.OrderTrackService;

/**
 * 我的订单Controller
 * 
 * @author sushipeng
 * 
 * @version 2017-07-18
 */
@Controller
@RequestMapping(value = "${appPath}/my")
public class MyOrderController extends BaseController {

	@Autowired
	private BusinessInfoService businessInfoService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private OrderGoodsService orderGoodsService;

	@Autowired
	private OrderServiceService orderServiceService;

	@Autowired
	private OrderLessonService orderLessonService;

	@Autowired
	private OrderFieldService orderFieldService;

	@Autowired
	private OrderGroupPurcService orderGroupPurcService;

	@Autowired
	private OrderTrackService orderTrackService;

	@Autowired
	private ModuleManageService moduleManageService;

	/**
	 * 我的订单
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param buildingID
	 *            楼盘ID（不可空）
	 * @param moduleID
	 *            模块ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "getOrderList")
	@ResponseBody
	public Map<String, Object> getOrderList(String userID, String buildingID, String moduleID, HttpServletRequest request) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, buildingID, moduleID)) {
			return toJson;
		}
		Account account = accountService.get(userID);
		if (account == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "用户不存在");
			return toJson;
		}
		List<MyOrderViewBean> myOrderViewBeans = orderTrackService.getMyOrderViewList(buildingID, userID, moduleID);
		if (myOrderViewBeans == null || myOrderViewBeans.size() == 0) {
			toJson.put("code", Global.CODE_SUCCESS);
			toJson.put("message", "暂无数据");
			return toJson;
		}

		/* Data数据开始 */
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (MyOrderViewBean myOrderViewBean : myOrderViewBeans) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("orderID", myOrderViewBean.getOrderId());
			data.put("name", myOrderViewBean.getShowName());
			data.put("businessImage", ValidateUtil.getImageUrl(myOrderViewBean.getBusinessPic(), ValidateUtil.ZERO, request));
			data.put("orderType", myOrderViewBean.getOrderType());
			data.put("timeLabel", myOrderViewBean.getTimeLabel());
			data.put("time", orderTrackService.getOrderTime(myOrderViewBean));
			data.put("orderMoney", ValidateUtil.validateDouble(myOrderViewBean.getPayMoney()));
			OrderTrack orderTrack = orderTrackService.getRecentOrderStatus(myOrderViewBean.getOrderId());
			data.put("orderStatus", orderTrack == null ? "" : orderTrack.getStateMsgPhone());

			datas.add(data);
		}
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", datas);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 订单筛选
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param buildingID
	 *            楼盘ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "getOrderFilter")
	@ResponseBody
	public Map<String, Object> getOrderFilter(String userID, String buildingID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, buildingID)) {
			return toJson;
		}
		Account account = accountService.get(userID);
		if (account == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "用户不存在");
			return toJson;
		}

		/* Data数据开始 */
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		// 全部订单
		Map<String, Object> first = new HashMap<String, Object>();
		first.put("moduleID", CommonGlobal.ALL_ORDER_MODULEID);
		first.put("moduleName", CommonGlobal.ALL_ORDER_DESC);
		datas.add(first);
		// 精品团购
		Map<String, Object> last = new HashMap<String, Object>();
		last.put("moduleID", CommonGlobal.GROUP_PURCHASE_MODULEID);
		last.put("moduleName", CommonGlobal.GROUP_PURCHASE_DESC);
		datas.add(last);

		List<ModuleManage> moduleManages = moduleManageService.getModuleListByVillageInfoId(buildingID);
		if (moduleManages != null && moduleManages.size() != 0) {
			for (ModuleManage moduleManage : moduleManages) {
				Map<String, Object> middle = new HashMap<String, Object>();
				middle.put("moduleID", moduleManage.getId());
				middle.put("moduleName", moduleManage.getModuleName());

				datas.add(middle);
			}
		}
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", datas);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 商品购买订单详情
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param orderID
	 *            订单ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "getBusinessOrderDetail")
	@ResponseBody
	public Map<String, Object> getBusinessOrderDetail(String userID, String orderID, HttpServletRequest request) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, orderID)) {
			return toJson;
		}
		OrderGoodsBean orderGoodsBean = orderGoodsService.getOrderGoodsByOrderIdAndAccountId(orderID, userID);
		if (orderGoodsBean == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "订单不存在");
			return toJson;
		}

		/* Data数据开始 */
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("orderID", orderGoodsBean.getId());
		this.getOrderStatus(data, orderID);
		data.put("businessID", orderGoodsBean.getBusinessInfoId());
		data.put("businessPhone", orderGoodsBean.getBusinessInfo().getPhoneNum());
		data.put("businessName", orderGoodsBean.getBusinessInfo().getBusinessName());

		/* 商品列表开始 */
		List<Map<String, Object>> commodities = new ArrayList<Map<String, Object>>();
		List<OrderGoodsList> orderGoodsLists = orderGoodsBean.getOrderGoodsLists();
		for (OrderGoodsList orderGoodsList : orderGoodsLists) {
			Map<String, Object> commodity = new HashMap<String, Object>();
			commodity.put("commodityID", orderGoodsList.getGoodsInfoId());
			commodity.put("commodityName", orderGoodsList.getName());
			commodity.put("commodityImage", ValidateUtil.getImageUrl(orderGoodsList.getImgs(), ValidateUtil.ZERO, request));
			commodity.put("commodityMoney", ValidateUtil.validateDouble(orderGoodsList.getPaySumMoney()));
			commodity.put("commodityNumber", ValidateUtil.validateInteger(orderGoodsList.getGoodsSum()));

			commodities.add(commodity);
		}
		data.put("commodities", commodities);
		/* 商品列表结束 */

		data.put("deliveryFee", ValidateUtil.validateDouble(orderGoodsBean.getAddressMoney()) - ValidateUtil.validateDouble(orderGoodsBean.getAddressBenefit()));
		data.put("activityReduce", ValidateUtil.validateDouble(orderGoodsBean.getBenefitMoney()));
		data.put("couponReduce", ValidateUtil.validateDouble(orderGoodsBean.getCouponMoney()));
		data.put("orderMoney", ValidateUtil.validateDouble(orderGoodsBean.getPayMoney()));
		data.put("contactPerson", orderGoodsBean.getAccountName());
		data.put("contactPhone", orderGoodsBean.getAccountPhoneNumber());
		data.put("contactAddress", orderGoodsBean.getAddress());
		String deliveryDate = null;
		// 是否立即配送
		if (CommonGlobal.YES.equals(orderGoodsBean.getIsStart())) {
			deliveryDate = orderGoodsBean.getEndTime();
		} else {
			deliveryDate = orderGoodsBean.getStartTime() + "~" + orderGoodsBean.getEndTime();
		}
		data.put("deliveryDate", deliveryDate);
		data.put("leaveMessage", orderGoodsBean.getAccountMsg());
		data.put("orderCode", orderGoodsBean.getOrderNo());
		data.put("orderTime", DateFormatUtils.format(orderGoodsBean.getCreateDate(), "yyyy-MM-dd HH:mm"));
		data.put("paidMethod", orderGoodsBean.getPayType());
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", data);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 预约服务类订单详情
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param orderID
	 *            订单ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "getServiceOrderDetail")
	@ResponseBody
	public Map<String, Object> getServiceOrderDetail(String userID, String orderID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, orderID)) {
			return toJson;
		}
		OrderServiceBean orderServiceBean = orderServiceService.getOrderServiceByOrderIdAndAccountId(orderID, userID);
		if (orderServiceBean == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "订单不存在");
			return toJson;
		}

		/* Data数据开始 */
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("orderID", orderServiceBean.getId());
		this.getOrderStatus(data, orderID);
		BusinessInfo businessInfo = orderServiceBean.getBusinessInfo();
		data.put("businessID", orderServiceBean.getBusinessInfoId());
		data.put("businessPhone", businessInfo.getPhoneNum());
		data.put("businessName", businessInfo.getBusinessName());
		data.put("serviceWay", businessInfo.getServiceModel());
		data.put("serviceCharge", ValidateUtil.validateDouble(businessInfo.getServiceCharge()));
		OrderServiceList orderServiceList = orderServiceBean.getOrderServiceList();
		data.put("serviceID", orderServiceList.getServiceInfoId());
		data.put("serviceName", orderServiceList.getName());
		data.put("unit", businessInfoService.getUnit(orderServiceList.getBaseUnit()));
		data.put("serviceMoney", ValidateUtil.validateDouble(orderServiceList.getPaySumMoney()));
		data.put("serviceNumber", ValidateUtil.validateInteger(orderServiceList.getPayCount()));
		data.put("couponReduce", ValidateUtil.validateDouble(orderServiceBean.getCouponMoney()));
		data.put("orderMoney", ValidateUtil.validateDouble(orderServiceBean.getPayMoney()));
		data.put("contactPerson", orderServiceBean.getAccountName());
		data.put("contactPhone", orderServiceBean.getAccountPhoneNumber());
		data.put("contactAddress", orderServiceBean.getServiceAddress());
		// 是否立即上门
		String serviceDate = null;
		if (CommonGlobal.YES.equals(orderServiceBean.getIsStart())) {
			serviceDate = orderServiceBean.getEndTime();
		} else {
			serviceDate = orderServiceBean.getStartTime() + "~" + orderServiceBean.getEndTime();
		}
		data.put("serviceDate", serviceDate);
		data.put("leaveMessage", orderServiceBean.getAccountMsg());
		data.put("orderCode", orderServiceBean.getOrderNo());
		data.put("orderTime", DateFormatUtils.format(orderServiceBean.getCreateDate(), "yyyy-MM-dd HH:mm"));
		data.put("paidMethod", orderServiceBean.getPayType());
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", data);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 课程购买类订单详情
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param orderID
	 *            订单ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "getCourseOrderDetail")
	@ResponseBody
	public Map<String, Object> getCourseOrderDetail(String userID, String orderID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, orderID)) {
			return toJson;
		}
		OrderLessonBean orderLessonBean = orderLessonService.getOrderLessonByOrderIdAndAccountId(orderID, userID);
		if (orderLessonBean == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "订单不存在");
			return toJson;
		}

		/* Data数据开始 */
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("orderID", orderLessonBean.getId());
		this.getOrderStatus(data, orderID);
		data.put("businessID", orderLessonBean.getBusinessInfoId());
		data.put("businessPhone", orderLessonBean.getBusinessInfo().getPhoneNum());
		data.put("businessName", orderLessonBean.getBusinessInfo().getBusinessName());
		OrderLessonList orderLessonList = orderLessonBean.getOrderLessonList();
		data.put("courseID", orderLessonList.getLessonInfoId());
		data.put("courseName", orderLessonList.getName());
		data.put("coursePrice", ValidateUtil.validateDouble(orderLessonList.getPaySumMoney()));
		data.put("classNumber", ValidateUtil.validateInteger(orderLessonList.getLessonCount()));
		data.put("classTime", DateFormatUtils.format(orderLessonList.getStartTime(), "yyyy-MM-dd") + "至" + DateFormatUtils.format(orderLessonList.getEndTime(), "MM-dd"));
		data.put("classLocation", orderLessonList.getAddress());
		data.put("couponReduce", ValidateUtil.validateDouble(orderLessonBean.getCouponMoney()));
		data.put("orderMoney", ValidateUtil.validateDouble(orderLessonBean.getPayMoney()));
		data.put("contactPerson", orderLessonBean.getAccountName());
		data.put("contactPhone", orderLessonBean.getAccountPhoneNumber());
		data.put("leaveMessage", orderLessonBean.getAccountMsg());
		data.put("orderCode", orderLessonBean.getOrderNo());
		data.put("orderTime", DateFormatUtils.format(orderLessonBean.getCreateDate(), "yyyy-MM-dd HH:mm"));
		data.put("paidMethod", orderLessonBean.getPayType());
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", data);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 场地预约类订单详情
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param orderID
	 *            订单ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "getSiteOrderDetail")
	@ResponseBody
	public Map<String, Object> getSiteOrderDetail(String userID, String orderID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, orderID)) {
			return toJson;
		}
		OrderFieldBean orderFieldBean = orderFieldService.getOrderFieldByOrderIdAndAccountId(orderID, userID);
		if (orderFieldBean == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "订单不存在");
			return toJson;
		}

		/* Data数据开始 */
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("orderID", orderFieldBean.getId());
		this.getOrderStatus(data, orderID);
		data.put("businessID", orderFieldBean.getBusinessInfoId());
		data.put("businessPhone", orderFieldBean.getBusinessInfo().getPhoneNum());
		data.put("businessName", orderFieldBean.getBusinessInfo().getBusinessName());
		List<OrderFieldList> orderFieldLists = orderFieldBean.getOrderFieldLists();
		OrderFieldList orderFieldList = orderFieldLists.get(0);
		data.put("siteID", orderFieldList.getFieldInfoId());
		data.put("siteName", orderFieldList.getName());
		String appointmentTime = DateFormatUtils.format(orderFieldList.getAppointmentTime(), "yyyy-MM-dd");
		data.put("reservationDate", appointmentTime + " " + AppUtils.formatDateWeek(appointmentTime));

		/* 场地预约时段开始 */
		List<Map<String, Object>> details = new ArrayList<Map<String, Object>>();
		for (OrderFieldList field : orderFieldLists) {
			Map<String, Object> detail = new HashMap<String, Object>();
			detail.put("timePeriod", DateFormatUtils.format(field.getStartTime(), "HH:mm") + "~" + DateFormatUtils.format(field.getEndTime(), "HH:mm"));
			detail.put("price", ValidateUtil.validateDouble(field.getSumMoney()));

			details.add(detail);
		}
		data.put("detail", details);
		/* 场地预约时段结束 */

		data.put("couponReduce", ValidateUtil.validateDouble(orderFieldBean.getCouponMoney()));
		data.put("orderMoney", ValidateUtil.validateDouble(orderFieldBean.getPayMoney()));
		data.put("contactPerson", orderFieldBean.getAccountName());
		data.put("contactPhone", orderFieldBean.getAccountPhoneNumber());
		data.put("leaveMessage", orderFieldBean.getAccountMsg());
		data.put("orderCode", orderFieldBean.getOrderNo());
		data.put("orderTime", DateFormatUtils.format(orderFieldBean.getCreateDate(), "yyyy-MM-dd HH:mm"));
		data.put("paidMethod", orderFieldBean.getPayType());
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", data);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 团购类订单详情
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param orderID
	 *            订单ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "getGroupBuyDetail")
	@ResponseBody
	public Map<String, Object> getGroupBuyDetail(String userID, String orderID, HttpServletRequest request) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, orderID)) {
			return toJson;
		}
		OrderGroupPurcBean orderGroupPurcBean = orderGroupPurcService.getOrderGroupPurcByOrderIdAndAccountId(orderID, userID);
		if (orderGroupPurcBean == null) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "订单不存在");
			return toJson;
		}

		/* Data数据开始 */
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("orderID", orderGroupPurcBean.getId());
		data.put("orderStatus", orderGroupPurcService.getOrderGroupPurStatus(orderGroupPurcBean));
		data.put("businessPhone", orderGroupPurcBean.getBusinessInfo().getPhoneNum());
		List<OrderGroupPurcList> orderGroupPurcLists = orderGroupPurcBean.getOrderGroupPurcLists();
		OrderGroupPurcList orderGroupPurcList = orderGroupPurcLists.get(0);
		data.put("groupBuyID", orderGroupPurcList.getOrderGroupPurcId());
		data.put("groupBuyName", orderGroupPurcList.getName());
		data.put("groupBuyEndTime", DateFormatUtils.format(orderGroupPurcList.getEndTime(), "yyyy-MM-dd"));
		data.put("groupBuyImage", ValidateUtil.getImageUrl(orderGroupPurcList.getImgs(), ValidateUtil.ZERO, request));
		data.put("groupBuyNumber", orderGroupPurcLists.size());
		data.put("isAnyTimeCancel", orderServiceService.getSupportType(orderGroupPurcBean.getSupportType())[0]);
		data.put("orderMoney", orderGroupPurcBean.getPayMoney());

		/* 团购券号开始 */
		List<Map<String, Object>> groupVouchers = new ArrayList<Map<String, Object>>();
		for (OrderGroupPurcList groupPurcList : orderGroupPurcLists) {
			Map<String, Object> groupVoucher = new HashMap<String, Object>();
			groupVoucher.put("code", groupPurcList.getGroupPurcNumber());
			groupVoucher.put("status", groupPurcList.getGroupPurcState());

			groupVouchers.add(groupVoucher);
		}
		data.put("groupVouchers", groupVouchers);
		/* 团购券号结束 */

		data.put("contactPhone", orderGroupPurcBean.getAccountPhoneNumber());
		data.put("leaveMessage", orderGroupPurcBean.getAccountMsg());
		data.put("orderCode", orderGroupPurcBean.getOrderNo());
		data.put("orderTime", DateFormatUtils.format(orderGroupPurcBean.getCreateDate(), "yyyy-MM-dd HH:mm"));
		data.put("paidMethod", orderGroupPurcBean.getPayType());
		/* Data数据结束 */

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", data);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 取消订单
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param orderType
	 *            订单类型（不可空） 1->商品购买订单 2->服务预约订单 3->课程购买订单 4->场地预约订单 5->精品团购订单
	 * @param orderID
	 *            订单ID（不可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "cancelOrder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> cancelOrder(String userID, int orderType, String orderID) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, String.valueOf(orderType), orderID)) {
			return toJson;
		}

		// 根据订单类型调用对应的取消订单接口
		boolean flag = true;
		switch (orderType) {
		case 1:
			flag = orderGoodsService.cancelOrder(orderID, userID, OrderGlobal.CANCEL_TYPE_ACCOUNT);
			break;
		case 2:
			flag = orderServiceService.cancelOrder(orderID, userID, OrderGlobal.CANCEL_TYPE_ACCOUNT);
			break;
		case 3:
			flag = orderLessonService.cancelOrder(orderID, userID, OrderGlobal.CANCEL_TYPE_ACCOUNT);
			break;
		case 4:
			flag = orderFieldService.cancelOrder(orderID, userID, OrderGlobal.CANCEL_TYPE_ACCOUNT);
			break;
		case 5:
			flag = orderGroupPurcService.cancelOrder(orderID, userID, OrderGlobal.CANCEL_TYPE_ACCOUNT);
			break;
		default:
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "订单类型有误");
			return toJson;
		}
		// 判断订单是否可取消
		if (!flag) {
			toJson.put("code", Global.CODE_PROMOT);
			toJson.put("message", "订单取消失败");
			return toJson;
		}

		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("message", "订单取消成功");
		return toJson;
	}

	/**
	 * 团购券申请退款
	 * 
	 * @param userID
	 *            用户ID（不可空）
	 * @param orderID
	 *            订单ID（不可空）
	 * @param groupVouchers
	 *            团购券ID集合（不可空）
	 * @param refundReason
	 *            退款原因（不可空）
	 * @param refundMessage
	 *            退款吐槽（可空）
	 * @return Map<String, Object>
	 */
	@RequestMapping(value = "getUserProfile")
	@ResponseBody
	public Map<String, Object> getUserProfile(String userID, String orderID, String groupVouchers, String refundReason, String refundMessage) {
		// 验证接收到的参数
		Map<String, Object> toJson = new HashMap<String, Object>();
		if (ValidateUtil.validateParams(toJson, userID, orderID, groupVouchers, refundReason)) {
			return toJson;
		}
		/* Data数据开始 */
		Map<String, Object> data = new HashMap<String, Object>();
		/* Data数据结束 */
		toJson.put("code", Global.CODE_SUCCESS);
		toJson.put("data", data);
		toJson.put("message", "信息已获取");
		return toJson;
	}

	/**
	 * 获取订单状态
	 */
	public void getOrderStatus(Map<String, Object> data, String orderId) {
		OrderTrack orderTrack = orderTrackService.getRecentOrderStatus(orderId);
		data.put("orderStatus", orderTrack == null ? "" : orderTrack.getStateMsgPhone());
		data.put("statusName", orderTrack == null ? "" : orderTrack.getStateMsgPhone());
		data.put("statusDesc", orderTrack == null ? "" : orderTrack.getHandleMsgPhone());
		data.put("statusTime", DateFormatUtils.format(orderTrack == null ? new Date() : orderTrack.getCreateDate(), "MM-dd HH:mm"));
	}
}