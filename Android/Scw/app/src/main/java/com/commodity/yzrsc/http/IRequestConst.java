package com.commodity.yzrsc.http;

/**
 * 请求的基本数据
 */
public class IRequestConst {
    public static final String REQUEST_URL = "https://yzrsc-api.83soft.cn/";//测试环境2 scw-web.83soft.cn
    public static final String REQUEST_URLS = "https://yzrsc-api.83soft.cn:443/";//测试环境动态
    //public static final String REQUEST_URL = "http://api.soucangwang.com/";//正式环境

    // 请求类别
    public static interface RequestMethod {
        // 版本升级
        public static final String MobileUpdate = REQUEST_URL + "system/MobileUpdate";
        // 测试接口
        public static final String SuccessWithData = REQUEST_URL + "Test/SuccessWithPagedData";
        // 获取短信接口
        public static final String GetVaildCode = REQUEST_URL + "Member/GetVaildCode";
        // 获取短信接口 更改账户密码获取手机验证码
        public static final String GetVaildCodeRegisted = REQUEST_URL + "Member/GetVaildCodeRegisted";
        public static final String ChangeInfoNewMobileGetVaild = REQUEST_URL + "/Member/ChangeInfoNewMobileGetVaild";
        //修改手机号获取短息
        public static final String ChangeInfoMobileGetVaild = REQUEST_URL + "Member/ChangeInfoMobileGetVaild";
        //修改手机号
        public static final String ChangeInfoMobile = REQUEST_URL + "Member/ChangeInfoNewMobileSubmit";
        public static final String ChangeInfoNewMobileSubmit = REQUEST_URL + "Member/ChangeInfoNewMobileSubmit";
        // 注册接口
        public static final String Register = REQUEST_URL + "Member/Register";
        // 忘记密码
        public static final String ChangePassword = REQUEST_URL + "Member/ChangePassword";
        // 个人信息-修改密码
        public static final String ChangeInfoPassword = REQUEST_URL + "Member/ChangeInfoPassword";
        //静态界面
        public static final String GetStaticPageUrl = REQUEST_URL + "Common/GetStaticPageUrl";
        //登录接口
        public static final String Login = REQUEST_URL + "Member/Login";
        // 店铺信息
        public static final String StoreInfo = REQUEST_URL + "Shop/GetShopInfo";
        public static final String EditShopInfo = REQUEST_URL + "Shop/EditShopInfo";
        //订单超时时间
        public static final String GetOrderExpireTime = REQUEST_URL + "MemberOrder/GetOrderExpireTime";
        //开店认证
        public static final String ShopAuthentication = REQUEST_URL + "Shop/ShopAuthentication";
        //头像上传
        public static final String UploadAvatar = REQUEST_URL + "Shop/UploadAvatar";
        //修改店铺名称
        public static final String EditShopName = REQUEST_URL + "Shop/EditShopName";
        // 获取在售商品列表(分类商品
        public static final String GetGoodsSaleList = REQUEST_URL + "Goods/GetGoodsSaleList";
        //添加商品
        public static final String AddShopGoods = REQUEST_URL + "Shop/AddShopGoods";
        //添加商品
        public static final String AddGoods = REQUEST_URL + "SellerGoods/AddGoods";
        //获取转售商品
        public static final String GetMyResellGoods = REQUEST_URL + "SellerGoods/GetMyResellGoods";
        //设置商品已经售罄
        public static final String SetGoodsSoldout = REQUEST_URL + "SellerGoods/SetGoodsSoldout";
        //在售
        public static final String SetGoodsOnSale = REQUEST_URL + "SellerGoods/SetGoodsOnSale";
        //我的上传
        public static final String GetMyGoods = REQUEST_URL + "SellerGoods/GetMyGoods";
        //我的下架
        public static final String GetMyOffShelvesGoods = REQUEST_URL + "SellerGoods/GetMyOffShelvesGoods";
        //删除只有商品
        public static final String DeleteGoodsSaleInfo = REQUEST_URL + "SellerGoods/DeleteGoodsSaleInfo";
        //分类
        public static final String GetGoodsKindList = REQUEST_URL + "Goods/GetGoodsKindList";
        //商品列表
//        public static final String GetGoodsSaleList = REQUEST_URL + "Goods/GetGoodsSaleList";
        //获取订单列表
        public static final String BuyGetOrderList = REQUEST_URL + "MemberOrder/GetOrderList";
        public static final String BuyGetOrderDetail = REQUEST_URL + "MemberOrder/GetOrderDetail";
        //留言列表
        public static final String GetOrderArbitrationMessageList = REQUEST_URL + "MemberOrder/GetOrderArbitrationMessageList";
        //卖家订单
        public static final String GetOrderList = REQUEST_URL + "SellerOrder/GetOrderList";
        //确认收货
        public static final String ConfirmOrderReceived = REQUEST_URL + "MemberOrder/ConfirmOrderReceived";
        //卖家订单详情
        public static final String GetSoldOrderDetail = REQUEST_URL + "SellerOrder/GetSoldOrderDetail";
        //卖家发货
        public static final String DeliverAnOrder = REQUEST_URL + "SellerOrder/DeliverAnOrder";
        //转售订单
        public static final String GetResellOrderList = REQUEST_URL + "SellerOrder/GetResellOrderList";
        //添加商品评价
        public static final String AddGoodsEvaluation = REQUEST_URL + "Goods/AddGoodsEvaluation";
        //更新转售商品
        public static final String UpdateResellGoods = REQUEST_URL + "SellerGoods/UpdateResellGoods";
        //申请退款
        public static final String AskForOrderRefundment = REQUEST_URL + "MemberOrder/AskForOrderRefundment";
        //转售详情
        public static final String GetResellOrderDetail = REQUEST_URL + "SellerOrder/GetResellOrderDetail";
        //钱包统计信息
        public static final String GetWalletAmount=REQUEST_URL+"MyWallet/GetWalletAmount";
        //银行卡列表
        public static final String GetBankCardList=REQUEST_URL+"MyWallet/GetBankCardList";
        //添加银行卡
        public static final String AddBankCard=REQUEST_URL+"MyWallet/AddBankCard";
        //申请提现
        public static final String ApplyForWithdraw=REQUEST_URL+"MyWallet/ApplyForWithdraw";
        public static final String GetWithdrawDetail=REQUEST_URL+"MyWallet/GetWithdrawDetail";
        //提现日志
        public static final String GetWithdrawLog=REQUEST_URL+"MyWallet/GetWithdrawLog";
        //资金事务记录
        public static final String GetTransactionLog=REQUEST_URL+"MyWallet/GetTransactionLog";
        //签名
        public static final String OnlinePayOrderSimple=REQUEST_URL+"MemberOrder/OnlinePayOrderSimple";//
        public static final String OnlinePayOrder=REQUEST_URL+"MemberOrder/OnlinePayOrder";
        //确认订单支付
        public static final String IsOrderPaid=REQUEST_URL+"MemberOrder/IsOrderPaid";
        //商品详情
        public static final String GetGoodsDetail = REQUEST_URL + "Goods/GetGoodsDetail";
        //店铺搜索
        public static final String SearchShopGoodsSale = REQUEST_URL + "Goods/SearchShopGoodsSale";
        public static final String mSearchGoodsSale = REQUEST_URL + "SellerGoods/SearchGoodsSale";
        //查看原宝贝
        public static final String GetParentGoodsSaleDetail = REQUEST_URL + "SellerGoods/GetParentGoodsSaleDetail";
        //修改上传商品
        public static final String UpdateMyGoods = REQUEST_URL + "SellerGoods/UpdateMyGoods";
        //添加或者删除用户收藏商品(存在则删除)
        public static final String CancelFavorGoods = REQUEST_URL + "Member/CancelFavorGoods";
        //获取用户收货地址列表
        public static final String GetAddressInfoList = REQUEST_URL + "Member/GetAddressInfoList";
        //添加或编辑用户收货地址(Id=0则为添加)
        public static final String AddAddressInfo = REQUEST_URL + "Member/AddAddressInfo";
        //删除用户收货地址
        public static final String CancelAddressInfos = REQUEST_URL + "Member/CancelAddressInfos";
        // 确认订单
        public static final String OrderAnOrder = REQUEST_URL + "MemberOrder/OrderAnOrder";
        //获取物流
        public static final String GetOrderLogisticInfo_MemberOrder = REQUEST_URL + "MemberOrder/GetOrderLogisticInfo";
        //查看订单物流信息
        public static final String GetOrderLogisticInfo_SellerOrder = REQUEST_URL + "SellerOrder/GetOrderLogisticInfo";
        //获取物流公司名称
        public static final String GetLogisticsList = REQUEST_URL + "Common/GetLogisticsList";
        //获取退回内容
        public static final String GetOrderRefundmentInfo = REQUEST_URL + "SellerOrder/GetOrderRefundmentInfo";
        //确认退款
        public static final String AgreeRefundOrder = REQUEST_URL + "SellerOrder/AgreeRefundOrder";
        //拒绝退款
        public static final String DisagreeRefundOrder = REQUEST_URL + "SellerOrder/DisagreeRefundOrder";
        //卖家留言
        public static final String LeaveArbitrationMessage = REQUEST_URL + "SellerOrder/LeaveArbitrationMessage";
        //买家留言
        public static final String MemberorderLeaveArbitrationMessage = REQUEST_URL + "MemberOrder/LeaveArbitrationMessage";
        // 获取用户收藏
        public static final String GetMyFavorGoods = REQUEST_URL + "Member/GetMyFavorGoods";
        // 未读通知数量
        public static final String GetUnreadNotificationCounts = REQUEST_URL + "Notification/GetUnreadNotificationCounts";
        // 获取使用帮助信息
        public static final String GetGuides = REQUEST_URL + "Guide/GetGuides";
        // 添加购物车商品
        public static final String AddCart = REQUEST_URL + "ShoppingCart/AddShoppingCart";
        // 删除购物车商品
        public static final String DeleteCart = REQUEST_URL + "ShoppingCart/RemoveShoppingCarts";
        // 修改购物车商品数量
        public static final String ChangeCartQuantity = REQUEST_URL + "ShoppingCart/ChangeGoodsQuantity";
        // 购物车商品列表
        public static final String GetCartList = REQUEST_URL + "ShoppingCart/GetShoppingCarts";
        public static final String OrderCartList = REQUEST_URL + "ShoppingCart/ToplaceByShoppingCart";
        // 添加联系人
        public static final String AddContact = REQUEST_URL + "MemberContact/AddContact";
        // 删除联系人
        public static final String DeleteContact = REQUEST_URL + "MemberContact/DeleteContact";
        // 联系人列表
        public static final String GetContactList = REQUEST_URL + "MemberContact/GetContactList";
        // 获取最近联系人列表
        public static final String GetRecentContactList = REQUEST_URL + "MemberContact/GetRecentContactList";
        // 第三方登录
        public static final String LoginOpen = REQUEST_URL + "Member/LoginOpen";
        // 第三方登录获取手机验证码
        public static final String GetVaildCodeOpen = REQUEST_URL + "Member/GetVaildCodeOpen";
        // 第三方登录绑定用户
        public static final String BindLoginAccountOpen = REQUEST_URL + "Member/BindLoginAccountOpen";
        // 注册后修改个人信息
        public static final String RegisterInfo = REQUEST_URL + "Member/RegisterInfo";
        // 获取首页商品列表
        public static final String GetMainGoodsSaleList = REQUEST_URL + "Goods/GetMainGoodsSaleList";
        // 转售商品
        public static final String ResellGoods = REQUEST_URL + "SellerGoods/ResellGoods";
        // 账号是否已注册
        public static final String MobieIsRegister = REQUEST_URL + "Member/MobieIsRegister";
        // 获取系统消息列表
        public static final String GetSystemNotificationList = REQUEST_URL + "Notification/GetSystemNotificationList";
        // 获取被评价列表
        public static final String GetEvaluationNotificationList = REQUEST_URL + "Notification/GetEvaluationNotificationList";
        // 获取被关注通知列表
        public static final String GetFollowNotificationList = REQUEST_URL + "Notification/GetFollowNotificationList";
        // 获取广告数据
        public static final String GetAdvers = REQUEST_URL + "Adver/GetAdvers";
        // 获取商品评价列表
        public static final String GetEvaluationList = REQUEST_URL + "Goods/GetEvaluationList";
        // 关于易州人商城静态页面
        public static final String about_staticPage = "http://scw-web.83soft.cn/web/staticPage/about";
        // 用户协议易州人商城静态页面
        public static final String license_staticPage = "http://scw-web.83soft.cn/web/staticPage/license";
        // 读消息
        public static final String ReadNotification = REQUEST_URL + "Notification/ReadNotification";
        // 通过Id列表获取联系人信息
        public static final String GetContactInfo = REQUEST_URL + "MemberContact/GetContactInfo";
        // 搜索商品
        public static final String SearchGoodsSale = REQUEST_URL + "Goods/SearchGoodsSale";
        // 获取水印图片
        public static final String GetWaterMarkImages = REQUEST_URL + "Common/GetWaterMarkImages";

        //动态分类
        public static final String GetDynamicCatalog  = REQUEST_URLS  +"UserDynamic/GetDynamicCatalog";
        //动态列表
        public static final String GetDynamicList  = REQUEST_URLS  +"UserDynamic/GetDynamicInfoList2";
        //动态列表
        public static final String GetDynamicList1  = REQUEST_URLS  +"UserDynamic/GetDynamicInfoList";
        //动态上传
        public static final String PostDynamic  = REQUEST_URLS  +"UserDynamic/UploadDynamic";
        //动态点赞
        public static final String PostDynamicLike  = REQUEST_URLS  +"UserDynamic/DynamicLikeOrNot";
        //动态添加评论
        public static final String PostDynamicEvaluate  = REQUEST_URLS  +"UserDynamic/AddComment";
        //点赞列表
        public static final String PostDynamZanList  = REQUEST_URLS  +"UserDynamic/GetDynamicLikeList";
        //评论列表
        public static final String PostDynamEvalutionList  = REQUEST_URLS  +"UserDynamic/GetDynamicCommentList";
        //删除动态
        public static final String PostDynamDelete  = REQUEST_URLS  +"UserDynamic/DeleteDynamic";
        //修改个人信息
        public static final String PostChangeInfo  = REQUEST_URLS  +"Member/ModifyMemberInfo";
        //获取个人信息
        public static final String PostGetnfo  = REQUEST_URLS  +"Member/GetMemberInfo";
        //删除自己的评论
        public static final String PostDetect  = REQUEST_URLS  +"UserDynamic/DeleteComment";
        // 删除商品
        public static final String POSTDELETESHOP= "https://yzrsc-api.83soft.cn/ShoppingCart/RemoveShoppingCarts";
        public static final String POSTREDUCESHOP= "https://yzrsc-api.83soft.cn/ShoppingCart/ChangeGoodsQuantity";
        public static final String POSTORDER= "https://yzrsc-api.83soft.cn/ShoppingCart/ToplaceByShoppingCart";
        public static final String POSTPAY= "https://yzrsc-api.83soft.cn/MemberOrder/OnlinePayNOrders";
        public static final String POSTPAYWEIXIN= "https://yzrsc-api.83soft.cn/MemberOrder/OnlinePayNOrdersSimple";
        public static final String POSTWALLORDER= "https://yzrsc-api.83soft.cn/MyWallet/Recharge";
        public static final String POSTWALLORDERPAY= "https://yzrsc-api.83soft.cn/MyWallet/WallentRechargeOrderOnlinePay";
        public static final String POSTISPAY= "https://yzrsc-api.83soft.cn/MyWallet/IsWalletRechargePaid";//钱包的
        public static final String POSTISPAYMoney= "https://yzrsc-api.83soft.cn/MyWallet/Consume";//钱包的扣款
    }
}