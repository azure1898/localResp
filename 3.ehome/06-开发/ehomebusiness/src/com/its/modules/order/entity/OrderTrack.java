/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.order.entity;

import org.hibernate.validator.constraints.Length;

import com.its.common.persistence.DataEntity;

/**
 * 订单跟踪表Entity
 * 
 * @author xzc
 * @version 2017-07-11
 */
public class OrderTrack extends DataEntity<OrderTrack> {

    private static final long serialVersionUID = 1L;
    private String orderId; // 订单id
    private String orderType; // 订单类型
    private String orderNo; // 订单号
    private String stateMsg; // 跟踪状态
    private String handleMsg; // 处理信息
    private String stateMsgPhone; // 跟踪状态_手机端
    private String handleMsgPhone; // 处理信息_手机端
    private String createName; // 录入人
    private String businessInfoId;

    public OrderTrack() {
        super();
    }

    public OrderTrack(String id) {
        super(id);
    }

    @Length(min = 0, max = 64, message = "订单号长度必须介于 0 和 64 之间")
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Length(min = 0, max = 64, message = "跟踪状态长度必须介于 0 和 64 之间")
    public String getStateMsg() {
        return stateMsg;
    }

    public void setStateMsg(String stateMsg) {
        this.stateMsg = stateMsg;
    }

    @Length(min = 0, max = 255, message = "处理信息长度必须介于 0 和 255 之间")
    public String getHandleMsg() {
        return handleMsg;
    }

    public void setHandleMsg(String handleMsg) {
        this.handleMsg = handleMsg;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getStateMsgPhone() {
        return stateMsgPhone;
    }

    public void setStateMsgPhone(String stateMsgPhone) {
        this.stateMsgPhone = stateMsgPhone;
    }

    public String getHandleMsgPhone() {
        return handleMsgPhone;
    }

    public void setHandleMsgPhone(String handleMsgPhone) {
        this.handleMsgPhone = handleMsgPhone;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getBusinessInfoId() {
        return businessInfoId;
    }

    public void setBusinessInfoId(String businessInfoId) {
        this.businessInfoId = businessInfoId;
    }

}