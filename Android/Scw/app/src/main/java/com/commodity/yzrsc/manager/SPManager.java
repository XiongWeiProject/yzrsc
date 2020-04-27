package com.commodity.yzrsc.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.commodity.yzrsc.MainApplication;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cn.yohoutils.Logger;

/**
 * 缓存管理者
 * @author liyushen
 *
 */
public class SPManager {
	private final SharedPreferences preferences;
	private final Editor editor;
	private static SPManager instance;
	private Map<String, Object> defaultValues;
	private Map<String, Object> resetValues;

	public static SPManager instance() {
		if (instance == null) {
			instance = new SPManager();
		}
		return instance;
	}

	@SuppressLint("CommitPrefEdits")
	public SPManager() {
		defaultValues = new HashMap<String, Object>();
		resetValues = new HashMap<String, Object>();
		preferences = MainApplication.getContext().getSharedPreferences(
				ConfigManager.FILE_NAME, Context.MODE_PRIVATE);
		editor = preferences.edit();

	}
	public SharedPreferences getSharedPreferences(){
		return preferences;
	}
	public Editor getEditor(){
		return editor;
	}
	/**
	 * 获取所有得的sharepreferences
	 * 
	 * @return 所有key, value
	 */
	public Map<String, Object> getDefaultValues() {
		return Collections.unmodifiableMap(defaultValues);
	}

	/**
	 * 获取所有启动软件需要重新设置的值
	 * 
	 * @return key, value集合
	 */
	public Map<String, Object> getResetValues() {
		return Collections.unmodifiableMap(resetValues);
	}

	public void resetValue(String key) {
		Logger.i("test", "key: " + defaultValues.get(key));
		resetValues.put(key, defaultValues.get(key));
	}

	/**
	 * 获取String类型
	 * 
	 * @param key
	 * @return value
	 */
	public String getString(String key) {
		return preferences.getString(key, "");
	}

	/**
	 * 设置String类型
	 * 
	 * @param key
	 * @param value
	 */
	public void setString(String key, String value) {
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * 设置Float类型
	 * 
	 * @param key
	 * @param value
	 */
	public void setFloat(String key, Float value) {
		editor.putFloat(key, value);
		editor.commit();
	}

	public Float getFloat(String key) {
		return preferences.getFloat(key, 0);
	}

	/**
	 * 获取int类型
	 * 
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		return preferences.getInt(key, 0);
	}

	/**
	 * 设置int类型
	 * 
	 * @param key
	 * @param value
	 */
	public void setInt(String key, int value) {
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 获取long类型
	 * 
	 * @param key
	 * @return
	 */
	public long getLong(String key) {
		return preferences.getLong(key, 0);
	}

	/**
	 * 设置long类型
	 * 
	 * @param key
	 * @param value
	 */
	public void setLong(String key, long value) {
		editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * 获取boolean类型
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBoolean(String key) {
		return preferences.getBoolean(key, false);
	}

	/**
	 * 获取boolean类型
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBooleanByCustomer(String key) {
		return preferences.getBoolean(key, false);
	}

	/**
	 * 设置boolean类型
	 * 
	 * @param key
	 * @param value
	 */
	public void setBoolean(String key, boolean value) {
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 通过key获取文件路径并转换成file对象
	 * 
	 * @param key
	 * @return
	 */
	public File getFile(String key) {
		return new File(preferences.getString(key, ""));
	}

	/**
	 * 设置文件转换成路径
	 * 
	 * @param key
	 * @param value
	 */
	public void setFile(String key, File value) {
		editor.putString(key, value.getAbsolutePath());
		editor.commit();
	}

	/**
	 * 移除某个值
	 * 
	 * @param key
	 */
	public void remove(String key) {
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 清除所有的值
	 */
	public void clear() {
		editor.clear();
		editor.commit();
	}

	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 * 
	 * @param context
	 * @param key
	 * @param object
	 */
	public static void put(Context context, String key, Object object) {

		SharedPreferences sp = context.getSharedPreferences(
				ConfigManager.FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();

		if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		} else {
			editor.putString(key, object.toString());
		}

		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 * 
	 * @param context
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	public static Object get(Context context, String key, Object defaultObject) {
		SharedPreferences sp = context.getSharedPreferences(
				ConfigManager.FILE_NAME, Context.MODE_PRIVATE);

		if (defaultObject instanceof String) {
			return sp.getString(key, (String) defaultObject);
		} else if (defaultObject instanceof Integer) {
			return sp.getInt(key, (Integer) defaultObject);
		} else if (defaultObject instanceof Boolean) {
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if (defaultObject instanceof Float) {
			return sp.getFloat(key, (Float) defaultObject);
		} else if (defaultObject instanceof Long) {
			return sp.getLong(key, (Long) defaultObject);
		}

		return null;
	}

	/**
	 * 移除某个key值已经对应的值
	 * 
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(
				ConfigManager.FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 清除所有数据
	 * 
	 * @param context
	 */
	public static void clear(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				ConfigManager.FILE_NAME, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 查询某个key是否已经存在
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean contains(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(
				ConfigManager.FILE_NAME, Context.MODE_PRIVATE);
		return sp.contains(key);
	}

	/**
	 * 返回所有的键值对
	 * 
	 * @param context
	 * @return
	 */
	public static Map<String, ?> getAll(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				ConfigManager.FILE_NAME, Context.MODE_PRIVATE);
		return sp.getAll();
	}

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 * 
	 * @author liyushen
	 * 
	 */
	private static class SharedPreferencesCompat {
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 * 
		 * @return
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod() {
			try {
				Class clz = Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
			}

			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 * 
		 * @param editor
		 */
		public static void apply(Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			editor.commit();
		}
	}

}