<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>商家结算信息管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
$(document).ready(function() {
    fillPro(); // 加载全部省市区数据
    $("#btnCheck").click(function() {
        var ids = $("#checkedId").val();
        if (ids == "") {
            alertx("请选择批量结算的结算单");
            return;
        } else {
            var href = "${ctx}/balance/businessBalance/batchBalance?ids=" + ids;
            return confirmx('确认要结算？结算后这批结算单将成为已结算状态', href);
        }
    });
    $("#btnExport").click(function() {
        top.$.jBox.confirm("确认要导出结算申请单吗？", "系统提示",
        function(v, h, f) {
            if (v == 'ok') {
                $("#searchForm").prop("action", "${ctx}/balance/businessBalance/export");
                $("#searchForm").submit();
            }
        },
        {
            buttonsFocus: 1
        });
        top.$('.jbox-body .jbox-icon').css('top', '55px');
    });
    $("#btuElemPrint").click(function() {
        /* $("#searchForm").prop("action", "${ctx}/balance/businessBalance/print");
                                            $("#searchForm").submit(); */
        var url = "${ctx}/balance/businessBalance/print?balanceStartTime=<fmt:formatDate value='${businessBalance.balanceStartTime}' pattern='yyyy-MM-dd'/>&balanceEndTime=<fmt:formatDate value='${businessBalance.balanceEndTime}' pattern='yyyy-MM-dd'/>&checkState=${businessBalance.checkState}&balanceState=${businessBalance.balanceState}&businessName=${businessBalance.businessName}";
        windowOpen(url, "结算单打印", "800", "350")
    });
});
function page(n, s) {
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").submit();
    return false;
}
/*
 * 全选
 */
function batchBalance(obj) {
    var checked = obj.checked;
    if (!checked) {
        $("#checkedId").val("");
    }
    var checkBoxs = $('input[name="itemCheckbox"]');
    for (var i = 0; i < checkBoxs.length; i++) {
        checkBoxs[i].checked = checked;
        if (checkBoxs[i].checked) {
            var checkedId = $("#checkedId").val() + checkBoxs[i].value + ",";
            $("#checkedId").val(checkedId);
        }
    }
}
/*
 * 单选
 */
function itemCheck(obj) {
    $("#checkedId").val("");
    var checkBoxs = $('input[name="itemCheckbox"]');
    for (var i = 0; i < checkBoxs.length; i++) {
        if (checkBoxs[i].checked) {
            var checkedId = $("#checkedId").val() + checkBoxs[i].value + ",";
            $("#checkedId").val(checkedId);
        }
    }
}
</script>
<style type="text/css">
.div-inline {
	display: inline;
	padding-right: 50px;
}
</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/balance/businessBalance/">商家结算信息列表</a></li>
		<li><a href="${ctx}/balance/propertyBalance/">物业结算信息列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="businessBalance"
		action="${ctx}/balance/businessBalance/" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<ul class="ul-form">
			<li><input name="balanceStartTime" type="text" placeholder="结算开始时间"
				readonly="readonly" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${businessBalance.balanceStartTime}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			</li>
			<li><input name="balanceEndTime" type="text" readonly="readonly"
				 placeholder="结算结束时间" maxlength="20" class="input-medium Wdate"
				value="<fmt:formatDate value="${businessBalance.balanceEndTime}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
			</li>
			<li class="btns"><select id="addrpro" name="addrPro"
				style="width: 120px" onchange="changeCity()">
					<option value="">全部省份</option>
			</select> <select id="addrcity" name="addrCity" style="width: 120px"
				onchange="changeVillage()">
					<option value="">全部城市</option>
			</select> <select id="addrarea" name="addrArea" style="display: none;">
					<option value="">全部区域</option>
			</select> <select id="addrVillage" name="villageInfoId" style="width: 120px">
					<option value="">全部楼盘</option>
			</select> <input type="text" class="hide" id="hidProId" value="${businessBalance.addrPro}"> <input
				type="text" class="hide" id="hidCityId" value="${businessBalance.addrCity}"> <input
				type="text" class="hide" id="hidAreaId" value=""> <input
				type="text" class="hide" id="hidVillageId"
				value="${couponManage.villageInfoId}"></li>
			<li><form:select path="checkState" class="input-medium">
					<form:option value="" label="核对状态" />
					<form:options items="${fns:getDictList('checkState')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select></li>
			<li><form:select path="balanceState" class="input-medium">
					<form:option value="" label="结算状态" />
					<form:options items="${fns:getDictList('check_state')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select></li>
			<li><form:input path="businessName" placeholder="商家名称"
					htmlEscape="false" maxlength="64" class="input-medium" /></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" /></li>
			<li class="clearfix"></li>
		</ul>
		<!-- 操作按钮 start -->
		<div style="margin: 10px">
			<div class="div-inline">
				<input type="checkbox" onclick="batchBalance(this)"
					title="支持申请结算的记录前会出现勾选框；已结算的结算单不能重复申请结算；待核对的结算单需核对后才能申请结算">全选
			</div>
			<div class="div-inline">
				<shiro:hasPermission name="balance:businessBalance:edit">
					<a class="btn btn-primary" id="btnCheck" href="#"><i
						class="icon-plus icon-custom"></i> 批量结算</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="balance:businessBalance:view">
					<a id="btnExport" href="#" class="btn btn-primary"><i
						class="icon-edit icon-custom"></i> 导出结算申请单</a>
				</shiro:hasPermission>
				<shiro:hasPermission name="balance:businessBalance:edit">
					<a id="btuElemPrint" href="#" class="btn btn-primary"><i
						class="icon-trash icon-custom"></i> 打印</a>
				</shiro:hasPermission>
			</div>
			<div class="div-inline">
				<span class="help-inline"> <font color="red"> 订单金额： <fmt:formatNumber
							type="number" pattern="0.00" maxFractionDigits="2">${sumOrderMoney}</fmt:formatNumber> 元 平台优惠： <fmt:formatNumber
							type="number" pattern="0.00" maxFractionDigits="2">${sumCouponMoney}</fmt:formatNumber> 元 扣点金额： <fmt:formatNumber
							type="number" pattern="0.00" maxFractionDigits="2">${sumDeductionMoney}</fmt:formatNumber> 元 应付金额： <fmt:formatNumber
							type="number" pattern="0.00" maxFractionDigits="2">${sumPayMoney}</fmt:formatNumber> 元
				</font></span>
			</div>
		</div>
		<!-- 操作按钮 end -->
	</form:form>
	<sys:message content="${message}" />
	<input type="hidden" id="checkedId" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序号</th>
				<th>所在城市</th>
				<th>商家名称</th>
				<th>结算周期</th>
				<th>结算状态</th>
				<th>订单金额</th>
				<th>平台优惠</th>
				<th>扣点金额</th>
				<th>应付金额</th>
				<shiro:hasPermission name="balance:businessBalance:edit">
					<th>操作</th>
					<th>核对状态</th>
				</shiro:hasPermission>
			</tr>
		</thead>
		<tbody>

			<c:forEach items="${page.list}" var="businessBalance"
				varStatus="status">
				<tr>
					<td><c:if
							test="${businessBalance.balanceState==0 && businessBalance.checkState !=0}">
							<input type="checkbox" name="itemCheckbox"
								onclick="itemCheck(this)" value="${businessBalance.id}">
						</c:if> ${status.count}</td>
					<td>${businessBalance.cityName}</td>
					<td>${businessBalance.businessName}</td>
					<td><fmt:formatDate
							value="${businessBalance.balanceStartTime}" pattern="yyyy-MM-dd" />
						~ <fmt:formatDate value="${businessBalance.balanceEndTime}"
							pattern="yyyy-MM-dd" /></td>
					<td>${fns:getDictLabel(businessBalance.balanceState, 'check_state', '')}
					</td>
					<td><fmt:formatNumber type="currency">${businessBalance.orderMoney}</fmt:formatNumber>
					</td>
					<td><fmt:formatNumber type="currency">${businessBalance.couponMoney}</fmt:formatNumber>
					</td>
					<td><fmt:formatNumber type="currency">${businessBalance.deductionMoney}</fmt:formatNumber>
					</td>
					<td><fmt:formatNumber type="currency">${businessBalance.payMoney}</fmt:formatNumber>
					</td>
					<shiro:hasPermission name="balance:businessBalance:view">
						<td><a
							href="${ctx}/balance/businessBalanceDetail/list?businessBalanceId=${businessBalance.id}&balanceStartTime=<fmt:formatDate
                           value='${businessBalance.balanceStartTime}' pattern='yyyy-MM-dd' />&balanceEndTime=<fmt:formatDate
                           value='${businessBalance.balanceEndTime}' pattern='yyyy-MM-dd' />">结算单明细</a>
						</td>
						<td id="${businessBalance.id}">
						<c:choose>
						  <c:when test="${businessBalance.checkState==0 }">
	                        <a
	                            href="${ctx}/balance/businessBalance/check?id=${businessBalance.id}">
	                                ${fns:getDictLabel(businessBalance.checkState, 'checkState', '')}
	                        </a>
						  </c:when>
						  <c:otherwise>
                                ${fns:getDictLabel(businessBalance.checkState, 'checkState', '')}
						  </c:otherwise>
						</c:choose></td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>