var vm = new Vue({
	el: "#app",
	data: {
		criticism: {},
		commentContent: "",
		meanwhileIcon: "../../images/gx_01.png",
		meanwhileIcon2: "../../images/gx_02.png",
		ismeanwhile: 0,
		showPopup: false,
		topic: {
			id: 0,
			name: ""
		},
		atwhos:[],
		speakData:[],
		uploadImageNum:0,
		
		textnum:200,
		
		prompt:"写下您的评论吧…"
		
	},
	mounted: function() { //页面加载之后自动调用，常用于页面渲染
		this.$nextTick(function() { //在2.0版本中，加mounted的$nextTick方法，才能使用vm
			this.cartView();
		});
	},
	methods: {
		// 渲染页面
		cartView: function() {
			var _this = this;
			if(getQueryString("name")!=null){
				_this.prompt= "回复@"+getQueryString("name");
			}

			//			this.$http.get("/speak/toComment"{
			//				speakId:123,
			//			}).then(function(res){
			//				_this.criticism = res.data;
			//			});
		},
		meanwhile: function() {
			var _this = this;
			if(_this.ismeanwhile == 0) {
				_this.ismeanwhile = 1;
			} else {
				_this.ismeanwhile = 0;
			}

		},
		//发送发言
		sendOut: function() {
			var sTest = $('#editable').text();
			var sHtml = $('#editable').html();
			if($.trim(sTest).length < 5) {
				layer.open({
					content: '内容不能少于五个字符',
					skin: 'msg',
					time: 2 //2秒后自动关闭
				});
				return;
			}
			$('#results').html(sHtml);
			$('#results .atlink').each(function(i, e) {
				$(e).attr('href', 'http://www.baidu.com?id=' + $(e).attr('data-atid'));
			})
		},
		//变下面的数字
		calculation:function(){//没加两端去空格    加上有问题
			var _this = this;
			var sTest = $('#editable').text();
			console.log($.trim(sTest).length);
			if(sTest.length <200) {
				_this.textnum=200-sTest.length;
			}else if(sTest.length == 200){
				_this.commentContent = $('#editable').html();
				_this.textnum=0;
			}else{
				$('#editable').html(_this.commentContent)
				layer.open({
					content: '发言超过限制长度',
					skin: 'msg',
					time: 2 //2秒后自动关闭
				});
			}
		},
		//删除选择的图片
		deleteImg:function(image){
			var _this = this;
			image.selected=0;
			image.isactive=0;
			_this.uploadImageNum-=1;
		},
		seltopic: function() {
			$('#popupPage').empty()
			$.get('../operate/partialtopic.html', function(res) {
				$('#popupPage').html(res);
			})
			this.showPopup = true;
		},
		selwho:function(){
			$('#popupPage').empty()
			$.get('../operate/partialcontact.html', function(res) {
				$('#popupPage').html(res);
			})
			this.showPopup = true;
		},
		
		//点击相机跳转的页面
		openUploading:function(){
			$('#popupPage').empty()
			$.get('../operate/partialuploading.html',function(res){
				$('#popupPage').html(res);
			})
			this.showPopup=true;
	},
		appendTopic: function() {
			$('#editable').append('&nbsp;<a href="#" data-atid="' + this.topic.id + '" class="atlink" contentEditable="false">' + this.topic.name + '</a>&nbsp;');
		    vm.calculation();//插入标签后触发input事件
		},
		appendAt:function(){
			this.atwhos.forEach(function(obj,index){
				$('#editable').append('&nbsp;<a href="#" data-atid="' + obj.friendId + '" class="atlink" contentEditable="false">' + obj.friendName + '</a>&nbsp;');
				vm.calculation();//插入标签后触发input事件
			});
			
		}
	}
});