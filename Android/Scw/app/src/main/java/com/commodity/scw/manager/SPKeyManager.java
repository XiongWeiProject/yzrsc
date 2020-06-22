package com.commodity.scw.manager;

import android.widget.TextView;

import com.commodity.scw.model.PicInfo;
import com.commodity.scw.model.mine.DetailMyOrdeEntity;
import com.commodity.scw.ui.activity.general.PicShowActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 缓存管理者
 *
 * @author liyushen
 */
public class SPKeyManager {
    public static final String USERINFO_id = "USERINFO_id";//ID
    public static final String USERINFO_nickname = "USERINFO_nickname";//昵称 ,
    public static final String USERINFO_mobile = "USERINFO_mobile";//账户(手机) ,
    public static final String USERINFO_promotionCode = "USERINFO_promotionCode";//推广码 ,
    public static final String USERINFO_inviter = "USERINFO_inviter";//邀请人 ,
    public static final String USERINFO_avatar = "USERINFO_avatar";//头像 ,
    public static final String USERINFO_deviceToken = "USERINFO_deviceToken";//令牌
    public static final String USERINFO_expireTime = "USERINFO_expireTime";//失效时间 ,
    public static final String USERINFO_isPermanet = "USERINFO_isPermanet"; //是否永久
    public static final String USERINFO_imToken = "USERINFO_imToken"; //融云token
    public static final String USERINFO_isLogin = "USERINFO_isLogin"; //是否登录
    public static final String USERINFO_pwd = "USERINFO_pwd"; //用户密码
    public static final String UPDATE_URL = "UPDATE_URL";
    public static final String ISNOTFIRSTNAPP = "isNotFirstInApp"; //是否第一次登录APp
    public static TextView ListenTextView ; //监听主activity
    public static List<PicInfo> picInfoList=new ArrayList<>(); //监听主activity
    public static int  pageSize = 10 ; //分页大小
    public static int  delay_time= 100 ; //延迟时间
    public static DateFormat  dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss") ; //时间格式化
    public static DateFormat  dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ; //时间格式化
    public static final String GetGoodsSaleListCacheStr = "GetGoodsSaleListCacheStr"; //无网络缓存
    public static final String GetHomeBanerAdversCacheStr = "GetHomeBanerAdversCacheStr"; //无网络缓存
    public static final String GetGoodsKindListCacheStr = "GetGoodsKindListCacheStr"; //无网络缓存
    public static final String CURACTIVITYNAME = "curactivityname"; //当前activity的名字
    public static final String PHOTO_MODE = "photo_mode";
    public static final String INDEX = "idx";
    public static DetailMyOrdeEntity curDetailMyOrdeEntity = null;
    public static DetailMyOrdeEntity XiaoShouDetailMyOrdeEntity = null;
    public static List<TextView> messageTipTextViewList=new ArrayList<>();
    public static int uploadmax=13;
    public static final String APP_about_URL = "APP_about_URL"; //关于页面
    public static final String APP_license_URL = "APP_license_URL"; //用户协议
    public static final String APP_authorization_URL = "APP_authorization_URL"; //授权协议
}
