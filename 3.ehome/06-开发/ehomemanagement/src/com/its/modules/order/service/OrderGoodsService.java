/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.its.common.persistence.Page;
import com.its.common.service.CrudService;
import com.its.common.utils.WXUtils;
import com.its.modules.order.entity.OrderGoods;
import com.its.modules.order.entity.OrderGoodsList;
import com.its.modules.order.entity.OrderRefundInfo;
import com.its.modules.order.entity.OrderTrack;
import com.its.modules.goods.dao.GoodsInfoDao;
import com.its.modules.goods.dao.GoodsSkuPriceDao;
import com.its.modules.goods.entity.GoodsInfo;
import com.its.modules.goods.entity.GoodsSkuPrice;
import com.its.modules.order.dao.OrderGoodsDao;

/**
 * 订单-商品类Service
 * 
 * @author liuhl
 * @version 2017-07-11
 */
@Service
@Transactional(readOnly = true)
public class OrderGoodsService extends CrudService<OrderGoodsDao, OrderGoods> {

    /** 退款信息明细表Service */
    @Autowired
    private OrderRefundInfoService orderRefundInfoService;

    /** 订单跟踪表Service */
    @Autowired
    private OrderTrackService orderTrackService;

    /** 商品订单明细表SERVICE */
    @Autowired
    private OrderGoodsListService orderGoodsListService;

    /** 商品信息表Dao */
    @Autowired
    private GoodsInfoDao goodsInfoDao;

    /**
     * 商品规格价格Dao
     */
    @Autowired
    private GoodsSkuPriceDao goodsSkuPriceDao;

    public OrderGoods get(String id) {
        return super.get(id);
    }

    public List<OrderGoods> findList(OrderGoods orderGoods) {
        return super.findList(orderGoods);
    }

    public Page<OrderGoods> findPage(Page<OrderGoods> page, OrderGoods orderGoods) {
        return super.findPage(page, orderGoods);
    }

    @Transactional(readOnly = false)
    public void save(OrderGoods orderGoods) {
        super.save(orderGoods);
    }

    @Transactional(readOnly = false)
    public void delete(OrderGoods orderGoods) {
        super.delete(orderGoods);
    }

    /**
     * 订单完成
     * 
     * @param id
     *            订单号
     * @return 更新结果
     */
    @Transactional(readOnly = false)
    public int complete(String id) {
        OrderGoods orderGoods = super.get(id);
        orderGoods.setOverTime(new Date());
        orderGoods.preUpdate();
        int result = this.dao.complete(orderGoods);
        if (result == 0) {
            return result;
        }
        // 跟踪表要插入信息
        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrderNo(orderGoods.getOrderNo());
        orderTrack.setBusinessInfoId(orderGoods.getBusinessInfoId());
        orderTrack.setOrderId(orderGoods.getId());
        orderTrack.setOrderType(orderGoods.getProdType());
        orderTrack.setStateMsg("已完成");
        orderTrack.setHandleMsg("完成服务/送达/已自提");
        orderTrackService.saveOrdMsg(orderTrack);
        return result;

    }

    /**
     * 订单接受
     * 
     * @param id
     *            订单号
     * @return 更新结果
     */
    @Transactional(readOnly = false)
    public int accept(String id) {
        OrderGoods orderGoods = super.get(id);
        orderGoods.preUpdate();
        int result = this.dao.accept(orderGoods);
        if (result == 0) {
            return result;
        }
        // 跟踪表要插入信息
        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrderNo(orderGoods.getOrderNo());
        orderTrack.setBusinessInfoId(orderGoods.getBusinessInfoId());
        orderTrack.setOrderId(orderGoods.getId());
        orderTrack.setOrderType(orderGoods.getProdType());
        orderTrack.setStateMsg("已受理");
        orderTrack.setHandleMsg("商家已受理，准备配送");
        orderTrackService.saveOrdMsg(orderTrack);
        return result;

    }

    /**
     * 订单取消
     * 
     * @param orderGoods
     *            订单取消信息
     * @return 更新结果
     */
    @Transactional(readOnly = false)
    public int cancel(OrderGoods orderGoods) {
        orderGoods.preUpdate();
        // 订单状态更新为：已取消
        int result = this.dao.cancel(orderGoods);
        // 影响条数为0，更新失败
        if (0 == result) {
            return result;
        }

        // 数据库中订单数据（参数是画面传递过来的信息）
        OrderGoods orderGoodsInfo = super.get(orderGoods.getId());
        // 如果订单支付状态为1：已支付的话，执行退款处理
        if ("1".equals(orderGoodsInfo.getPayState())) {
            orderGoodsInfo.preUpdate();
            // 将该订单的支付状态改为2：退款中
            orderGoodsInfo.setPayState("2");
            super.save(orderGoodsInfo);
            // 如果是支付宝的话，生成退款信息，交由总后台进行退款
            if ("1".equals(orderGoodsInfo.getPayOrg())) {
                OrderRefundInfo orderRefundInfo = new OrderRefundInfo();
                orderRefundInfo.setBusinessInfoId(orderGoodsInfo.getBusinessInfoId());
                orderRefundInfo.setOrderNo(orderGoodsInfo.getOrderNo());
                // 因为是商品订单发生退款，所以固定为0：商品类
                orderRefundInfo.setOrderType("0");
                orderRefundInfo.setPayType(orderGoodsInfo.getPayType());
                orderRefundInfo.setOrderMoney(orderGoodsInfo.getPayMoney().toString());
                // 终端类型固定为商家后台
                orderRefundInfo.setType("2");
                orderRefundInfo.setModuleManageId(orderGoodsInfo.getModuleManageId());
                // 产品模式固定为0:商品购买
                orderRefundInfo.setProdType("0");
                orderRefundInfo.setRefundMoney(orderGoodsInfo.getPayMoney().toString());
                orderRefundInfo.setRefundType(orderGoodsInfo.getPayOrg());
                orderRefundInfoService.save(orderRefundInfo);
                // 如果是微信的话，直接调用接口进行退款
                // } else if () {
                // WXUtils.doRefund(out_trade_no, out_refund_no, total_fee,
                // refund_fee, MchID)
                // }
            }

        }

        if (result == 0) {
            return result;
        }
        // 商品订单明细取得
        OrderGoodsList orderGoodsList = new OrderGoodsList();
        // 根据订单号检索
        orderGoodsList.setOrderNo(orderGoods.getOrderNo());
        // 取得该订单对应的信息
        List<OrderGoodsList> orderGoodsInfoList = orderGoodsListService.findList(orderGoodsList);

        List<String> goodsId = new ArrayList<String>();
        // 商品数量信息MAP
        Map<String, Integer> goodsStock = new HashMap<String, Integer>();
        for (OrderGoodsList orderGoodsListTemp : orderGoodsInfoList) {
            goodsId.add(orderGoodsListTemp.getGoodsInfoId());

            // 根据规格项ID以及规格名称ID取得规格库存信息
            GoodsSkuPrice goodsSkuPrice = new GoodsSkuPrice();
            goodsSkuPrice.setGoodsInfoId(new GoodsInfo(orderGoodsListTemp.getGoodsInfoId()));
            goodsSkuPrice.setSkuKeyId(orderGoodsListTemp.getSkuKeyId());
            goodsSkuPrice.setSkuValueId(orderGoodsListTemp.getSkuValueId());
            // 取得商品规格表信息并添加单行锁
            List<GoodsSkuPrice> goodsSkuPriceInfo = goodsSkuPriceDao.findGoodsSkuPriceListForUpdate(goodsSkuPrice);
            // 商品规格表库存回退
            for (GoodsSkuPrice goodsSkuPriceTemp : goodsSkuPriceInfo) {
                goodsSkuPriceTemp.setStock(
                        nullToZero(goodsSkuPriceTemp.getStock()) + nullToZero(orderGoodsListTemp.getGoodsSum()));
                goodsSkuPriceTemp.preUpdate();
                result = goodsSkuPriceDao.update(goodsSkuPriceTemp);
                // 若更新失败则回滚事务
                if (0 == result) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return result;
                }
            }

            // 算出该订单每种商品一共多少库存
            int stock = nullToZero(orderGoodsListTemp.getGoodsSum())
                    + nullToZero(goodsStock.get(orderGoodsListTemp.getGoodsInfoId()));
            goodsStock.put(orderGoodsListTemp.getGoodsInfoId(), stock);
        }
        // 虽然逻辑上不会有商品为空的订单，为了程序的健壮性依然增加了该判断
        if (goodsId != null & goodsId.size() > 0) {
            // 对商品信息添加行级锁
            List<GoodsInfo> goodsInfoList = goodsInfoDao.findGoodsInfoListForUpdate(goodsId);

            // 库存回退
            for (GoodsInfo goodsInfo : goodsInfoList) {
                goodsInfo.preUpdate();
                goodsInfo.setStock(nullToZero(goodsInfo.getStock()) + nullToZero(goodsStock.get(goodsInfo.getId())));
                goodsInfo.preUpdate();
                result = goodsInfoDao.update(goodsInfo);
                // 若更新失败则回滚事务
                if (0 == result) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return result;
                }
            }
        }

        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrderNo(orderGoods.getOrderNo());
        orderTrack.setStateMsg("已取消");
        orderTrack.setHandleMsg("商家取消订单（自动退款）");
        orderTrack.setRemarks(orderGoods.getCancelRemarks());
        orderTrackService.save(orderTrack);
        return result;
    }

    /**
     * 订单配送
     * 
     * @param id
     *            订单号
     * @return 更新结果
     */
    @Transactional(readOnly = false)
    public int dispatching(String id) {
        OrderGoods orderGoods = super.get(id);
        orderGoods.preUpdate();
        int result = this.dao.dispatching(orderGoods);
        if (result == 0) {
            return result;
        }
        // 订单跟踪信息添加
        // 跟踪表要插入信息
        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrderNo(orderGoods.getOrderNo());
        orderTrack.setBusinessInfoId(orderGoods.getBusinessInfoId());
        orderTrack.setOrderId(orderGoods.getId());
        orderTrack.setOrderType(orderGoods.getProdType());
        orderTrack.setStateMsg("配送中");
        orderTrack.setHandleMsg("上门/配送/等待自提中");
        orderTrackService.saveOrdMsg(orderTrack);
        return result;

    }

    /**
     * 更新前的CHECK
     * 
     * @param id
     *            订单号
     * @param updateDate
     *            更新日时
     * @return
     */
    public boolean check(String id, String updateDate) {
        Map<String, String> paramer = new HashMap<String, String>();
        paramer.put("id", id);
        paramer.put("updateDate", updateDate);
        int result = this.dao.check(paramer);
        
        // 更新日时已经发生变化
        if (0 == result) {
            return false;
        }
        return true;
    }

    /**
     * 如果为NULL则变为0
     * 
     * @param number
     *            待转换数字
     * @return
     */
    private Integer nullToZero(Integer number) {
        if (number == null) {
            return NumberUtils.INTEGER_ZERO;
        }
        return number;
    }

    /**
     * 本周商品订单金额
     * @return
     */
    public Double findAllListMoney() {
        return this.dao.findAllListMoney();
    }

    /**
     * 本周商品订单数量
     *
     * @return
     */
    public Integer findAllListCount() {
        return this.dao.findAllListCount();
    }
}