package com.commodity.scw.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Log;

import com.commodity.scw.manager.ConfigManager;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import cn.yohoutils.Logger;
import cn.yohoutils.SafetyUtil;
import cn.yohoutils.StringUtil;

/**
 * Function: 处理文件名，创建文件的类， 如果要得到某个文件目录 {@link}
 * 
 * Date: 2016年3月30日 
 * 
 * @author liyushen
 */
@SuppressWarnings("unused")
public final class FileUtil {
	private static final String TAG = "FileUtil";


	/**
	 * 删除文件夹
	 * @param folder
	 */
	public static void deleteFolderRecursively(File folder) {
		if (folder != null && folder.isDirectory() && folder.canWrite()) {
			File[] listFiles = folder.listFiles();

			if (listFiles != null) {
				for (File f : listFiles) {
					if (f.isFile()) {
						f.delete();
					} else if (f.isDirectory()) {
						deleteFolderRecursively(f);
					}
				}
				folder.delete();
			}
		}
	}

	/**
	 * 创建一个目录，包括子目录
	 *
	 * @param directory  创建的目录 不能为null{@code null}
	 * @throws NullPointerException 如果目录为null {@code null}
	 * @throws IOException 不能被创建
	 */
	public static void forceMkdir(File directory) throws Exception {
		org.apache.commons.io.FileUtils.forceMkdir(directory);
	}

	/**
	 * 通过传入的路径得到名字
	 * <p>
	 * <pre>
	 * a/b/c.txt --> c.txt
	 * a.txt     --> a.txt
	 * a/b/c     --> c
	 * a/b/c/    --> ""
	 * http://www.xxx.com/d.jpg --> d.jpg
	 * </pre>
	 * <p>
	 *
	 * @param filename  http或者file, null returns null
	 * @return 返回最后的名字
	 */
	public static String getName(String filename) {
		return FilenameUtils.getName(filename);
	}

	/**
	 * 通过路径得到没有后缀的文件名
	 * <p>
	 * <pre>
	 * a/b/c.txt --> c
	 * a.txt     --> a
	 * a/b/c     --> c
	 * a/b/c/    --> ""
	 * http://xxx.com/d.jpg --> d
	 * </pre>
	 * <p>
	 *
	 * @param filename  http或者file, null returns null
	 * @return 没有后缀的名字
	 */
	public static String getBaseName(String filename) {
		return FilenameUtils.getBaseName(filename);
	}

	/**
	 * 获取后缀
	 * <p>
	 * <pre>
	 * foo.txt      --> "txt"
	 * a/b/c.jpg    --> "jpg"
	 * a/b.txt/c    --> ""
	 * a/b/c        --> ""
	 * </pre>
	 * <p>
	 *
	 * @param filename 整个文件名
	 * @return 后缀
	 */
	public static String getExtension(String filename) {
		return FilenameUtils.getExtension(filename);
	}

	/**
	 * <pre>
	 * C:\a\b\c.txt --> C:\a\b\
	 * ~/a/b/c.txt  --> ~/a/b/
	 * a.txt        --> ""
	 * a/b/c        --> a/b/
	 * a/b/c/       --> a/b/c/
	 * C:           --> C:
	 * C:\          --> C:\
	 * ~            --> ~/
	 * ~/           --> ~/
	 * ~user        --> ~user/
	 * ~user/       --> ~user/
	 * </pre>
	 * <p>
	 */
	public static String getFullPath(String filename) {
		return FilenameUtils.getFullPath(filename);
	}

	/**
	 * 判断文件是否存成
	 * @param fileName
	 * @return true：文件存成 false:文件不存在
	 */
	public static boolean isFileExist(String fileName) {
		boolean flag = false;
		if (fileName == null || fileName.length() == 0) {
			return false;
		}
		File file = new File(fileName);
		flag = isFileExist(file);
		return flag;
	}

	/**
	 * 判断文件是否存在
	 * @param file 文件
	 * @return  true：文件存成 false:文件不存在
	 */
	public static boolean isFileExist(File file) {
		if (file.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为URI路径
	 * @param filename
	 * @return true:是URI路径 false:不是
	 */
	public static boolean isURIPath(String filename) {
		if (filename.contains(":")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 生成文件，如果父文件夹不存在，则先生成父文件夹
	 * 
	 * @param fileName
	 *            :要生成的文件全路径
	 * @return File对象，如果有文件名不存在则返回null
	 */
	public static File createFile(String fileName) {

		if (fileName == null || fileName.length() <= 0) {
			return null;
		}
		File file = new File(fileName);
		// 获取父文件夹
		File folderFile = file.getParentFile();
		if (!folderFile.exists()) {
			folderFile.mkdirs();
		}
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 复制单个文件
	 * @param oldPath String 原文件路径 如：c:/fqf.txt
	 * @param newPath String 复制后路径 如：f:/fqf.txt
	 */
	public  boolean copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { //文件存在时
				InputStream inStream = new FileInputStream(oldPath); //读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				int length;
				while ( (byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; //字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
			return true;
		}
		catch (Exception e) {
			System.out.println("复制单个文件操作出错");
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * 复制一个文件
	 * 
	 * @param src
	 * @param tar
	 * @throws Exception
	 */
	public static void copyFile(File src, File tar) throws Exception {
		try {
			int bytesum = 0;
			int byteread = 0;
			if (src.isFile()) { // 文件存在时
				InputStream inStream = new FileInputStream(src); // 读入原文件
				FileOutputStream fs = new FileOutputStream(tar);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					//System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
				src.delete();
			}
		} catch (Exception e) {
			//System.out.println("复制单个文件操作出错");
			e.printStackTrace();

		}
		// if (src.isFile()) {
		// InputStream is = new FileInputStream(src);
		// OutputStream op = new FileOutputStream(tar);
		// BufferedInputStream bis = new BufferedInputStream(is);
		// BufferedOutputStream bos = new BufferedOutputStream(op);
		// byte[] bt = new byte[4*1024];
		// int len = bis.read(bt);
		// while (len != -1) {
		// bos.write(bt, 0, len);
		// len = bis.read(bt);
		// }
		// bis.close();
		// bos.close();
		// src.delete();
		// }
	}

	/**
	 * 获取文件大小
	 * 
	 * @param
	 * @return
	 */
	public static long getFileSize(File file) {
		long dirSize = 0;

		if (file == null) {
			return 0;
		}
		if (!file.isDirectory()) {
			dirSize += file.length();
			return dirSize;
		}

		File[] files = file.listFiles();
		for (File f : files) {
			if (f.isFile()) {
				dirSize += f.length();
			} else if (f.isDirectory()) {
				dirSize += f.length();
				dirSize += getFileSize(f); // 如果遇到目录则通过递归调用继续统计
			}
		}

		return dirSize;
	}

	/**
	 * 获取文件夹大小
	 * 
	 * @return
	 */
	public static String getDirSize(Context context) {
		File file = StorageUtils.getCacheDirectory(context);
		//ZGSystemUtil.getImageCacheDirectory();
		double size = 0;
		long dirSize = getFileSize(file);
		size = (dirSize + 0.0) / (1024 * 1024);
		DecimalFormat df = new DecimalFormat("0.00");// 以Mb为单位保留两位小数
		String filesize = df.format(size);
		return filesize;

	}
	
	/**
	 * 获取加密串
	 * @param token
	 * @param d  时间戳
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getEntryData(String uid, String token, String d){
		String md5Data = "";
	/*	SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    String d = format.format(curTime);*/
		md5Data = uid+token+d;
		Logger.i("AAA===", "AAA==="+token+"   "+md5Data+"    "+SafetyUtil.encryptStringToMd5(md5Data,"32"));
		Log.i("FileUtil", "uid"+uid+"token"+token+"entryData"+SafetyUtil.encryptStringToMd5(md5Data,"32")+"curTime"+d);
		return SafetyUtil.encryptStringToMd5(md5Data,"32");
	}
	
    
    /**  
     * 获取application中指定的meta-data  
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空 
     */  
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;  
        }  
        String resultData = null;
        try {  
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {  
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {  
                    if (applicationInfo.metaData != null) {  
                        resultData = applicationInfo.metaData.getString(key);  
                    }  
                }  
            }  
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();  
        }  
        return resultData;  
    }
	public static String zoomPhoto2Local(String filePath) {
		if (StringUtil.isEmpty(filePath)) {
			return null;
		}
		String name = SafetyUtil.encryptStringToMd5(filePath, "32");
		File f = FileUtil.createFile(ConfigManager.IMG_PATH + name + ".jpg");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		int baseWidth = 1000;
		if (options.outWidth > baseWidth) {
			options.inSampleSize = options.outWidth / baseWidth;
			if ((options.outWidth / options.inSampleSize) >= baseWidth * 1.5) {
				options.inSampleSize += 1;
			}
		}
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(filePath, options);
		int degree = getImageDegree(filePath);
		Bitmap newBitmap = rotaingImageView(degree, bitmap);

		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		newBitmap.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
		if (newBitmap != null) {
			newBitmap.recycle();
			newBitmap = null;
		}
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return f.getAbsolutePath();
	}

	/**
	 * 获取圖片角度
	 *
	 * @param path
	 * @return
	 */
	public static int getImageDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/*
	 * 旋转图片
	 *
	 * @param angle
	 *
	 * @param bitmap
	 *
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	public static void deleteFiles(String path){
		File file = new File(path);
		if (file.isDirectory()) {
			// delete(file.)
			File[] files = file.listFiles();

			for (File f : files) {
				deleteFiles(f.getAbsolutePath());
			}

		} else if (file.exists() && file.isFile()) {
			Log.v("FileUtils", "删除文件 -> " + file.getPath());
			file.delete();
		} else if (!file.exists()) {
			Log.v("FileUtils", "该目录下无任何文件(或 该文件不存在) -> " + file.getPath());
		}

	}
}
