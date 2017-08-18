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
import com.its.modules.order.entity.OrderService;
import com.its.modules.order.entity.OrderServiceList;
import com.its.modules.order.entity.OrderRefundInfo;
import com.its.modules.order.entity.OrderTrack;
import com.its.modules.order.service.OrderRefundInfoService;
import com.its.modules.order.service.OrderServiceListService;
import com.its.modules.order.service.OrderServiceService;
import com.its.modules.order.service.OrderTrackService;
import com.its.modules.setup.entity.BusinessCategorydict;
import com.its.modules.setup.service.BusinessCategorydictService;
import com.its.modules.sys.entity.User;
import com.its.modules.sys.utils.DictUtils;
import com.its.modules.sys.utils.UserUtils;

/**
 * 订单-服务类Controller
 * 
 * @author liuhl
 * @version 2017-07-17
 */
@Controller
@RequestMapping(value = "${adminPath}/order/orderService")
public class OrderServiceController extends BaseController {

    @Autowired
    private OrderServiceService orderServiceService;

    /** 订单跟踪表Service */
    @Autowired
    private OrderTrackService orderTrackService;

    /** 商户分类Service */
    @Autowired
    private BusinessCategorydictService businessCategorydictService;

    /** 退款信息明细表Service */
    @Autowired
    private OrderRefundInfoService orderRefundInfoService;

    /** 商品订单明细表表Service */
    @Autowired
    private OrderServiceListService orderServiceListService;

    @ModelAttribute
    public OrderService get(@RequestParam(required = false) String id) {
        OrderService entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = orderServiceService.get(id);
        }
        if (entity == null) {
            entity = new OrderService();
        }
        return entity;
    }

    @RequiresPermissions("order:orderService:view")
    @RequestMapping(value = { "list", "" })
    public String list(OrderService orderService, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        // 从SESSION中取得商家信息
        User user = UserUtils.getUser();
        // 只显示当前商家对应的订单
        orderService.setBusinessInfoId(user.getBusinessinfoId());
        // 只显当前商家服务的订单
        orderService.setProdType("1");
        Page<OrderService> page = orderServiceService.findPage(new Page<OrderService>(request, response), orderService);
        // 商家分类信息检索条件
        Map<String, String> paramer = new HashMap<String, String>();
        // 根据当前登陆者的商家ID进行检索
        paramer.put("businessInfoId", user.getBusinessinfoId());
        List<BusinessCategorydict> businessCategorydictList = businessCategorydictService
                .findCategoryListByBusinessId(paramer);
        // 将检索的信息放在画面中
        model.addAttribute("businessCategorydictList", businessCategorydictList);
        // 将上方导航下拉菜单默认选中为服务订单
        model.addAttribute("nowProdType", "1");

        // 当画面输入检索条件，并检索时
        if (StringUtils.isNotBlank(orderService.getSearchFlg())
                && (page.getList() == null || page.getList().size() == 0)) {
            // 当检索条件只有订单号时
            if (StringUtils.isNotBlank(orderService.getOrderNo()) && null == orderService.getEndCreateDate()
                    && null == orderService.getBeginCreateDate() && StringUtils.isBlank(orderService.getServiceType())
                    && StringUtils.isBlank(orderService.getOrderState())
                    && StringUtils.isBlank(orderService.getPayState())) {
                addMessage(model, "请确定您输入的订单号是否正确");
                model.addAttribute("type", "error");
                // 除了订单号作为检索条件，还存在别的条件时
            } else {
                addMessage(model, "您查询的订单未找到");
                model.addAttribute("type", "error");
            }
            // 迁移至服务订单列表页面
            return "modules/order/orderServiceList";
        }

        for (OrderService orderServiceTemp : page.getList()) {
            // 为了排他处理这里使用乐观锁以更新日时控制
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            orderServiceTemp.setUpdateDateString(sdf.format(orderServiceTemp.getUpdateDate()));
        }
        model.addAttribute("page", page);
        // 迁移至服务订单列表页面
        return "modules/order/orderServiceList";
    }

    @RequiresPermissions("order:orderService:view")
    @RequestMapping(value = { "export" })
    public String export(OrderService orderService, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        // 从SESSION中取得商家信息
        User user = UserUtils.getUser();
        // 只显示当前商家对应的订单
        orderService.setBusinessInfoId(user.getBusinessinfoId());
        // 只显当前商家服务的订单
        orderService.setProdType("1");

        // EXCEL导出
        try {
            String fileName = DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            List<OrderService> orderServiceList = orderServiceService.findList(orderService);
            if (orderServiceList == null || orderServiceList.size() == 0) {
                addMessage(model, "当前检索条件下订单未找到");
                // 迁移至服务订单列表页面
                return "modules/order/orderServiceList";
            }
            // 数据编辑
            for (OrderService orderServiceExcelData : orderServiceList) {
                // 服务方式
                orderServiceExcelData.setServiceTypeLabel(
                        DictUtils.getDictLabel(orderServiceExcelData.getServiceType(), "order_service_type", ""));
                // 服务方式
                orderServiceExcelData.setPayStateLabel(
                        DictUtils.getDictLabel(orderServiceExcelData.getPayState(), "pay_goods_state", ""));
                // 服务方式
                orderServiceExcelData.setOrderStateLabel(
                        DictUtils.getDictLabel(orderServiceExcelData.getOrderState(), "order_service_state", ""));
                // 时间
                StringBuffer time = new StringBuffer();
                if (null != orderServiceExcelData.getCreateDate()) {
                    time.append("下单：");
                    time.append(DateUtils.formatDate(orderServiceExcelData.getCreateDate(), "yyyy-MM-dd HH:mm"));
                }
                if (null != orderServiceExcelData.getPayTime()) {
                    time.append((char) 10);
                    time.append("支付：");
                    time.append(DateUtils.formatDate(orderServiceExcelData.getPayTime(), "yyyy-MM-dd HH:mm"));
                }
                orderServiceExcelData.setTimeForExcel(time.toString());
            }

            new ExportExcel("服务订单数据", OrderService.class).setDataList(orderServiceList).write(response, fileName)
                    .dispose();
            // 迁移至服务订单列表页面
            return null;
        } catch (Exception e) {
            addMessage(model, "导出服务订单数据！失败信息：" + e.getMessage());
        }
        // 迁移至服务订单列表页面
        return "modules/order/orderServiceList";
    }

    @RequiresPermissions("order:orderService:view")
    @RequestMapping(value = "form")
    public String form(OrderService orderService, Model model, HttpServletRequest request) {

        model.addAttribute("orderService", orderService);
        // 商品订单明细取得
        OrderServiceList orderServiceList = new OrderServiceList();
        // 根据订单号检索
        orderServiceList.setOrderNo(orderService.getOrderNo());
        orderService.setOrderServiceList(orderServiceListService.findList(orderServiceList));
        // 編輯取得的订单详情，将取得的文件名加上路径
        for (OrderServiceList orderServiceListTemp : orderService.getOrderServiceList()) {
            if (StringUtils.isNotBlank(orderServiceListTemp.getImgs())) {
                String[] imageNames = orderServiceListTemp.getImgs().split(",");
                try {
                    // 为了页面大小，显示压缩后的图片
                    orderServiceListTemp
                            .setImageUrl(MyFDFSClientUtils.get_fdfs_file_url(request, imageNames[0] + "_compress2"));
                } catch (IOException | MyException e) {
                }
            }
        }

        // 为了排他处理这里使用乐观锁以更新日时控制
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orderService.setUpdateDateString(sdf.format(orderService.getUpdateDate()));

        // 订单跟踪表信息取得
        OrderTrack orderTrack = new OrderTrack();
        orderTrack.setOrderNo(orderService.getOrderNo());
        orderService.setOrderTrackList(orderTrackService.findList(orderTrack));

        // 退款信息检索条件
        OrderRefundInfo orderRefundInfo = new OrderRefundInfo();
        // 根据订单号检索信息
        orderRefundInfo.setOrderNo(orderService.getOrderNo());
        orderRefundInfo = orderRefundInfoService.findOrderRefundInfoByOrderNo(orderRefundInfo);
        if (null != orderRefundInfo) {
            // 将退款完成时间添加到画面中
            model.addAttribute("refundOverTime", orderRefundInfo.getRefundOverTime());
        }
        // 迁移至服务订单详细页面
        return "modules/order/orderServiceForm";
    }

    /**
     * 服务订单取消
     * 
     * @param orderTrack
     *            订单取消信息
     * @return
     */
    @RequiresPermissions("order:orderService:cancel")
    @RequestMapping(value = "cancel")
    public String cancel(OrderService orderService, Model model, RedirectAttributes redirectAttributes,
            String redirectUrl) {
        if (redirectUrl != null && redirectUrl != "")
            redirectUrl = "redirect:" + Global.getAdminPath() + redirectUrl;
        else
            redirectUrl = "redirect:" + Global.getAdminPath() + "/order/orderService/?repage";
        // 如果更新日时已经发生变化，则不再进行更新处理
        if (!orderServiceService.check(orderService.getId(), orderService.getUpdateDateString())) {
            addMessage(redirectAttributes, "订单信息已被他人修正，操作失败");
            return redirectUrl;
        }
        // 返回影响条数
        int result = 0;
        try {
            result = orderServiceService.cancel(orderService);
        } catch (Exception e) {
            addMessage(redirectAttributes, "操作失败");
            redirectAttributes.addFlashAttribute("type", "error");
            // 迁移至商品订单列表页面
            return redirectUrl;
        }
        // 若没更新则显示操作
        if (0 == result) {
            addMessage(redirectAttributes, "操作失败");
            // 迁移至服务订单列表页面
            return redirectUrl;
        } else {
            addMessage(redirectAttributes, "操作成功");
            // 迁移至服务订单列表页面
            return redirectUrl;
        }
    }

    /**
     * 服务订单完成
     * 
     * @param orderNo
     *            要处理的服务订单号
     * @param updateDate
     *            前回更新日时
     * @return
     */
    @RequiresPermissions("order:orderService:complete")
    @RequestMapping(value = "complete")
    public String complete(@RequestParam(required = true) String id, @RequestParam(required = true) String updateDate,
            RedirectAttributes redirectAttributes, String redirectUrl) {
        if (redirectUrl != null && redirectUrl != "")
            redirectUrl = "redirect:" + Global.getAdminPath() + redirectUrl;
        else
            redirectUrl = "redirect:" + Global.getAdminPath() + "/order/orderService/?repage";
        // 如果更新日时已经发生变化，则不再进行更新处理
        if (!orderServiceService.check(id, updateDate)) {
            addMessage(redirectAttributes, "订单信息已被他人修正，操作失败");
            return redirectUrl;
        }
        // 返回影响条数
        int result = orderServiceService.complete(id);
        // 更新条数为0则更新失败
        if (0 == result) {
            addMessage(redirectAttributes, "操作失败");
            // 迁移至服务订单列表页面
            return redirectUrl;
        } else {
            addMessage(redirectAttributes, "操作成功");
            // 迁移至服务订单列表页面
            return redirectUrl;
        }
    }

    /**
     * 服务订单受理
     * 
     * @param orderNo
     *            要处理的服务订单号
     * @return
     */
    @RequiresPermissions("order:orderService:accept")
    @RequestMapping(value = "accept")
    public String accept(@RequestParam(required = true) String id, @RequestParam(required = true) String updateDate,
            RedirectAttributes redirectAttributes, String redirectUrl) {
        if (redirectUrl != null && redirectUrl != "")
            redirectUrl = "redirect:" + Global.getAdminPath() + redirectUrl;
        else
            redirectUrl = "redirect:" + Global.getAdminPath() + "/order/orderService/?repage";
        // 如果更新日时已经发生变化，则不再进行更新处理
        if (!orderServiceService.check(id, updateDate)) {
            addMessage(redirectAttributes, "订单信息已被他人修正，操作失败");
            return redirectUrl;
        }
        // 返回影响条数
        int result = orderServiceService.accept(id);
        // 若更新条数为0则更新失败
        if (0 == result) {
            addMessage(redirectAttributes, "操作失败");
            // 迁移至服务订单列表页面
            return redirectUrl;
        } else {
            addMessage(redirectAttributes, "操作成功");
            // 迁移至服务订单列表页面
            return redirectUrl;
        }
    }
}