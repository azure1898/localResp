<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
	<title>${fns:getConfig('productName')}</title>
	<meta name="decorator" content="blank"/><c:set var="tabmode" value="${empty cookie.tabmode.value ? '1' : cookie.tabmode.value}"/>
    <c:if test="${tabmode eq '1'}"><link rel="Stylesheet" href="${ctxStatic}/jerichotab/css/jquery.jerichotab.css" />
    <script type="text/javascript" src="${ctxStatic}/jerichotab/js/jquery.jerichotab.js"></script></c:if>
	<style type="text/css">
		#main {padding:0;margin:0;} #main .container-fluid{padding:0 4px 0 0px;}
		#header {margin:0px;position:static;} 
		#header li {font-size:14px;_font-size:12px;}
		#header .brand {font-family:Helvetica, Georgia, Arial, sans-serif, 宋体;font-size:26px;padding:0px; padding-left:33px; padding-right:20px; }
		#header .brand>img{height:40px; padding-top:10px;}
		#footer {margin:8px 0 0 0;padding:3px 0 0 0;font-size:11px;text-align:center;border-top:2px solid #2dc78a;}
		#footer, #footer a {color:#999;} #left{overflow-x:hidden;overflow-y:auto;background: #fbfbfb;} #left .collapse{position:static;}
		#userControl>li>a{/*color:#fff;*/text-shadow:none;} #userControl>li>a:hover, #user #userControl>li.open>a{background:transparent;}
	#openClose, #openClose.close{background-color:#fbfbfb;}
	.navbar-inner {
    height: 60px;
    padding-right: 20px;
    padding-left: 20px;
    background-color: #373e53;
    background-image: linear-gradient(to bottom, #373e53, #373e53);
    border: 0px; 
    }
    .navbar .brand {
	padding: 18px 20px;
	font-family: 'Telex', sans-serif;
	text-shadow: 1px 1px 0 rgba(0, 0, 0, 0.2)
}
#productName{
color: #2dc78a;

}
.accordion-inner{padding:0px;}
.accordion-group{-webkit-border-radius:0px;border-radius:0px;border:none; border-bottom:1px solid #dee2ee;  margin-bottom:0px;}
.accordion-inner .nav-list li a{ padding:0px 0px 0px 20px;}
.accordion-inner ul li a i{display:none}
.accordion-inner ul li.active a i{display:inline; float:right; line-height:24px; margin-right:5px}
.accordion-heading{ border-radius:0px; height:40px;}
.accordion-heading .accordion-toggle{ padding:0px 15px; line-height:40px;}
.accordion-heading .accordion-toggle>i.fa{ float:right; margin-right:-8px; line-height:30px;}
#jerichotab .tab_pages>div.tabs{ border-bottom:2px solid #2dc78a; height:30px;}
#jerichotab .tab_pages>div.tabs>ul>li>div.tab_left>div.tab_close>a{background:none;}
#jerichotab .tab_pages>div.tabs>ul>li.tab_unselect>div.tab_left>div.tab_close>a{margin-top:8px;}
#jerichotab .tab_pages>div.tabs>ul>li>div.tab_left>div.tab_close>a>i{float:left;}
#jerichotab .tab_pages>div.tabs>ul>li>div{ background:none;border:1px solid #ccc; height:30px; line-height:30px;}
#jerichotab .tab_pages>div.tabs>ul>li>div.tab_left{ border-right-width:0px; margin:0px;}
#jerichotab .tab_pages>div.tabs>ul>li>div.tab_right{border-left-width:0px; margin:0px;}
#jerichotab .tab_pages>div.tabs>ul>li>div>div{height:30px; line-height:30px;}
#jerichotab .tab_pages>div.tabs>ul>li.tab_selected>div{ background-color:#2dc78a; border-color:#2dc78a; color:#fff;}
#jerichotab .tab_pages>div.tabs>ul>li.tab_selected>div.tab_left>div.tab_close>a{color:#fff}
.jericho_tab .tab_pages{padding-top:10px;}
.nav{
color:#ced0d7;
font-size: 12px;
}
#time1{
color:#ced0d7;
    line-height: 24px;
    text-indent: 20px;
    border-left: 2px solid #535a6c;}
.nav_left{
color:#ced0d7;
line-height: 14px;
   text-indent: 15px;
   font-size:12px;
   border-left: 1px solid #bdbfc6;}
.navbar .nav.pull-right {
  color:#ced0d7;
   font-size:12px;
    margin-top: 8px;
}
.nav_right{
color:#ced0d7;
line-height: 14px;
   text-indent: 15px;
   font-size:12px;
   border-left: 0px solid #bdbfc6;
}
	</style>
	<script type="text/javascript">
		$(document).ready(function() {
			// <c:if test="${tabmode eq '1'}"> 初始化页签
			$.fn.initJerichoTab({
                renderTo: '#right', uniqueId: 'jerichotab',
                contentCss: { 'height': $('#right').height() - tabTitleHeight },
                tabs: [], loadOnce: true, tabWidth: 110, titleHeight: tabTitleHeight
            });//</c:if>
			// 绑定菜单单击事件
			
			
			$("#menu a.menu").click(function(){
				// 一级菜单焦点
				$("#menu li.menu").removeClass("active");
				$(this).parent().addClass("active");
				// 左侧区域隐藏
				if ($(this).attr("target") == "mainFrame"){
					$("#left,#openClose").hide();
					wSizeWidth();
					// <c:if test="${tabmode eq '1'}"> 隐藏页签
					$(".jericho_tab").hide();
					$("#mainFrame").show();//</c:if>
					return true;
				}
				// 左侧区域显示
				$("#left,#openClose").show();
				if(!$("#openClose").hasClass("close")){
					$("#openClose").click();
				}
				// 显示二级菜单
				var menuId = "#menu-" + $(this).attr("data-id");
				if ($(menuId).length > 0){
					$("#left .accordion").hide();
					$(menuId).show();
					// 初始化点击第一个二级菜单
					if (!$(menuId + " .accordion-body:first").hasClass('in')){
						$(menuId + " .accordion-heading:first a").click();
					}
					if (!$(menuId + " .accordion-body li:first ul:first").is(":visible")){
						$(menuId + " .accordion-body a:first i").click();
					}
					// 初始化点击第一个三级菜单
					$(menuId + " .accordion-body li:first li:first a:first i").click();
				}else{
					// 获取二级菜单数据
					$.get($(this).attr("data-href"), function(data){
						if (data.indexOf("id=\"loginForm\"") != -1){
							alert('未登录或登录超时。请重新登录，谢谢！');
							top.location = "${ctx}";
							return false;
						}
						$("#left .accordion").hide();
						$("#left").append(data);
						// 链接去掉虚框
						$(menuId + " a").bind("focus",function() {
							if(this.blur) {this.blur()};
						});
						// 二级标题
						$(menuId + " .accordion-heading a").click(function(){
							$(menuId + " .accordion-toggle i").removeClass('fa fa-caret-down').addClass('fa fa-caret-right');
							if(!$($(this).attr('data-href')).hasClass('in')){
								$(this).children("i").removeClass('fa fa-caret-right').addClass('fa fa-caret-down');
							}
						});
						// 二级内容
						$(menuId + " .accordion-body a").click(function(){
							$(menuId + " li").removeClass("active");
							$(menuId + " li i").removeClass("icon-white");
							$(this).parent().addClass("active");
							
						});
						// 展现三级
						$(menuId + " .accordion-inner a").click(function(){
							var href = $(this).attr("data-href");
							if($(href).length > 0){
								$(href).toggle().parent().toggle();
								return false;
							}
							// <c:if test="${tabmode eq '1'}"> 打开显示页签
							return addTab($(this)); // </c:if>
						});
						// 默认选中第一个菜单
						$(menuId + " .accordion-body a:first i").click();
						$(menuId + " .accordion-body li:first li:first a:first i").click();
					});
				}
				// 大小宽度调整
				wSizeWidth();
				return false;
			});
			// 初始化点击第一个一级菜单
			$("#menu a.menu:first span").click();
			// <c:if test="${tabmode eq '1'}"> 下拉菜单以选项卡方式打开
			$("#userInfo a").mouseup(function(){
				return addTab($(this), true);
			});// </c:if>
			// 鼠标移动到边界自动弹出左侧菜单
			$("#openClose").mouseover(function(){
				if($(this).hasClass("open")){
					$(this).click();
				}
			});
			// 获取通知数目  <c:set var="oaNotifyRemindInterval" value="${fns:getConfig('oa.notify.remind.interval')}"/>
			function getNotifyNum(){
				$.get("${ctx}/oa/oaNotify/self/count?updateSession=0&t="+new Date().getTime(),function(data){
					var num = parseFloat(data);
					if (num > 0){
						$("#notifyNum,#notifyNum2").show().html("("+num+")");
					}else{
						$("#notifyNum,#notifyNum2").hide()
					}
				});
			}
			getNotifyNum(); //<c:if test="${oaNotifyRemindInterval ne '' && oaNotifyRemindInterval ne '0'}">
			setInterval(getNotifyNum, ${oaNotifyRemindInterval}); //</c:if>
		});
		// <c:if test="${tabmode eq '1'}"> 添加一个页签
		function addTab($this, refresh){
			$(".jericho_tab").show();
			$("#mainFrame").hide();
			$.fn.jerichoTab.addTab({
                tabFirer: $this,
                title: $this.text(),
                closeable: true,
                data: {
                    dataType: 'iframe',
                    dataLink: $this.attr('href')
                }
            }).loadData(refresh);
			$('#jerichotab .tab_pages>div.tabs>ul>li>div.tab_left>div.tab_close>a').html('<i class="fa fa-remove"></i>');
			$('#jerichotab .tab_pages>div.tabs>ul>li>div.tab_left').css('width','100px');
			$('#jerichotab .tab_pages>div.tabs>ul>li>div.tab_left>div.tab_text').css('width','87px');
			return false;
		}// </c:if>
	</script>
</head>
<body>
	<div id="main">
		<div id="header" class="navbar navbar-fixed-top">
			<div class="navbar-inner">
				<!--平台名称  -->
				<div class="brand"><img src="${ctxStatic}/images/LOGO.png"  /></div>
				<ul id="userControl" class="nav pull-right" >
					<li><a href="${pageContext.request.contextPath}${fns:getFrontPath()}/index-${fnc:getCurrentSiteId()}.html" target="mainFrame" title="访问网站主页"><div class="nav_right">首页</div></a></li>
					<li><a title="个人信息"><div class="nav_left">您好，${fns:getUser().name}</div></a></li>
					<li><a href="${ctx}/out" title="退出登录"><div class="nav_left">退出</div></a></li>
					<li id="userInfo"><a href="${ctx}/sys/user/modifyPwd" target="mainFrame"><div class="nav_left">修改密码</div></a></li>
					<li>&nbsp;</li>
				</ul>
				<!-- 分割线 -->
			
				<!-- 动态显示时间  格式YYYY-MM-DD HH:mm:ss 星期 XX -->
				<div class="nav" >
					<span style="white-space: pre"> </span>
					<td width="18%"><font color="#FFFFFF">
					<div id="time1">
					</div>
					</font></td>
				</div>
				
				<div class="nav-collapse" style="display: none;">
					<ul id="menu" class="nav" style="*white-space:nowrap;float:none;">
						<c:set var="firstMenu" value="true"/>
						<c:forEach items="${fns:getMenuList()}" var="menu" varStatus="idxStatus">
							<c:if test="${menu.parent.id eq '1'&&menu.isShow eq '1'}">
								<li class="menu ${not empty firstMenu && firstMenu ? ' active' : ''}">
									<c:if test="${empty menu.href}">
										<a class="menu" href="javascript:" data-href="${ctx}/sys/menu/tree?parentId=${menu.id}" data-id="${menu.id}"><span>${menu.name}</span></a>
									</c:if>
									<c:if test="${not empty menu.href}">
										<a class="menu" href="${fn:indexOf(menu.href, '://') eq -1 ? ctx : ''}${menu.href}" data-id="${menu.id}" target="mainFrame"><span>${menu.name}</span></a>
									</c:if>
								</li>
								<c:if test="${firstMenu}">
									<c:set var="firstMenuId" value="${menu.id}"/>
								</c:if>
								<c:set var="firstMenu" value="false"/>
							</c:if>
						</c:forEach>
					</ul>
				</div><!--/.nav-collapse -->
			</div>
	    </div>
	    <div class="container-fluid">
			<div id="content" class="row-fluid">
				<div id="left"><%-- 
					<iframe id="menuFrame" name="menuFrame" src="" style="overflow:visible;" scrolling="yes" frameborder="no" width="100%" height="650"></iframe> --%>
				</div>
				<div id="openClose" class="close">&nbsp;</div>
				<div id="right">
					<iframe id="mainFrame" name="mainFrame" src="" style="overflow:visible;" scrolling="yes" frameborder="no" width="100%" height="650"></iframe>
				</div>
			</div>
		    <div id="footer" class="row-fluid">
	            <%-- Copyright &copy; 2012-${fns:getConfig('copyrightYear')} ${fns:getConfig('productName')} - Powered By <a href="http://its111.com" target="_blank">Its111</a> ${fns:getConfig('version')}--%>
	            ${fns:getConfig('productName')} - Powered By <a href="#">广州普及网络科技有限公司北京分公司</a> 
			</div>
		</div>
	</div>
	<script type="text/javascript">  
       //动态显示  
       setInterval("document.getElementById('time1').innerHTML=new Date().toLocaleString();",1000);  
     </script>
	<script type="text/javascript"> 
		var leftWidth = 160; // 左侧窗口大小
		var tabTitleHeight = 33; // 页签的高度
		var htmlObj = $("html"), mainObj = $("#main");
		var headerObj = $("#header"), footerObj = $("#footer");
		var frameObj = $("#left, #openClose, #right, #right iframe");
		function wSize(){
			var minHeight = 500, minWidth = 980;
			var strs = getWindowSize().toString().split(",");
			htmlObj.css({"overflow-x":strs[1] < minWidth ? "auto" : "hidden", "overflow-y":strs[0] < minHeight ? "auto" : "hidden"});
			mainObj.css("width",strs[1] < minWidth ? minWidth - 10 : "auto");
			frameObj.height((strs[0] < minHeight ? minHeight : strs[0]) - headerObj.height() - footerObj.height() - (strs[1] < minWidth ? 42 : 28));
			$("#openClose").height($("#openClose").height() - 5);// <c:if test="${tabmode eq '1'}"> 
			$(".jericho_tab iframe").height($("#right").height() - tabTitleHeight); // </c:if>
			wSizeWidth();
		}
		function wSizeWidth(){
			if (!$("#openClose").is(":hidden")){
				var leftWidth = ($("#left").width() < 0 ? 0 : $("#left").width());
				$("#right").width($("#content").width()- leftWidth - $("#openClose").width() -5);
			}else{
				$("#right").width("100%");
			}
		}// <c:if test="${tabmode eq '1'}"> 
		function openCloseClickCallBack(b){
			$.fn.jerichoTab.resize();
		} // </c:if>
	</script>
	<script src="${ctxStatic}/common/wsize.min.js" type="text/javascript"></script>
</body>
</html>