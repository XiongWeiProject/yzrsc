package com.commodity.scw.ui.activity.general;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commodity.scw.MainApplication;
import com.commodity.yzrsc.R;
import com.commodity.scw.manager.ImageLoaderManager;
import com.commodity.scw.manager.SPKeyManager;
import com.commodity.scw.model.AlbumInfo;
import com.commodity.scw.ui.BaseActivity;
import com.commodity.scw.utils.ImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yohoutils.EfficientAdapter;
import cn.yohoutils.StringUtil;


/**
 * Created by liyushen on 2017/6/3 22:41
 * 选择图片
 */
public class PhotoSelectActivity extends BaseActivity {
	private GridView vPhotoGrid;
	/**
	 * 当前用于显示的照片
	 */
	private List<String> mCurPhotoList = null;
	/**
	 * 选中
	 */
	private TextView vSelected = null;
	/**
	 * 当前选中的照片
	 */
	private List<String> mCurSelectPhoto = null;

	private PhotoAdapter mAdapter = null;

	private boolean mIsToTakePhoto = false;// 是否直接进拍照

	/**
	 * 各个分组的照片
	 */
	private Map<String, List<String>> mGruopMap = new HashMap<String, List<String>>();
	private List<AlbumInfo> mAlbumList = new ArrayList<AlbumInfo>();
	private final String ALL_PHOTO_KEY = "All";
	private final String CAMERA = "Camera";
	private final static int SCAN_OK = 1;
	private ProgressDialog mProgressDialog;

	private TextView vAlbumName = null;
	private LinearLayout vAlbumBackGround = null;
	private RelativeLayout vPhotoselectLayout = null;
	private ListView vAlbumList = null;
	private AlbumListAdapter mAlbumAdapter = null;
	/**
	 * 是否单选
	 */
	private boolean mIsSelectSingle = false;
	/**
	 * 拍照存储路径
	 */
	private Uri mImageFilePath;
	/**
	 * 最大可选择照片数
	 */
	private int mMaxNum = 12;


	@Override
	protected int getContentView() {
		return R.layout.activity_photoselelct;
	}

	@Override
	protected void initView() {
		vPhotoGrid = (GridView) findViewById(R.id.photoselect_grid);
		vSelected = (TextView) findViewById(R.id.selected);
		vAlbumName = (TextView) findViewById(R.id.album_name);
		vAlbumBackGround = (LinearLayout) findViewById(R.id.album_background);
		vPhotoselectLayout = (RelativeLayout) findViewById(R.id.photoselect_bottom);
		vAlbumList = (ListView) findViewById(R.id.album_list);
		init();
	}

	@Override
	protected void initListeners() {
		vPhotoGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				String path = mCurPhotoList.get(position);
				if (path == null) {
					return;
				}
				if (mCurSelectPhoto == null) {
					mCurSelectPhoto = new ArrayList<String>();
				}
				if (path.equals(CAMERA)) {
					gotoTakePhoto();
					return;
				} else if (mIsSelectSingle) {
					// 单选模式

					mCurSelectPhoto.clear();
					mCurSelectPhoto.add(path);
					finishByResult();
				}

			}
		});

		vSelected.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finishByResult();
			}
		});

		vAlbumName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showOrHideAlbumList();
			}
		});

		vAlbumBackGround.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showOrHideAlbumList();
			}
		});

		vAlbumList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				if (mAlbumList == null || mAlbumList.size() <= position) {
					return;
				}
				AlbumInfo info = mAlbumList.get(position);

				if (info == null) {
					return;
				}
				vAlbumName.setText(info.getAlbumName());
				mCurPhotoList = mGruopMap.get(info.getFilePath());
				if (mCurPhotoList == null) {
					mCurPhotoList = new ArrayList<String>();
				}
				// mCurPhotoList.add(0, CAMERA);
				mAdapter.setDataSource(mCurPhotoList);
				showOrHideAlbumList();
				for (int i = 0; i < mAlbumList.size(); i++) {
					AlbumInfo albumInfo = mAlbumList.get(i);
					if (albumInfo == null) {
						continue;
					}
					if (info.equals(albumInfo)) {
						albumInfo.setSelect(true);
					} else {
						albumInfo.setSelect(false);
					}
				}
				mAlbumAdapter.notifyDataSetChanged();
			}
		});

		vPhotoselectLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	private void gotoTakePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		ContentValues values = new ContentValues(3);
		values.put(MediaStore.Images.Media.DISPLAY_NAME,
				"UTravel" + System.currentTimeMillis());
		values.put(MediaStore.Images.Media.DESCRIPTION, "this is description");
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
		mImageFilePath = PhotoSelectActivity.this.getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageFilePath);
		startActivityForResult(intent, 1);
	}

	protected void init() {
		mAdapter = new PhotoAdapter(getApplicationContext());
		vPhotoGrid.setAdapter(mAdapter);

		mAlbumAdapter = new AlbumListAdapter(getApplicationContext(), null);
		vAlbumList.setAdapter(mAlbumAdapter);

		if (MainApplication.mHashMap.containsKey("SelectPhotos")) {
			mCurSelectPhoto = (List<String>) MainApplication.mHashMap.get("SelectPhotos");
			MainApplication.mHashMap.remove("SelectPhotos");
		}
		if (MainApplication.mHashMap.containsKey(SPKeyManager.PHOTO_MODE)) {
			mIsToTakePhoto = (Boolean) MainApplication.mHashMap
					.get(SPKeyManager.PHOTO_MODE);
			MainApplication.mHashMap.remove(SPKeyManager.PHOTO_MODE);
		}
		if (MainApplication.mHashMap.containsKey(SPKeyManager.INDEX)) {
			mMaxNum = (Integer) MainApplication.mHashMap.get(SPKeyManager.INDEX);
			MainApplication.mHashMap.remove(SPKeyManager.INDEX);
		}

		// 直接进拍照
		if (mIsToTakePhoto) {
			gotoTakePhoto();
		}

		if (mCurSelectPhoto == null) {
			mCurSelectPhoto = new ArrayList<String>();
		}
		if (MainApplication.mHashMap.containsKey("selectSingle")) {
			mIsSelectSingle = (Boolean) MainApplication.mHashMap
					.get("selectSingle");
			MainApplication.mHashMap.remove("selectSingle");
		}
		setSelectedButtonStatus();
		setTitle("图片");
		getImages();
	}

	/**
	 * 设置完成按钮状态
	 */
	private void setSelectedButtonStatus() {

		if (mCurSelectPhoto == null || mCurSelectPhoto.size() == 0) {
			vSelected.setText("完成");
			vSelected.setEnabled(false);
		} else {
			vSelected.setText("(" + mCurSelectPhoto.size() + "/" + mMaxNum
					+ ")" + "完成");
			vSelected.setEnabled(true);
		}
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// if(!mIsSelectSingle){
	// getMenuInflater().inflate(R.menu.activity_livelist_menu, menu);
	// }
	// return super.onCreateOptionsMenu(menu);
	// }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (vAlbumBackGround.getVisibility() == View.VISIBLE) {
				showOrHideAlbumList();
				break;
			}
			finish();
			break;
		// case R.id.menu_selected:
		// Toast.makeText(getApplicationContext(), "选定",
		// Toast.LENGTH_SHORT).show();
		// finishByResult();
		// break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * 带数据返回
	 */
	private void finishByResult() {
		setResult(RESULT_OK);
		if (mCurSelectPhoto != null && mCurSelectPhoto.size() > 0) {
			MainApplication.mHashMap.put("SelectPhotos", mCurSelectPhoto);
		}
		finish();
	}

	private class PhotoAdapter extends EfficientAdapter<String> {
		private RelativeLayout.LayoutParams mLinearLayout = null;

		public PhotoAdapter(Context context) {
			super(context);
		}

		@Override
		protected int getItemLayout() {

			return R.layout.item_photoselect;
		}

		@Override
		protected void initView(View v) {
			ViewHolder holder = new ViewHolder();
			holder.vPhoto = (ImageView) v.findViewById(R.id.photo_show);
			holder.vCover = (View) v.findViewById(R.id.cover_view);
			holder.vCheck = (CheckBox) v.findViewById(R.id.photo_check);
			v.setTag(holder);
		}

		@Override
		protected void bindView(View v, final String data, int pos) {
			final ViewHolder holder = (ViewHolder) v.getTag();

			if (mLinearLayout == null) {
				mLinearLayout = (RelativeLayout.LayoutParams) holder.vPhoto
						.getLayoutParams();
				mLinearLayout.width = MainApplication.SCREEN_W / 3;
				mLinearLayout.height = MainApplication.SCREEN_W / 3;
			}
			holder.vPhoto.setLayoutParams(mLinearLayout);

			if (data == null) {
				return;
			}
			if (data.equals(CAMERA)) {
				holder.vPhoto.setImageResource(R.drawable.selector_live_camera);
				holder.vCover.setVisibility(View.GONE);
				holder.vCheck.setVisibility(View.GONE);
				return;
			} else {
				holder.vPhoto.setImageBitmap(null);
				holder.vCheck.setVisibility(View.VISIBLE);
				String url = "file:///" + data;
				ImageLoaderManager.getInstance().displayImage(url, holder.vPhoto,
						R.drawable.icons_clock);
			}

			if (mIsSelectSingle) {
				holder.vCover.setVisibility(View.GONE);
				holder.vCheck.setVisibility(View.GONE);
			}

			if (mCurSelectPhoto.contains(data)) {
				holder.vCover.setVisibility(View.VISIBLE);
				holder.vCheck.setChecked(true);
			} else {
				holder.vCover.setVisibility(View.GONE);
				holder.vCheck.setChecked(false);
			}
			// 选中
			holder.vCheck.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mCurSelectPhoto.contains(data)) {
						mCurSelectPhoto.remove(data);
						holder.vCover.setVisibility(View.GONE);
						holder.vCheck.setChecked(false);
					} else {
						if (mCurSelectPhoto.size() >= mMaxNum) {
							holder.vCheck.setChecked(false);
							Toast.makeText(getApplicationContext(),
									"最多选择" + mMaxNum + "张照片",
									Toast.LENGTH_SHORT).show();
							return;
						}
						mCurSelectPhoto.add(data);
						holder.vCover.setVisibility(View.VISIBLE);
						holder.vCheck.setChecked(true);
					}
					setSelectedButtonStatus();
				}
			});
			holder.vPhoto.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mCurSelectPhoto.contains(data)) {
						mCurSelectPhoto.remove(data);
						holder.vCover.setVisibility(View.GONE);
						holder.vCheck.setChecked(false);
					} else {
						if (mCurSelectPhoto.size() >= mMaxNum) {
							holder.vCheck.setChecked(false);
							Toast.makeText(getApplicationContext(),
									"最多选择" + mMaxNum + "张照片",
									Toast.LENGTH_SHORT).show();
							return;
						}
						mCurSelectPhoto.add(data);
						holder.vCover.setVisibility(View.VISIBLE);
						holder.vCheck.setChecked(true);
					}
					setSelectedButtonStatus();
				}
			});
		}

		private class ViewHolder {
			private ImageView vPhoto;// 图片
			private View vCover;// 遮层
			private CheckBox vCheck;// 选中
		}
	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
	 */
	private void getImages() {
		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
		// startTime = System.currentTimeMillis();
		new Thread(new Runnable() {

			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = PhotoSelectActivity.this
						.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED + " desc ");

				if (mCursor == null) {
					mHandler.sendEmptyMessage(SCAN_OK);
					return;
				}

				while (mCursor.moveToNext()) {
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					File file = new File(path);
					if (!file.exists()) {
						file.mkdir();
					}
					if (!file.isDirectory()){
						// 获取该图片的父路径名
						String parentName = file.getParentFile().getName();
						String filePath = file.getParentFile().getPath();
						// 根据父路径名将图片放入到mGruopMap中
						if (!mGruopMap.containsKey(filePath)) {
							List<String> chileList = new ArrayList<String>();
							chileList.add(path);
							mGruopMap.put(filePath, chileList);
							AlbumInfo info = new AlbumInfo();
							info.setAlbumName(parentName);
							info.setFilePath(filePath);
							info.setPhotos(chileList);
							mAlbumList.add(info);
						} else {
							mGruopMap.get(filePath).add(path);
						}
						// 添加到ALL里面去
						if (mGruopMap.containsKey(ALL_PHOTO_KEY)) {
							mGruopMap.get(ALL_PHOTO_KEY).add(path);
						} else {
							List<String> chileList = new ArrayList<String>();
							chileList.add(path);
							mGruopMap.put(ALL_PHOTO_KEY, chileList);
							AlbumInfo info = new AlbumInfo();
							info.setAlbumName(ALL_PHOTO_KEY);
							info.setFilePath(ALL_PHOTO_KEY);
							info.setPhotos(chileList);
							info.setSelect(true);
							mAlbumList.add(0, info);
						}
					}
				}

				// 扫描图片完成
				mHandler.sendEmptyMessage(SCAN_OK);
				mCursor.close();
			}
		}).start();

	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SCAN_OK:
				mAlbumAdapter.setDataSource(mAlbumList);

				mCurPhotoList = mGruopMap.get(ALL_PHOTO_KEY);
				if (mCurPhotoList == null) {
					mCurPhotoList = new ArrayList<String>();
				}
				mCurPhotoList.add(0, CAMERA);
				mAdapter.setDataSource(mCurPhotoList);
				mProgressDialog.dismiss();

				break;
			}
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {

			if (mCurSelectPhoto == null) {
				mCurSelectPhoto = new ArrayList<String>();
			}
			if (mIsSelectSingle) {
				// 单选模式
				mCurSelectPhoto.clear();
			}
			mCurSelectPhoto.add(ImageUtil.getImageAbsolutePath(
					PhotoSelectActivity.this, mImageFilePath));
			finishByResult();

		} else {
			if (mIsToTakePhoto) {
				finish();
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (vAlbumBackGround.getVisibility() == View.VISIBLE) {
			showOrHideAlbumList();
			return;
		}
		super.onBackPressed();
	}

	/**
	 * 显示或隐藏相册列表
	 */
	private void showOrHideAlbumList() {
		if (vAlbumBackGround.getVisibility() == View.GONE) {
			vAlbumBackGround.setVisibility(View.VISIBLE);
			// vMoreTravel.setVisibility(View.VISIBLE);
			// vMoreVideo.setVisibility(View.VISIBLE);
			// vMorePhoto.setVisibility(View.VISIBLE);
			// MobclickAgent.onEvent(getActivity(), "IndexMore");

		} else {
			vAlbumBackGround.setVisibility(View.GONE);
			// vMoreTravel.setVisibility(View.GONE);
			// vMoreVideo.setVisibility(View.GONE);
			// vMorePhoto.setVisibility(View.GONE);
		}
	}

	/**
	 * 活动列表适配器
	 *
	 * @author 谢晓祥
	 *
	 */
	private class AlbumListAdapter extends EfficientAdapter<AlbumInfo> {

		public AlbumListAdapter(Context context, List<AlbumInfo> dataList) {
			super(context, dataList);
		}

		@Override
		protected int getItemLayout() {
			return R.layout.item_album;
		}

		@Override
		protected void initView(final View v) {

			if (v.getTag() == null) {
				ViewHolder holder = new ViewHolder();
				holder.vImg = (ImageView) v.findViewById(R.id.album_img);
				holder.vAlbumName = (TextView) v.findViewById(R.id.album_name);
				holder.vCheck = (ImageView) v.findViewById(R.id.album_check);
				v.setTag(holder);
			}
		}

		@Override
		protected void bindView(View view, AlbumInfo info, int pos) {
			final ViewHolder holder = (ViewHolder) view.getTag();
			if (info == null) {
				return;
			}
			holder.vAlbumName.setText(info.getAlbumName());

			String firstPhoto = null;
			List<String> photos = info.getPhotos();
			if (photos != null) {
				for (int i = 0; i < photos.size(); i++) {
					String photo = photos.get(i);
					if (!StringUtil.isEmpty(photo) && !photo.equals(CAMERA)) {
						firstPhoto = photo;
						break;
					}
				}
			}

			String imgUrl = "file:///" + firstPhoto;
			android.util.Log.d("ss", "==imgUrl:" + imgUrl);
			ImageLoaderManager.getInstance().displayImage(imgUrl, holder.vImg);
			if (info.isSelect()) {
				holder.vCheck.setVisibility(View.VISIBLE);
			} else {
				holder.vCheck.setVisibility(View.GONE);
			}
			// holder.vCheck.setChecked(info.isSelect());
		}

		private class ViewHolder {
			private ImageView vImg;//
			private TextView vAlbumName;//
			private ImageView vCheck;//
		}
	}

}