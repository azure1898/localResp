var vm = new Vue({
    el: "#app",
    data: {
        topAd: [],
        topModule: [],
        topBusiness: [],
        groupBuy: [],
        middleBusiness: [],
        footerBusiness: [],
        topic: {},
        topicData: [],
        isClass: true,
        urlList: {
            business: "../business/modulelist.html?mid=",
            course: "../course/modulelist.html?mid=",
            groupbuy: "../groupbuy/modulelist.html?mid=",
            site: "../site/modulelist.html?id=",
            service: "../service/modulelist.html?mid=",
            businessIndex: "../business/businessindex.html?id=",
            courseIndex: "../course/courselist.html?id=",
            siteIndex: "../site/modulelist.html?id=",
            serviceIndex: "../service/servicelist.html?id=",
             businessDetail:"../business/commoditydetail.html?id=",
              courseDetail:"../course/coursedetail.html?id=",
               serviceDetail:"../service/servicedetail.html?id="
        }
    },
    mounted: function () {
        this.$nextTick(function () {
            var _this = this;

            var data = {
                userID: userInfo.userID,
                buildingID: userInfo.buildingID
            };

            this.getData(_this, '/live/getTopAdSlot', data, function (resData) {
                _this.topAd = resData;
                _this.init();
            });

            this.getData(_this, '/live/getTopModule', data, function (resData) {
                _this.topModule = resData;
            });

            this.getData(_this, '/live/getTopBusiness', data, function (resData) {
                _this.topBusiness = resData;
            });

            this.getData(_this, '/live/getGroupBuy', data, function (resData) {
                _this.groupBuy = resData;

            });

            this.getData(_this, '/live/getMiddleBusiness', data, function (resData) {
                _this.middleBusiness = resData;
            });

            this.getData(_this, '/live/getFooterBusiness', data, function (resData) {
                _this.footerBusiness = resData;
            });

            this.getData(_this, '/live/getTopic', data, function (resData) {
                _this.topic = resData;
                //              _this.topicData = _this.topic.topicData;
            });
        });
    },
    updated: function () {
        $('#myCarousel').carousel({
            //自动4秒播放
            interval: 3000,
        });
         $('#myCarousel_gro').carousel({
            //自动4秒播放
            interval: 5000,
        });
        $('#myCarousel_m_b').carousel({
            //自动4秒播放
            interval: 5000,
        });
        var mySwiper = new Swiper('.swiper-container', {
            direction: 'vertical',
            loop: true,
            autoplay: 5000,
            height: 70,
        });
        var mySwiper = new Swiper('.swiper-container_1', {
            direction: 'horizontal',
            loop: true,
            autoplay: 5000,
            height: 90,
            autoplayDisableOnInteraction: false,

        });
//        var myElement= document.getElementById('myCarousel')
//                      var hm=new Hammer(myElement);
//                      hm.on("swipeleft",function(){
//                          $('#myCarousel').carousel('next')
//                      })
//                      hm.on("swiperight",function(){
//                          $('#myCarousel').carousel('prev')
//                      }) 
    },
    methods: {
      topAdClick: function (module) {
                 var _this = this;

            switch (module.productMode) {
                case "0":
                    location.href = _this.urlList.businessIndex + module.businessID;
                    break;              
                case "1":
                    location.href = _this.urlList.serviceIndex + module.businessID;
                    break;
                case "2":
                         location.href = _this.urlList.courseIndex +module.businessID;
                    break;
                case "3":
                         location.href = _this.urlList.siteIndex + module.businessID;
                    break;
            }      

        },
        topAdClick_p:function(module){
        	var _this = this;
        	switch (module.productMode) {
                case "0":
                    location.href = _this.urlList.businessDetail + module.productID+'&bid='+module.businessID;
                    break;              
                case "2":
                    location.href = _this.urlList.courseDetail +  module.productID;
                    break;               
                case "1":
                         location.href = _this.urlList.serviceDetail + module.productID;
                    break;
            }      
        	
        },
        moduleClick: function (module) {
            var _this = this;

            switch (module.moduleID) {
                case "01":
                    location.href = "#";
                    break;              
                case "02":
                    location.href = _this.urlList.business + module.moduleID;
                    break;
                case "03":
                         location.href = _this.urlList.service + module.moduleID;
                    break;
                     case "04":
                    location.href = _this.urlList.course + module.moduleID;
                    break;
            }
        },
        topBusinessClick: function (business) {
            this.businessJump(business);
        },
        middleBusinessClick: function (businessID, mode) {
            var _this = this;

            switch (mode.modeType) {
                case "0":
                    location.href = _this.urlList.business + mode.modeType;
                    break;
                case "1":
                    location.href = _this.urlList.service + mode.modeType;
                    break;
                case "2":
                    location.href = _this.urlList.course + mode.modeType;
                    break;
                case "3":
                    location.href = this.urlList.site + businessID;
                    break;
            }
        },
        footBusinessClick: function (business) {
            this.businessJump(business);
        },
        businessJump: function (business) {
            var _this = this;

            switch (business.modeID) {
                case "0":
                    location.href = _this.urlList.businessIndex + business.businessID;
                    break;
                case "1":
                    location.href = _this.urlList.serviceIndex + business.businessID;
                    break;
                case "2":
                    location.href = _this.urlList.courseIndex + business.businessID;
                    break;
                case "3":
                    location.href = this.urlList.siteIndex + business.businessID;
                    break;
            }
        }
    }
})