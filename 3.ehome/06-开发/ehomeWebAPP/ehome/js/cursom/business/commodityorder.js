var vm = new Vue({
    el: "#app",
    data: {
        urlList: {
            list: "commoditylist.html?id=",
            groupbuy: "../groupbuy/groupbuydetail.html?id=",
            phoneicon: "../../images/telphone.png",
            goicon: "../../images/grey_go.png",
            checkicon: "../../images/02.png",
            checkedicon: "../../images/01.png"
        },
        order: {},
        addressList: [],
        timePeriod: [],
        leaveMessage:"",
        deliveryDate: {
            label: "",
            date: "",
            desc: "",
            isImmediate: 1,
            start: "",
            end: ""
        },
        couponDiscounted: 0,
        couponID:"",
        isOK:true,
        num:0
    },
    mounted: function () { //页面加载之后自动调用，常用于页面渲染
        this.$nextTick(function () { //在2.0版本中，加mounted的$nextTick方法，才能使用vm
              var _this = this;

            var orderData = {
                userID: userInfo.userID,
                buildingID: userInfo.buildingID,
                businessID: getQueryString("id")
            };

            _this.getData(_this, "/live/confirmBusinessOrder", orderData, function (resData) {
                resData.coupons.forEach(function (coupon, index) {
                    coupon.isSelected = 0;
                });

                _this.order = resData;
                _this.init(  _this.order,_this.order.coupons);

                if (resData.deliveryDate && resData.deliveryDate.length > 0) {
                    var delivery = resData.deliveryDate[0];
                    var delivery2 = resData.deliveryDate[1];
                    _this.deliveryDate.date = delivery.date;
                    _this.deliveryDate.date = delivery2.date;
                    if (delivery.timePeriod && delivery.timePeriod.length > 0) {
                        _this.deliveryDate.isImmediate = delivery.timePeriod[0].isImmediate;
                        _this.deliveryDate.start = delivery.timePeriod[0].start;
                        _this.deliveryDate.end = delivery.timePeriod[0].end;

                        if (delivery.timePeriod[0].isImmediate == 1) {
                            _this.deliveryDate.label = "立即配送（预计" + delivery.timePeriod[0].end + "送达）";
                        } else {
                            _this.deliveryDate.label = "预计" + delivery.desc + " " + delivery.timePeriod[0].start + '~' + delivery.timePeriod[0].end + "送达";
                        }
                    } else {
                        _this.deliveryDate.isImmediate = delivery2.timePeriod[0].isImmediate;
                        _this.deliveryDate.start = delivery2.timePeriod[0].start;
                        _this.deliveryDate.end = delivery2.timePeriod[0].end;

                        if (delivery2.timePeriod[0].isImmediate == 1) {
                            _this.deliveryDate.label = "立即配送（预计" + delivery2.desc + delivery2.timePeriod[0].end + "送达）";
                        } else {
                            _this.deliveryDate.label = "预计" + delivery2.desc + " " + delivery2.timePeriod[0].start + '~' + delivery2.timePeriod[0].end + "送达";
                        }
                    }
                }
                _this.timePeriod = resData.deliveryDate[0].timePeriod;
            });

            var data = {
                userID: userInfo.userID,
                buildingID: userInfo.buildingID
            };

            _this.getData(_this, "/live/choiceAddress", data, function (resData) {
                resData.forEach(function (address, index) {
                    if (address.address == _this.order.contactAddress) {
                        address.isSelected = 1;
                    }
                    else {
                        address.isSelected = 0;
                    }
                });

                _this.addressList = resData;
            });
        })
    },
    methods: {
        // 渲染页面
        init:function(item,coup){
        	var arr=new Arrey();
        	coup.foreach(function(e,i){
        		if(item.totalMoney >= e.couponCondition){
        		arr.push(e);
        	}
})
        	if(arr.length>0){
        		this.isOK=false;
        		this.num=arr.length;
        	}
        	else{
        		this.isOK=true;
        	}
        	
        	
        },
        changeAddress: function (currentAddress) {
            this.addressList.forEach(function (address, index) {
                address.isSelected = 0;
            });

            currentAddress.isSelected = 1;

            this.order.contactPerson = currentAddress.contactPerson;
            this.order.contactPhone = currentAddress.contactPhone;
            this.order.contactAddress = currentAddress.address;

            $("#myModal_address").modal('toggle');
        },
        changeDelivery: function (delivery) {
            $("#timeModal .business_mainmenu2 li.selected").removeClass("selected");
            $(event.target).closest("li").addClass("selected");
            this.timePeriod = delivery.timePeriod;
            this.deliveryDate.date = delivery.date;
            this.deliveryDate.desc = delivery.desc;
        },
        timeSelected: function (period) {
            this.deliveryDate.isImmediate = period.isImmediate;
            this.deliveryDate.start = period.start;
            this.deliveryDate.end = period.end;
            if (period.isImmediate == 1) {
                this.deliveryDate.label = "立即配送（预计" + period.end + "送达）";
            } else {
                this.deliveryDate.label = "预计" + this.deliveryDate.desc + " " + period.start + '~' + period.end + "送达";
            }

            $("#timeModal").modal('toggle');
        },
        changeCoupon: function (coupon) {
            this.order.coupons.forEach(function (coupon, index) {
                coupon.isSelected = 0;
                
            });

            if (coupon) {
                coupon.isSelected = 1;
                this.couponID=coupon.couponID;
                if (coupon.couponType == 0) {
                    this.couponDiscounted = coupon.couponMoney;
                }
                else {
                this.couponDiscounted = this.order.totalMoney * (100 - coupon.couponMoney) / 100;
                	if( this.couponDiscounted > coupon.couponCap){
                		this.couponDiscounted =coupon.couponCap;
                	}
                	
                    
                }
            }
            else {
                this.couponDiscounted = 0;
            }

            $("#couponModal").modal('toggle');
        },
        submitOrder: function () {
            var _this = this;

            var data = {
                userID: userInfo.userID,
                buildingID: userInfo.buildingID,
                businessID: getQueryString("id"),
                contactPerson: _this.order.contactPerson,
                contactPhone: _this.order.contactPhone,
                contactAddress: _this.order.contactAddress,
                isImmediate: 0,
                deliveryStart: _this.deliveryDate.data + _this.deliveryDate.start,
                deliveryEnd: _this.deliveryDate.data + _this.deliveryDate.end,
                leaveMessage: _this.leaveMessage,
                couponID:_this.couponID
            };

            _this.postData(_this, "/live/submitBusinessOrder", data, function (resData) {
            });
        }
    }
})