var vm = new Vue({
	el: "#app",
	data: {
		urlList: {
			siteindex: "siteindex.html?id=",
			siteorder: "siteorder.html?id="
		},
		siteReservation: [],
		place: {},
		scaleId: "",
		datesweek: "",
		times: 0, //用于判断是否是第一次
		fullAppointment: 0, //用于判断场地是否已被全部预约
		submitUrl: "" //用于拼接场地id
	
	},
	mounted: function() { //页面加载之后自动调用，常用于页面渲染
		this.$nextTick(function() { //在2.0版本中，加mounted的$nextTick方法，才能使用vm
			var _this = this;
			var data = {
				userID: userInfo.userID,
				businessID: getQueryString("id"),
				buildingID: userInfo.buildingID,
				moduleID: getQueryString("mid")
			};
			this.getData(_this,"/live/getSites",data,function(resData) {
				_this.place = resData;
				_this.scaleId = _this.place.sites[0].siteID;
				_this.datesweek = _this.place.reservationDate[0].date;
				var iteReservationData = {
					userID: userInfo.userID,
					businessID: getQueryString("id"),
					siteID: _this.place.sites[0].siteID,
					date: _this.place.reservationDate[0].date
				};			
				_this.getData(_this,"/live/getSiteReservation",iteReservationData, function(resData2){
					var test = resData2;
					test.forEach(function(obj, index) {
						obj.isactive = 0;
						if (obj.isBooked == 1) {
							_this.fullAppointment += 1;
						}
					})
					_this.siteReservation = test;
					if (_this.siteReservation.length == _this.fullAppointment) {
						$("#appointment").addClass("ser_zbtn2");
						$("#appointment").text("预约已满");
					}
					_this.times = 0;
					_this.fullAppointment = 0;
				})
			});
		})
	},
	methods: {
		//预约时段
		subscribe: function(time, index) {
			var _this = this;
			if (time.isactive == 1) { //要取消预约
				if ($(event.target).closest("li").prev().hasClass("active") && $(event.target).closest("li").next().hasClass("active")) {
					layer.open({
						content: '请选择连续时间段',
						skin: 'msg',
						time: 2 //2秒后自动关闭
					});
				} else {
					time.isactive = 0;
					_this.times -= 1;
				}
			} else { //要预约
				if (_this.times == 0) { //第一次点
					if (time.isBooked == 1) { //第一点的为被预约的
						layer.open({
							content: '已被预约',
							skin: 'msg',
							time: 2 //2秒后自动关闭
						});
					} else {
						_this.times += 1;
						time.isactive = 1;
					}
				} else { //偶数次点
					if (time.isBooked == 1) { //已被预约是
						layer.open({
							content: '已被预约',
							skin: 'msg',
							time: 2 //2秒后自动关闭
						});
					} else if ($(event.target).closest("li").prev().hasClass("active") || $(event.target).closest("li").next().hasClass("active")) {
						time.isactive = 1;
						_this.times += 1;
					} else {
						layer.open({
							content: '请选择连续时间段',
							skin: 'msg',
							time: 2 //2秒后自动关闭
						});
					}
				}
			}
		},
		//选择包间
		changeCategory: function(scale, index) {
			var _this = this;
			$(event.target).parent("li").addClass("selected");
			$(event.target).parent("li").siblings('li').removeClass("selected")
			_this.scaleId = scale.siteID;
			vm.toggleData(_this.scaleId, _this.datesweek);
		},
		//选择日期
		changeDate: function(dates, index) {
			var _this = this;
			$(event.target).addClass("active");
			$(event.target).parent("li").siblings('li').children().removeClass("active")
			_this.datesweek = _this.place.reservationDate[index].date;
			vm.toggleData(_this.scaleId, _this.datesweek);
		},
		//选择包间和时间 时切换数据
		toggleData: function(siteParameter, dateParameter) {
			var _this = this;

			var path = interfaceUrl + "/live/getSiteReservation";
			this.$http.get(path, {
				userID: userInfo.userID,
				businessID: getQueryString("id"),
				siteID: siteParameter,
				date: dateParameter
			}).then(function(res) {
				$("#ulTimes li").removeClass("active");
				var test = res.data.data;
				test.forEach(function(obj, index) {
					obj.isactive = 0;
					if (obj.isBooked == 1) {
						_this.fullAppointment += 1;
					}
				})
				_this.siteReservation = test;
				if (_this.siteReservation.length == _this.fullAppointment) {
					$("#appointment").addClass("ser_zbtn2");
					$("#appointment").text("预约已满");
				}
				_this.times = 0;
				_this.fullAppointment = 0;
			});
		},
		//收藏商家
		collection: function(item) {
			var _this = this;
			_this.myCollection(_this, item);
		},
		//提交预约
		Submit: function(siteReservation) {
			var _this = this;
			var idaggregate = [];
			siteReservation.forEach(function(obj, index) {
				if (obj.isactive == 1) {
					idaggregate.push(obj.siteReservationID);
				}
			});
			//			console.log(idaggregate)
			if (idaggregate.length == 0) {
				layer.open({
					content: '请选择预约时段',
					skin: 'msg',
					time: 2 //2秒后自动关闭
				});
			} else {
				_this.submitUrl = idaggregate.join()
			}
		}

	}
});
Vue.filter("separate", function(value) {
	var week = value.split("-");
	var month = week[1];
	var day = week[2];
	var f = month + "/" + day
	return f;
});