<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>预约场地管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
    $(document).ready(function() {
        //$("#name").focus();
        $("#inputForm").validate({
            rules: {
                cancelRemarks : "required",
                cancelRemarksSelect : "required"
            },
            messages: {
                cancelRemarks : "请填写取消原因",
                cancelRemarksSelect : "请填写取消原因"
            },
            submitHandler: function(form){
                loading('正在提交，请稍等...');
                form.submit();
            },
            errorContainer: "#messageBox",
            errorPlacement: function(error, element) {
                $("#messageBox").text("输入有误，请先更正。");
                if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
                    error.appendTo(element.parent().parent());
                } else {
                    error.insertAfter(element);
                }
            }
        });
    });
    function setRemarks(remarks) {
        $("#cancelRemarks")[0].value = remarks;
        if ("其他原因" == remarks) {
            $("#cancelRemarks")[0].value = "";
            $("#cancelRemarks").css('display','');
        } else {
            $("#cancelRemarks").css('display','none');
        }
    }
    </script>
</head>
<body>
    <div class="form-actions" style="text-align:right;width:95.5%">
    <div style="margin-left:15px;">
    <c:choose>
        <c:when test="${orderField.outTimeState && orderField.orderState == '1'}">
        </c:when>
        <c:when test="${orderField.orderState == '2'}">
        </c:when>
        <c:otherwise>
            <a id="btuElemCancel" class="btn btn-primary" data-toggle="modal" data-target="#myModal${status.index}"><i class="icon-remove icon-custom"></i>取消</a>
        </c:otherwise>
    </c:choose>
    <!-- 模态框（Modal） -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
        <div class="modal-dialog"  style="text-align:left;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">取消订单</h4>
                </div>
            <form:form id="inputForm" modelAttribute="orderField" action="${ctx}/order/orderField/cancel" method="post">
                <div class="modal-body">
                    <select id="cancelRemarksSelect" name="cancelRemarksSelect" onchange="setRemarks(this.value)" style="width:200px;">
                      <option selected value="">请选择订单取消原因</option>
                      <option value="客户联系取消">客户联系取消</option>
                      <option value="无法联系客户">无法联系客户</option>
                      <option value="商品无法送达">商品无法送达</option>
                      <option value="客户未付款，释放库存">客户未付款，释放库存</option>
                      <option value="其他原因">其他原因</option>
                    </select>
                    <br/>
                    <br/>
                    <form:hidden path="id" value="${orderField.id}"/>
                    <form:hidden path="orderNo" value="${orderField.orderNo}"/>
                    <form:hidden path="updateDateString" value="${orderField.updateDateString}"/>
                    <form:input path="cancelRemarks" htmlEscape="false" maxlength="81" style="width:200px;display:none;" placeholder="请填写取消原因" class="input-xlarge"/>
                    <p>订单取消后系统将自动退款。</p>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">确定</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                </div>
            </form:form>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
    <input id="btnBack" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
    </div>
    </div>
    <table id="contentTable" class="table table-striped table-bordered table-condensed" style="width:98.3%">
        <thead>
            <tr>
                <th style="width:15%;">订单号</th>
                <th style="width:10%;">姓名</th>
                <th style="width:15%;">电话</th>
                <th style="width:15%;">预约场地</th>
                <th style="width:6%;">金额</th>
                <th style="width:8%;">终端类型</th>
                <th style="width:8%;">支付状态</th>
                <th style="width:8%;">订单状态</th>
                <th style="width:15%;">时间</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>${orderField.orderNo}</td>
                <td>
                    ${orderField.accountName}
                </td>
                <td>
                    ${orderField.accountPhoneNumber}
                </td>
                <td>
                    ${orderField.name}
                    <c:if test="${orderField.orderFieldList != null}">
                        <br/>
                        <fmt:formatDate value="${orderField.orderFieldList.appointmentTime}" pattern="yyyy年M月d日"/>
                        <br/>
                        <fmt:formatDate value="${orderField.orderFieldList.startTime}" pattern="HH:mm"/>~<fmt:formatDate value="${orderField.orderFieldList.endTime}" pattern="HH:mm"/>
                    </c:if>
                </td>
                <td>
                    ${orderField.payMoney}
                </td>
                <td>
                    ${fns:getDictLabel(orderField.type, 'order_type', "")}
                </td>
                <td <c:if test="${orderField.payState == 0}">style="color:red"</c:if>>
                    ${fns:getDictLabel(orderField.payState, 'pay_goods_state', "")}
                </td>
                <td <c:if test="${orderField.orderState == 0}">style="color:red"</c:if>>
                    <c:choose>
                        <c:when test="${orderField.outTimeState && orderField.orderState == '1'}">
                            <span>已完成</span>
                        </c:when>
                        <c:otherwise>
                            ${fns:getDictLabel(orderField.orderState, 'order_lesson_state', "")}
                        </c:otherwise>
                    </c:choose>
                    
                </td>
                <td>
                    <c:if test="${orderField.createDate !=null && orderField.createDate !=''}">
                    <span>下单：<fmt:formatDate value="${orderField.createDate}" pattern="yyyy-MM-dd HH:mm"/></span><br/>
                    </c:if>
                    <c:if test="${orderField.payTime !=null && orderField.payTime !=''}">
                    <span>支付：<fmt:formatDate value="${orderField.payTime}" pattern="yyyy-MM-dd HH:mm"/></span>
                    </c:if>
                </td>
            </tr>
        </tbody>
    </table>
    <p style="font-weight:bold;">支付信息</p>
    <table style="border:0;width:98.3%">
        <tr>
            <td style="width:25%">
                <p style="padding-left: 20px;">支付方式：${fns:getDictLabel(orderField.payType, 'pay_type', "")}</p>
            </td>
            <td style="width:25%">
                <p>支付机构：${fns:getDictLabel(orderField.payOrg, 'pay_org', "")}</p>
            </td>
            <td style="width:25%">
                <p>支付时间：<fmt:formatDate value="${orderField.payTime}" pattern="yyyy-MM-dd HH:mm"/></p>
            </td>
            <td style="width:25%">
                <p>支付帐号：${orderField.payUserName}</p>
            </td>
        </tr>
        <c:if test="${orderField.payState == '2' || orderField.payState == '3'}">
        <tr>
            <td style="width:25%">
                <p style="padding-left: 20px;">退款状态：${fns:getDictLabel(orderField.payState, 'pay_goods_state', "")}</p>
            </td>
            <td style="width:25%">
            </td>
            <td style="width:25%">
                <p>退款时间：<fmt:formatDate value="${refundOverTime}" pattern="yyyy-MM-dd HH:mm"/></p>
            </td>
            <td style="width:25%">
            </td>
        </tr>
        </c:if>
    </table>
    <p style="font-weight:bold;">订单跟踪</p>
    <table  class="table table-striped table-bordered table-condensed" style="width:98.3%">
        <thead>
            <tr>
                <th>订单状态</th>
                <th>处理时间</th>
                <th>处理信息</th>
                <th>操作账户</th>
                <th>备注</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${orderField.orderTrackList}" var="orderTrack">
            <tr>
                <td style="width:12.5%">
                    ${orderTrack.stateMsg}
                </td>
                <td style="width:25%">
                    <fmt:formatDate value="${orderTrack.createDate}" pattern="yyyy-MM-dd HH:mm"/>
                </td>
                <td style="width:25%">
                    ${orderTrack.handleMsg}
                </td>
                <td style="width:12.5%">
                    ${orderTrack.createName}
                </td>
                <td style="width:25%">
                    ${orderTrack.remarks}
                </td>
        </c:forEach>
        </tbody>
    </table>
    <div class="form-actions" style="text-align:right;width:95.5%">
    <div style="margin-left:15px;">
    <c:choose>
        <c:when test="${orderField.outTimeState && orderField.orderState == '1'}">
        </c:when>
        <c:when test="${orderField.orderState == '2'}">
        </c:when>
        <c:otherwise>
            <a id="btuElemCancel" class="btn btn-primary" data-toggle="modal" data-target="#myModal${status.index}"><i class="icon-remove icon-custom"></i>取消</a>
        </c:otherwise>
    </c:choose>
    <input id="btnBackDown" class="btn" type="button" value="返回" onclick="history.go(-1)"/>
    </div>
    </div>
</body>
</html>