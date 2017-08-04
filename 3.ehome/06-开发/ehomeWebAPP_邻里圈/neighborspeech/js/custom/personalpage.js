var vm = new Vue({
	el:"#app",
	data:{
		personalpage:{},
		releaseDetails:[],
		praiseInformation:"",
		followInformation:""
	},
	mounted:function(){//页面加载之后自动调用，常用于页面渲染
		this.$nextTick(function(){//在2.0版本中，加mounted的$nextTick方法，才能使用vm
			this.cartView();
		});
	},
	methods:{
		// 渲染页面
		cartView:function(){
			var _this = this;
			this.$http.get("../../data/personalpage.json").then(function(res){
				_this.personalpage = res.data;
                _this.releaseDetails=_this.personalpage.data
				console.log(_this.releasePictures);
			});
		},
		//赞一个
		praiseOne:function(releaseDetail,type){
			var _this = this;
			if(releaseDetail.isPraise==1){//取消点赞
				releaseDetail.isPraise=0;
				releaseDetail.countPraise-=1;
//				 this.$http.post("/speak/saveComment"{
//				 	id:releaseDetail.id,
//				 	type:type
//				 }).then(function(res){
//						_this.praiseInformation = res.data.message;		
//					});
			}else{//点赞
				releaseDetail.isPraise=1;
				releaseDetail.countPraise+=1;
//				this.$http.post("/speak/saveComment"{
//				 	id:releaseDetail.id,
//				 	type:type
//				 }).then(function(res){
//						_this.praiseInformation = res.data.message;		
//					});
			}
	},
	follow:function(personalpage){
			var _this = this;
		if(personalpage.isFocus==1){//取消关注
			personalpage.isFocus=0;
//			this.$http.post("/speak/saveComment"{
//				 	userId:,
//					subUserId:personalpage.id,
//				 	type:0
//				 }).then(function(res){
//						_this.followInformation = res.data.message;		
//					});
			
		}else{
			personalpage.isFocus=1;
		//			this.$http.post("/speak/saveComment"{
//				 	userId:,
//					subUserId:personalpage.id,
//				 	type:1
//				 }).then(function(res){
//						_this.followInformation = res.data.message;		
//					});
		}
	}
		
	}
});