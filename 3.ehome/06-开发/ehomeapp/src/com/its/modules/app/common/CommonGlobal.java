package com.its.modules.app.common;

/**
 * 通用字典表
 */
public class CommonGlobal {
	/** 否 */
	public static final String NO = "0";
	/** 是 */
	public static final String YES = "1";

	/* <============================我的订单筛选模块============================> */
	/** 全部订单：模块ID */
	public static final String ALL_ORDER_MODULEID = "-1";
	/** 全部订单：文字描述 */
	public static final String ALL_ORDER_DESC = "全部订单";
	/** 精品团购 ：模块ID */
	public static final String GROUP_PURCHASE_MODULEID = "-2";
	/** 精品团购：文字描述 */
	public static final String GROUP_PURCHASE_DESC = "精品团购";

	/* <==============================推荐位置==============================> */
	/** 推荐位置：首页 */
	public static final String RECOMMEND_MAINT = "00";
	/** 推荐位置：社区 */
	public static final String RECOMMEND_COMMUNITY = "10";
	/** 推荐位置：社区更多 */
	public static final String RECOMMEND_COMMUNITY_MORE = "11";
	/** 推荐位置：生活 */
	public static final String RECOMMEND_LIFE = "20";

	/* <==============================楼盘的主导航==============================> */
	/** 主导航：首页 */
	public static final String MAIN_NAVIGATION_MAINT = "0";
	/** 主导航：社区 */
	public static final String MAIN_NAVIGATION_COMMUNITY = "1";
	/** 主导航：生活 */
	public static final String MAIN_NAVIGATION_LIFE = "2";
	/** 主导航：邻里圈 */
	public static final String MAIN_NAVIGATION_NEIGHBOR = "3";

	/* <==============================地址管理类型==============================> */
	/** 地址类型：按楼栋选择地址 */
	public static final String ADDRESS_TYPE_CHOICE = "0";
	/** 地址类型：手动输入地址 */
	public static final String ADDRESS_TYPE_INPUT = "1";

	/* <==============================优惠券使用状态==============================> */
	/** 会员优惠券使用状态：未使用 */
	public static final String DISCOUNT_USE_STATE_UNUSED = "0";
	/** 会员优惠券使用状态：已使用 */
	public static final String DISCOUNT_USE_STATE_USED = "1";
	/** 会员优惠券使用状态：已过期 */
	public static final String DISCOUNT_USE_STATE_EXPIRE = "2";
	/** 会员优惠券使用状态：已冻结 */
	public static final String DISCOUNT_USE_STATE_FROST = "3";

	/* <==============================优惠券领取方式==============================> */
	/** 优惠券领取方式：买家领取 */
	public static final String COUPON_RECEIVE_TYPE_RECEIVE = "0";
	/** 优惠券领取方式：下单赠送 */
	public static final String COUPON_RECEIVE_TYPE_ORDER = "1";
	/** 优惠券领取方式：平台推送 */
	public static final String COUPON_RECEIVE_TYPE_PLATFORM = "2";

	/* <==============================优惠券使用范围==============================> */
	/** 优惠券使用范围：无限制 */
	public static final String COUPON_USE_SCOPE_UNLIMIT = "0";
	/** 优惠券使用范围：服务品类专享 */
	public static final String COUPON_USE_SCOPE_SERVICE = "1";
	/** 优惠券使用范围：商家专享 */
	public static final String COUPON_USE_SCOPE_BUSINESS = "2";

	/* <===============================优惠券类型===============================> */
	/** 优惠券类型：固定金额券 */
	public static final String COUPON_TYPE_FIXED = "0";
	/** 优惠券类型：折扣券 */
	public static final String COUPON_TYPE_DISCOUNT = "1";

	/* <==============================优惠券使用条件==============================> */
	/** 优惠券使用条件：无限制 */
	public static final String COUPON_USE_RULE_UNLIMIT = "0";
	/** 优惠券使用条件：满额使用 */
	public static final String COUPON_USE_RULE_FULL = "1";

	/* <==============================优惠券发放类型==============================> */
	/** 优惠券发放类型：无限制 */
	public static final String COUPON_GRANT_TYPE_UNLIMIT = "0";
	/** 优惠券发放类型：限量发送 */
	public static final String COUPON_GRANT_TYPE_LIMIT = "1";

	/* <==============================优惠券领取规则==============================> */
	/** 优惠券领取规则：无限制 */
	public static final String COUPON_RECEIVE_RULE_UNLIMIT = "0";
	/** 优惠券领取规则：每人每日限领1张 */
	public static final String COUPON_RECEIVE_RULE_LIMITONE = "1";
	/** 优惠券领取规则：每人限领1张 */
	public static final String COUPON_RECEIVE_RULE_ONLYONE = "2";

	/* <==============================商家服务时段==============================> */
	/** 商家服务时段类型：上门服务时段 */
	public static final String BUSINESS_TIME_TYPE_VISIT = "0";
	/** 商家服务时段类型：到店服务时段 */
	public static final String BUSINESS_TIME_TYPE_ARRIVAL = "1";
	/** 商家服务时段类型：上门配送时段 */
	public static final String BUSINESS_TIME_TYPE_DELIVERY = "2";

	/* <==============================预约服务方式==============================> */
	/** 预约服务方式：上门 */
	public static final String APPOINT_SERVICE_TYPE_VISIT = "0";
	/** 预约服务方式：到店 */
	public static final String APPOINT_SERVICE_TYPE_ARRIVAL = "1";

	/* <==============================团购活动状态==============================> */
	/** 团购活动状态：待开始 */
	public static final String ACTIVITY_GROUP_PURCHASE_UNSTART = "0";
	/** 团购活动状态：活动中 */
	public static final String ACTIVITY_GROUP_PURCHASE_START = "1";
	/** 团购活动状态：已结束 */
	public static final String ACTIVITY_GROUP_PURCHASE_END = "2";
	/** 团购活动状态：已撤消 */
	public static final String ACTIVITY_GROUP_PURCHASE_REVOKED = "3";

}