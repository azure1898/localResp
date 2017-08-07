/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.order.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.its.common.config.Global;
import com.its.common.persistence.Page;
import com.its.common.web.BaseController;
import com.its.common.utils.DateUtils;
import com.its.common.utils.MyFDFSClientUtils;
import com.its.common.utils.StringUtils;
import com.its.common.utils.excel.ExportExcel;
import com.its.modules.order.entity.OrderGoods;
import com.its.modules.order.entity.OrderGoodsList;
import com.its.modules.order.entity.OrderRefundInfo;
import com.its.modules.order.entity.OrderTrack;
import com.its.modules.order.service.OrderGoodsListService;
import com.its.modules.order.service.OrderGoodsService;
import com.its.modules.order.service.OrderRefundInfoService;
import com.its.modules.order.service.OrderTrackService;
import com.its.modules.setup.entity.BusinessCategorydict;
import com.its.modules.setup.service.BusinessCategorydictService;
import com.its.modules.sys.entity.User;
import com.its.modules.sys.utils.UserUtils;

/**
 * 订单-商品类Controller
 * 
 * @author liuhl
 * @version 2017-07-11
 */
@Controller
@RequestMapping(value = "${adminPath}/order/orderGoods")
public class OrderGoodsController extends BaseController {

    /** 商品订单表Service */
    @Autowired
    private OrderGoodsService orderGoodsService;

    /** 商品订单明细表表Service */
    @Autowired
    private OrderGoodsListService orderGoodsListService;

    /** 订单跟踪表Service */
    @Autowired
    private OrderTrackService orderTrackService;

    /** 退款信息明细表Service */
    @Autowired
    private OrderRefundInfoService orderRefundInfoService;

    /** 商户分类Service */
    @Autowired
    private BusinessCategorydictService businessCategorydictService;

    /**
     * 订单详情取得
     * 
     * @param id
     *            订单ID
     * @return
     */
    @ModelAttribute
    public OrderGoods get(@RequestParam(required = false) String id) {
        OrderGoods entity = null;
        // 不为空的场合为订单详情页面
        if (StringUtils.isNotBlank(id)) {
            entity = orderGoodsService.get(id);
        }
        if (entity == null) {
            entity = new OrderGoods();
        }
        return entity;
    }

    @RequiresPermissions("order:orderGoods:view")
    @RequestMapping(value = { "list", "" })
    public String list(OrderGoods orderGoods, HttpServletRequest request, HttpServletResponse response, Model model) {

        // 从SESSION中取得商家信息
        User user = UserUtils.getUser();

        // 商家分类信息检索条件
        Map<String, String> paramer = new HashMap<String, String>();
        // 根据当前登陆者的商家ID进行检索
        paramer.put("businessInfoId", user.getBusinessinfoId());
        List<BusinessCategorydict> businessCategorydictList = businessCategorydictService
                .findCategoryListByBusinessId(paramer);
        // 将检索的信息放在画面中
        model.addAttribute("businessCategorydictList", businessCategorydictList);
        // 将上方导航下拉菜单默认选中为商品订单
        model.addAttribute("nowProdType", "0");

        // 只显示当前商家对应的订单
        orderGoods.setBusinessInfoId(user.getBusinessinfoId());
        // 只显当前商家商品的订单
        orderGoods.setProdType("0");

        Page<OrderGoods> page = orderGoodsService.findPage(new Page<OrderGoods>(request, response), orderGoods);

        // 当画面输入检索条件，并检索没有结果
        if (StringUtils.isNotBlank(orderGoods.getSearchFlg())
                && (page.getList() == null || page.getList().size() == 0)) {
            // 当检索条件只有订单号时
            if (StringUtils.isNotBlank(orderGoods.getOrderNo()) && null == orderGoods.getEndCreateDate()
                    && null == orderGoods.getBeginCreateDate() && StringUtils.isBlank(orderGoods.getAddressType())
                    && StringUtils.isBlank(orderGoods.getOrderState())
                    && StringUtils.isBlank(orderGoods.getPayState())) {
                addMessage(model, "请确定您输入的订单号是否正确");
                model.addAttribute("type", "error");
            // 除了订单号作为检索条件，还存在别的条件时
            } else {
                addMessage(model, "您查询的订单未找到");
                model.addAttribute("type", "error");
            }

            // 迁移至商品订单列表页面
            return "modules/order/orderGoodsList";
        }

        for (OrderGoods orderGoodsTemp : page.getList()) {
            // 为了排他处理这里使用乐观锁以更新日时控制
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            orderGoodsTemp.setUpdateDateString(sdf.format(orderGoodsTemp.getUpdateDate()));
        }
        model.addAttribute("page", page);
        // 迁移至商品订单列表页面
        return "modules/order/orderGoodsList";
    }

    /**
     * 订单详情显示预处理
     * 
     * @param orderGoods
     * @param model
     * @return
     */
    @RequiresPermissions("order:orderGoods:view")
    @RequestMapping(value = "form")
    public String form(OrderGoods orderGoods, Model model, HttpServletRequest request) {

        model.addAttribute("orderGoods", orderGoods);
        // 商品订单明细取得
        OrderGoodsList orderGoodsList = new OrderGoodsList();
        // 根据订单号检索
        orderGoodsList.setOrderNo(orderGoods.getOrderNo());
        orderGoods.setOrderGoodsList(orderGoodsListService.findList(orderGoodsList));
        // 編輯取得的订单详情，将取得的文件名加上路径
        for (OrderGoodsList orderGoodsListTemp : orderGoods.getOrderGoodsList()) {
            if (StringUtils.isNotBlank(orderGoodsListTemp.getImgs())) {
                String[] imageNames = orderGoodsListTemp.getImgs().split(",");
                try {
                    // 为了页面大小，显示压缩后的图片
                    orderGoodsListTemp
                            .setImageUrl(MyFDFSClientUtils.get_fdfs_file_url(request, imageNames[0] + "_compress2"));
                } catch (IOException | MyException e) {
                }
            }
        }

        // 为了排他处理这里使用乐观锁以更新日时控制
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orderGoods.setUpdateDateString(sdf.format(orderGoods.getUpdateDate()));

        // 订单跟踪表信息取得
        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrderNo(orderGoods.getOrderNo());
        orderGoods.setOrderTrackList(orderTrackService.findList(orderTrack));

        // 退款信息检索条件
        OrderRefundInfo orderRefundInfo = new OrderRefundInfo();
        // 根据订单号检索信息
        orderRefundInfo.setOrderNo(orderGoods.getOrderNo());
        orderRefundInfoService.findOrderRefundInfoByOrderNo(orderRefundInfo);
        // 将退款完成时间添加到画面中
        model.addAttribute("refundOverTime", orderRefundInfo.getRefundOverTime());
        // 迁移至商品订单详细页面
        return "modules/order/orderGoodsForm";
    }

    /**
     * 商品订单完成
     * 
     * @param orderNo
     *            要处理的商品订单号
     * @param updateDate
     *            前回更新日时
     * @return
     */
    @RequiresPermissions("order:orderGoods:edit")
    @RequestMapping(value = "complete")
    public String complete(@RequestParam(required = true) String id, @RequestParam(required = true) String updateDate,
            RedirectAttributes redirectAttributes) {
        // 如果更新日时已经发生变化，则不再进行更新处理
        if (!orderGoodsService.check(id, updateDate)) {
            addMessage(redirectAttributes, "订单信息已被他人修正，操作失败");
            redirectAttributes.addFlashAttribute("type", "error");
            return "redirect:" + Global.getAdminPath() + "/order/orderGoods/?repage";
        }
        // 返回影响条数
        int result = orderGoodsService.complete(id);
        // 更新条数为0则更新失败
        if (0 == result) {
            addMessage(redirectAttributes, "操作失败");
            redirectAttributes.addFlashAttribute("type", "error");
            // 迁移至商品订单列表页面
            return "redirect:" + Global.getAdminPath() + "/order/orderGoods/?repage";
        } else {
            addMessage(redirectAttributes, "操作成功");
            // 迁移至商品订单列表页面
            return "redirect:" + Global.getAdminPath() + "/order/orderGoods/?repage";
        }
    }

    /**
     * 商品订单受理
     * 
     * @param id
     *            要处理的商品订单号
     * @return
     */
    @RequiresPermissions("order:orderGoods:edit")
    @RequestMapping(value = "accept")
    public String accept(@RequestParam(required = true) String id, @RequestParam(required = true) String updateDate,
            RedirectAttributes redirectAttributes) {
        // 如果更新日时已经发生变化，则不再进行更新处理
        if (!orderGoodsService.check(id, updateDate)) {
            addMessage(redirectAttributes, "订单信息已被他人修正，操作失败");
            redirectAttributes.addFlashAttribute("type", "error");
            return "redirect:" + Global.getAdminPath() + "/order/orderGoods/?repage";
        }
        // 返回影响条数
        int result = orderGoodsService.accept(id);
        // 若更新条数为0则更新失败
        if (0 == result) {
            addMessage(redirectAttributes, "操作失败");
            redirectAttributes.addFlashAttribute("type", "error");
            // 迁移至商品订单列表页面
            return "redirect:" + Global.getAdminPath() + "/order/orderGoods/?repage";
        } else {
            addMessage(redirectAttributes, "操作成功");
            // 迁移至商品订单列表页面
            return "redirect:" + Global.getAdminPath() + "/order/orderGoods/?repage";
        }
    }

    /**
     * 商品订单取消
     * 
     * @param orderTrack
     *            订单取消信息
     * @return
     */
    @RequiresPermissions("order:orderGoods:edit")
    @RequestMapping(value = "cancel")
    public String cancel(OrderGoods orderGoods, Model model, RedirectAttributes redirectAttributes) {
        // 如果更新日时已经发生变化，则不再进行更新处理
        if (!orderGoodsService.check(orderGoods.getId(), orderGoods.getUpdateDateString())) {
            addMessage(redirectAttributes, "订单信息已被他人修正，操作失败");
            redirectAttributes.addFlashAttribute("type", "error");
            return "redirect:" + Global.getAdminPath() + "/order/orderGoods/?repage";
        }
        // 返回影响条数
        int result = orderGoodsService.cancel(orderGoods);
        // 若没更新则显示操作
        if (0 == result) {
            addMessage(redirectAttributes, "操作失败");
            redirectAttributes.addFlashAttribute("type", "error");
            // 迁移至商品订单列表页面
            return "redirect:" + Global.getAdminPath() + "/order/orderGoods/?repage";
        } else {
            addMessage(redirectAttributes, "操作成功");
            // 迁移至商品订单列表页面
            return "redirect:" + Global.getAdminPath() + "/order/orderGoods/?repage";
        }
    }

    /**
     * 商品订单配送
     * 
     * @param id
     *            要处理的商品订单号
     * @param updateDate
     *            更新日时
     * @return
     */
    @RequiresPermissions("order:orderGoods:edit")
    @RequestMapping(value = "dispatching")
    public String dispatching(@RequestParam(required = true) String id,
            @RequestParam(required = true) String updateDate, RedirectAttributes redirectAttributes) {
        // 如果更新日时已经发生变化，则不再进行更新处理
        if (!orderGoodsService.check(id, updateDate)) {
            addMessage(redirectAttributes, "订单信息已被他人修正，操作失败");
            redirectAttributes.addFlashAttribute("type", "error");
            return "redirect:" + Global.getAdminPath() + "/order/orderGoods/?repage";
        }
        int result = orderGoodsService.dispatching(id);
        if (0 == result) {
            addMessage(redirectAttributes, "操作失败");
            redirectAttributes.addFlashAttribute("type", "error");
            // 迁移至商品订单列表页面
            return "redirect:" + Global.getAdminPath() + "/order/orderGoods/?repage";
        } else {
            addMessage(redirectAttributes, "操作成功");
            // 迁移至商品订单列表页面
            return "redirect:" + Global.getAdminPath() + "/order/orderGoods/?repage";
        }
    }

    @RequiresPermissions("order:orderGoods:view")
    @RequestMapping(value = { "export" })
    public String export(OrderGoods orderGoods, HttpServletRequest request, HttpServletResponse response, Model model) {
        // 从SESSION中取得商家信息
        User user = UserUtils.getUser();
        // 只显示当前商家对应的订单
        orderGoods.setBusinessInfoId(user.getBusinessinfoId());
        // 只显当前商家商品的订单
        orderGoods.setProdType("0");

        // EXCEL导出
        try {
            String fileName = DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            List<OrderGoods> orderGoodsList = orderGoodsService.findList(orderGoods);
            if (orderGoodsList == null || orderGoodsList.size() == 0) {
                addMessage(model, "当前检索条件下订单未找到");
                model.addAttribute("type", "error");
                // 迁移至服务订单列表页面
                return "modules/order/orderGoodsList";
            }
            // 数据编辑
            for (OrderGoods orderGoodsExcelData : orderGoodsList) {
                // 时间
                StringBuffer time = new StringBuffer();
                if (null != orderGoodsExcelData.getCreateDate()) {
                    time.append("下单：");
                    time.append(DateUtils.formatDate(orderGoodsExcelData.getCreateDate(), "yyyy-MM-dd HH:mm"));
                }
                if (null != orderGoodsExcelData.getPayTime()) {
                    time.append((char) 10);
                    time.append("支付：");
                    time.append(DateUtils.formatDate(orderGoodsExcelData.getPayTime(), "yyyy-MM-dd HH:mm"));
                }
                // 时间
                orderGoodsExcelData.setTimeForExcel(time.toString());
            }

            new ExportExcel("商品订单数据", OrderGoods.class).setDataList(orderGoodsList).write(response, fileName).dispose();
            // 迁移至服务订单列表页面
            return null;
        } catch (Exception e) {
            addMessage(model, "导出服务订单数据！失败信息：" + e.getMessage());
            model.addAttribute("type", "error");
        }
        // 迁移至服务订单列表页面
        return "modules/order/orderGoodsList";
    }
}