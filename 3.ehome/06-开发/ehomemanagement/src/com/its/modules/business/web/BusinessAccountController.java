/**
 * Copyright &copy; 2012-2014 <a href="https://its111.com">Its111</a> All rights reserved.
 */
package com.its.modules.business.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.its.common.config.Global;
import com.its.common.persistence.Page;
import com.its.common.utils.StringUtils;
import com.its.common.web.BaseController;
import com.its.modules.sys.entity.Office;
import com.its.modules.sys.entity.Role;
import com.its.modules.sys.entity.User;
import com.its.modules.sys.service.SystemService;
import com.its.modules.sys.utils.UserUtils;

/**
 * 商户账户信息Controller
 * 
 * @author zhujiao
 * @version 2017-06-29
 */
@Controller
@RequestMapping(value = "${adminPath}/business/businessAccount")
public class BusinessAccountController extends BaseController {

    /** 来源于JSP页面的FLG：解冻 */
    private static final String FLAG_FROM_JSP_UNFROZEN = "0";
    /** 用户登录状态：冻结 */
    private static final String LOGIN_FLAG_FROZEN = "0";
    /** 用户登录状态：解冻 */
    private static final String LOGIN_FLAG_UNFROZEN = "1";
    @Autowired
    private SystemService systemService;

    @ModelAttribute
    public User get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return systemService.getUser(id);
        } else {
            return new User();
        }
    }

    @RequiresPermissions("business:businessAccount:view")
    @RequestMapping(value = { "list", "" })
    public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
        user.setLoginFlag("");
        Page<User> page = systemService.findUser(new Page<User>(request, response), user);
        model.addAttribute("page", page);
        return "modules/business/businessAccountList";
    }

    @RequiresPermissions("business:businessAccount:view")
    @RequestMapping(value = "form")

    public String form(User user, Model model) {
        if (user.getCompany() == null || user.getCompany().getId() == null) {
            user.setCompany(UserUtils.getUser().getCompany());
        }
        if (user.getOffice() == null || user.getOffice().getId() == null) {
            user.setOffice(UserUtils.getUser().getOffice());
        }
        model.addAttribute("user", user);
        model.addAttribute("allRoles", systemService.findAllRole());
        return "modules/business/businessAccountForm";
    }

    @RequiresPermissions(value = { "business:businessAccount:add", "business:businessAccount:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/user/list?repage";
        }
        // 修正引用赋值问题，不知道为何，Company和Office引用的一个实例地址，修改了一个，另外一个跟着修改。
        user.setCompany(new Office(request.getParameter("company.id")));
        user.setOffice(new Office(request.getParameter("office.id")));
        String prodType = request.getParameter("role.id");
        // 如果新密码为空，则不更换密码
        if (StringUtils.isNotBlank(user.getNewPassword())) {
            user.setPassword(SystemService.entryptPassword(user.getNewPassword()));
        }
        if (!beanValidator(model, user)) {
            return form(user, model);
        }
        if (!"true".equals(checkLoginName(user.getOldLoginName(), user.getLoginName()))) {
            addMessage(model, "保存用户'" + user.getLoginName() + "'失败，登录名已存在");
            return form(user, model);
        }
        // 角色数据有效性验证，过滤不在授权内的角色
        List<Role> roleList = Lists.newArrayList();
        List<String> roleIdList = user.getRoleIdList();
        for (Role r : systemService.findAllRole()) {
            if (roleIdList.contains(r.getId())) {
                roleList.add(r);
            }
        }
        user.setRoleList(roleList);
        // 保存用户信息
        systemService.saveUser(user);
        // 清除当前用户缓存
        if (user.getLoginName().equals(UserUtils.getUser().getLoginName())) {
            UserUtils.clearCache();
            // UserUtils.getCacheMap().clear();
        }
        addMessage(redirectAttributes, "保存商户账户信息'" + user.getLoginName() + "'成功");
        return "redirect:" + Global.getAdminPath() + "/business/businessAccount/list?businessinfoId=" + user.getBusinessinfoId() + "&prodType=" + prodType;
    }

    @RequiresPermissions("business:businessAccount:delete")
    @RequestMapping(value = "delete")
    public String delete(User user, RedirectAttributes redirectAttributes, @RequestParam(required = true) String prodType) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/business/businessAccount/list?repage";
        }
        if (UserUtils.getUser().getId().equals(user.getId())) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
        } else if (User.isAdmin(user.getId())) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
        } else {
            systemService.deleteUser(user);
            addMessage(redirectAttributes, "删除用户成功");
        }
        return "redirect:" + Global.getAdminPath() + "/business/businessAccount/list?businessinfoId=" + user.getBusinessinfoId() + "&prodType=" + prodType;
    }

    @RequiresPermissions(value = { "business:businessAccount:frozen", "business:businessAccount:unfrozen" }, logical = Logical.OR)
    @RequestMapping(value = "updateLoginFlag")
    public String updateLoginFlag(User user, RedirectAttributes redirectAttributes, @RequestParam(required = true) String prodType) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/business/businessAccount/list?businessinfoId=" + user.getBusinessinfoId();
        }
        String flagName = "";
        if (user.getLoginFlag().equals(FLAG_FROM_JSP_UNFROZEN)) {
            user.setLoginFlag(LOGIN_FLAG_UNFROZEN);
            flagName = "取消冻结";
        } else {
            user.setLoginFlag(LOGIN_FLAG_FROZEN);
            flagName = "冻结";
        }
        if (UserUtils.getUser().getId().equals(user.getId())) {
            addMessage(redirectAttributes, flagName + "商户账号失败, 不允许" + flagName + "当前用户");
        } else {
            systemService.updateUserLoginFlag(user);
            addMessage(redirectAttributes, flagName + "商户账号成功");
        }
        return "redirect:" + adminPath + "/business/businessAccount/list?businessinfoId=" + user.getBusinessinfoId() + "&prodType=" + prodType;
    }

    /**
     * 验证登录名是否有效
     * 
     * @param oldLoginName
     * @param loginName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkLoginName")
    public String checkLoginName(String oldLoginName, String loginName) {
        if (loginName != null && loginName.equals(oldLoginName)) {
            return "true";
        } else if (loginName != null && systemService.getUserByLoginName2(loginName) == null) {
            return "true";
        }
        return "false";
    }

}