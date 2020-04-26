package com.commodity.scw.manager;

import android.app.Application;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.commodity.scw.MainApplication;
import com.commodity.scw.model.UserInfo;
import com.commodity.scw.rongyun.server.utils.RongGenerate;
import com.commodity.scw.utils.RongIMUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import cn.yohoutils.Model.VersionBaseInfo;
import cn.yohoutils.StringUtil;
import io.rong.imkit.RongIM;

/**
 * Function: APP初始化配置
 * <p>
 * Date: 2016-3-30 上午11:55:40
 *
 * @author liyushen
 */
public class ConfigManager {
    /**
     * 保存在手机里面的SP文件名
     */
    public static final String FILE_NAME = "sp_data";
    /**
     * SD卡根目录
     */
    public static String SDCARD_PATH = Environment.getExternalStorageDirectory().toString();
    /**
     * 根目录
     */
    public static final String ROOT = SDCARD_PATH + File.separator + "scw"
            + File.separator;
    /**
     * 数据路径
     */
    public static final String DATA_PATH = ROOT + "Data" + File.separator;

    /**
     * 图片路径
     */
    public static final String IMG_PATH = ROOT + "Img" + File.separator;
    /**
     * video路径
     */
    public static final String VIDEO_PATH = ROOT + "video" + File.separator;
    public static String VIDEO_PATH_SD =  ROOT+"/aascwvideo";
    /**
     * 分享暂存的图片路径
     */
    public static final String DOWNLOADIMAGE = ROOT + "downloadimage";
    /**
     * 下载图片路径
     */
    public static final String DOWNLOAD_PATH = ROOT + "Download"
            + File.separator;

    public static VersionBaseInfo versionInfo;
    /**
     * 极光ID
     */
    public static String mPushID = null;

    /**
     * 保存用户id标识
     */
    public static final String PREF_UID = "uid";
    /**
     * 保存用户名标识
     */
    public static final String PREF_UNAME = "uname";
    /**
     * sessionCode
     */
    @SuppressWarnings("unused")
    private static final String SESSION_CODE = "sessionCode";
    /**
     * 存储基本数据路径
     */
    public static final String CONFIG_PATH = "config.bin";


    private static ConfigManager instance;
    SPManager spManager;

    /**
     * 当前登录用户信息
     */
    private static UserInfo mUserInfo = null;

    /**
     * 在进入应用程序中初始化
     *
     * @param context 上下文
     */
    public synchronized static void create(Application context) {
        if (instance != null) {
            return;
        }
        instance = new ConfigManager(context);
    }

    public static ConfigManager instance() {
        if (instance == null) {
            throw new RuntimeException("ConfigManager not created");
        }
        return instance;
    }

    public ConfigManager(Application context) {
        spManager = SPManager.instance();
        initPreferences();
    }

    /**
     * 重置所有得Preferences回Defaults
     */
    public void resetToDefaults() {
        resetToDefaults(spManager.getDefaultValues());
    }

    public void resetToResets() {
        resetToDefaults(spManager.getResetValues());
    }

    /**
     * 重置相应的key回default
     *
     * @param key
     */
    public void resetToDefault(String key) {
        if (spManager != null) {
            Map<String, Object> defaultValues = spManager.getDefaultValues();
            if (defaultValues != null && defaultValues.containsKey(key)) {
                Object defaultValue = defaultValues.get(key);
                initPreference(key, defaultValue, true);
            }
        }
    }

    /**
     * 获取文件保存路径
     *
     * @param fileName
     * @return
     */
    public static String getFilePath(String fileName) {
        if (StringUtil.isEmpty(fileName)) {
            fileName = "temp.bin";
        }
        String path = DATA_PATH + fileName;
        return path;
    }

    /**
     * 初始化preferences
     */
    private void initPreferences() {
        for (Entry<String, Object> entry : spManager.getDefaultValues().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            initPreference(key, value, false);
        }

        resetToDefaults(spManager.getResetValues());
    }

    /**
     * 初始化preferences
     *
     * @param key
     * @param value
     * @param force
     */
    private void initPreference(String key, Object value, boolean force) {
        if (value instanceof String) {
            initStringPreference(key, (String) value, force);
        } else if (value instanceof Integer) {
            initIntPreference(key, (Integer) value, force);
        } else if (value instanceof Long) {
            initLongPreference(key, (Long) value, force);
        } else if (value instanceof Boolean) {
            initBooleanPreference(key, (Boolean) value, force);
        } else if (value instanceof File) {
            initFilePreference(key, (File) value, force);
        } else if (value instanceof Float) {
            initFloatPreference(key, (Float) value, force);
        }
    }

    /**
     * 初始化String值
     *
     * @param prefKeyName
     * @param defaultValue
     * @param force
     */
    private void initStringPreference(String prefKeyName, String defaultValue,
                                      boolean force) {
        if (!spManager.getSharedPreferences().contains(prefKeyName) || force) {
            spManager.setString(prefKeyName, defaultValue);
        }
    }

    /**
     * 初始化Float值
     *
     * @param prefKeyName
     * @param defaultValue
     * @param force
     */
    private void initFloatPreference(String prefKeyName, Float defaultValue,
                                     boolean force) {
        if (!spManager.getSharedPreferences().contains(prefKeyName) || force) {
            spManager.setFloat(prefKeyName, defaultValue);
        }
    }

    /**
     * 初始化Boolean值
     *
     * @param prefKeyName
     * @param defaultValue
     * @param force
     */
    private void initBooleanPreference(String prefKeyName,
                                       boolean defaultValue, boolean force) {
        if (!spManager.getSharedPreferences().contains(prefKeyName) || force) {
            spManager.setBoolean(prefKeyName, defaultValue);
        }
    }

    /**
     * 初始化Int值
     *
     * @param prefKeyName
     * @param defaultValue
     * @param force
     */
    private void initIntPreference(String prefKeyName, int defaultValue,
                                   boolean force) {
        if (!spManager.getSharedPreferences().contains(prefKeyName) || force) {
            spManager.setInt(prefKeyName, defaultValue);
        }
    }

    /**
     * 初始化Long值
     *
     * @param prefKeyName
     * @param defaultValue
     * @param force
     */
    private void initLongPreference(String prefKeyName, long defaultValue,
                                    boolean force) {
        if (!spManager.getSharedPreferences().contains(prefKeyName) || force) {
            spManager.setLong(prefKeyName, defaultValue);
        }
    }

    /**
     * 初始化文件值
     *
     * @param prefKeyName
     * @param defaultValue
     * @param force
     */
    private void initFilePreference(String prefKeyName, File defaultValue,
                                    boolean force) {
        if (!spManager.getSharedPreferences().contains(prefKeyName) || force) {
            spManager.setFile(prefKeyName, defaultValue);
        }
    }

    /**
     * 软件重启preference值重置
     *
     * @param map
     */
    private void resetToDefaults(Map<String, Object> map) {
        for (Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof String) {
                spManager.setString(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                spManager.setInt(entry.getKey(), (Integer) entry.getValue());
            } else if (entry.getValue() instanceof Long) {
                spManager.setLong(entry.getKey(), (Long) entry.getValue());
            } else if (entry.getValue() instanceof Boolean) {
                spManager.setBoolean(entry.getKey(), (Boolean) entry.getValue());
            } else if (entry.getValue() instanceof File) {
                spManager.setFile(entry.getKey(), (File) entry.getValue());
            }
        }
    }

    /**
     * 存储用户信息
     *
     * @param user
     * @param
     */
    public void setUser(UserInfo user) {
        mUserInfo = user;
        spManager.setString(SPKeyManager.USERINFO_id, user.getId());
        spManager.setString(SPKeyManager.USERINFO_nickname, user.getNickname());
        spManager.setString(SPKeyManager.USERINFO_mobile, user.getMobile());
        spManager.setString(SPKeyManager.USERINFO_promotionCode, user.getPromotionCode());
        spManager.setString(SPKeyManager.USERINFO_inviter, user.getInviter());
        spManager.setString(SPKeyManager.USERINFO_avatar, user.getAvatar());
        spManager.setString(SPKeyManager.USERINFO_deviceToken, user.getDeviceToken());
        spManager.setString(SPKeyManager.USERINFO_expireTime, user.getExpireTime());
        spManager.setBoolean(SPKeyManager.USERINFO_isPermanet, user.isIsPermanet());
        spManager.setString(SPKeyManager.USERINFO_imToken, user.getImToken());
    }


    /**
     * 获取User
     */
    public UserInfo getUser() {
        if (mUserInfo == null) {
            mUserInfo = new UserInfo();
            mUserInfo.setId(spManager.getString(SPKeyManager.USERINFO_id));
            mUserInfo.setNickname(spManager.getString(SPKeyManager.USERINFO_nickname));
            mUserInfo.setMobile(spManager.getString(SPKeyManager.USERINFO_mobile));
            mUserInfo.setPromotionCode(spManager.getString(SPKeyManager.USERINFO_promotionCode));
            mUserInfo.setInviter(spManager.getString(SPKeyManager.USERINFO_inviter));
            mUserInfo.setAvatar(spManager.getString(SPKeyManager.USERINFO_avatar));
            mUserInfo.setDeviceToken(spManager.getString(SPKeyManager.USERINFO_deviceToken));
            mUserInfo.setExpireTime(spManager.getString(SPKeyManager.USERINFO_expireTime));
            mUserInfo.setIsPermanet(spManager.getBoolean(SPKeyManager.USERINFO_isPermanet));
            mUserInfo.setImToken(spManager.getString(SPKeyManager.USERINFO_imToken));

        }
        return mUserInfo;
    }

    /**
     * 清除用户信息
     */
    public void clearUser() {
        spManager.remove(SPKeyManager.USERINFO_id);
        spManager.remove(SPKeyManager.USERINFO_nickname);
        //spManager.remove(SPKeyManager.USERINFO_mobile);
        spManager.remove(SPKeyManager.USERINFO_promotionCode);
        spManager.remove(SPKeyManager.USERINFO_inviter);
        spManager.remove(SPKeyManager.USERINFO_avatar);
        spManager.remove(SPKeyManager.USERINFO_deviceToken);
        spManager.remove(SPKeyManager.USERINFO_expireTime);
        spManager.remove(SPKeyManager.USERINFO_isPermanet);
        spManager.remove(SPKeyManager.USERINFO_isLogin);
        spManager.remove(SPKeyManager.USERINFO_imToken);
        mUserInfo = null;
    }
    /**
     * 是否已经登录
     *
     * @return
     */
    public boolean isLogin() {
        return spManager.getBoolean(SPKeyManager.USERINFO_isLogin);
    }


    /**
     * 写入用户信息
     *
     * @param object
     */
    public void writeInSP(JSONObject object) {
        try {
            JSONObject dataObject = object.getJSONObject("data");
            if (mUserInfo == null) {
                mUserInfo = new UserInfo();
            }
            mUserInfo.setId(dataObject.optString("id"));
            mUserInfo.setNickname(dataObject.optString("nickname"));
            mUserInfo.setMobile(dataObject.optString("mobile"));
            mUserInfo.setPromotionCode(dataObject.optString("promotionCode"));
            mUserInfo.setInviter(dataObject.optString("inviter"));
            mUserInfo.setAvatar(dataObject.optString("avatar"));
            mUserInfo.setDeviceToken(dataObject.optString("deviceToken"));
            mUserInfo.setExpireTime(dataObject.optString("expireTime"));
            mUserInfo.setIsPermanet(dataObject.optBoolean("isPermanet"));
            mUserInfo.setImToken(dataObject.optString("imToken"));
            spManager.setBoolean(SPKeyManager.USERINFO_isLogin,true);
            setUser(mUserInfo);
            Log.e( "writeInSP: ", mUserInfo.toString());
            RongIMUtil.updateUserInfo(mUserInfo.getId(),mUserInfo.getNickname(),mUserInfo.getAvatar());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
