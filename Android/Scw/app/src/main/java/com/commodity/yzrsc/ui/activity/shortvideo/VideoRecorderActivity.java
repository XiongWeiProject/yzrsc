package com.commodity.yzrsc.ui.activity.shortvideo;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.commodity.yzrsc.R;
import com.commodity.yzrsc.manager.ConfigManager;
import com.yixia.camera.MediaRecorderBase;
import com.yixia.camera.MediaRecorderNative;
import com.yixia.camera.VCamera;
import com.yixia.camera.model.MediaObject;
import com.yixia.videoeditor.adapter.UtilityAdapter;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by liyushen on 2017/6/7 23:01
 */
public class VideoRecorderActivity extends Activity implements MediaRecorderBase.OnEncodeListener {

    private static final int REQUEST_KEY = 100;
    private MediaRecorderNative mMediaRecorder;
    private MediaObject mMediaObject;
    private FocusSurfaceView sv_ffmpeg;
    private RecordedButton rb_start;
    private int maxDuration = 8000;
    private boolean recordedOver;
    private MyVideoView vv_play;
    private ImageView iv_finish;
    private ImageView iv_back;
    private float dp100;
    private TextView tv_hint;
    private float backX = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_recorder);

        sv_ffmpeg = (FocusSurfaceView) findViewById(R.id.sv_ffmpeg);
        rb_start = (RecordedButton) findViewById(R.id.rb_start);
        vv_play = (MyVideoView) findViewById(R.id.vv_play);
        iv_finish = (ImageView) findViewById(R.id.iv_finish);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_hint = (TextView) findViewById(R.id.tv_hint);

        dp100 = getResources().getDimension(R.dimen.distance_100);

        initMediaRecorder();

        sv_ffmpeg.setTouchFocus(mMediaRecorder);

        rb_start.setMax(maxDuration);
        rb_start.setOnGestureListener(new RecordedButton.OnGestureListener() {
            @Override
            public void onLongClick() {
                mMediaRecorder.startRecord();
                myHandler.sendEmptyMessageDelayed(0, 50);
            }
            @Override
            public void onClick() {

            }
            @Override
            public void onLift() {
                recordedOver = true;
                videoFinish();
            }
            @Override
            public void onOver() {
                recordedOver = true;
                rb_start.closeButton();
                videoFinish();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);
    }

    /**
     * 初始化视频拍摄状态
     */
    private void initMediaRecorderState(){

        vv_play.setVisibility(View.GONE);
        vv_play.pause();

        iv_back.setX(backX);
        iv_finish.setX(backX);

        tv_hint.setVisibility(View.VISIBLE);
        rb_start.setVisibility(View.VISIBLE);

        lastValue = 0;
    }

    private void videoFinish() {
        mMediaRecorder.stopRecord();
        //开始合成视频, 异步
        mMediaRecorder.startEncoding();
    }

    float lastValue;
    private void startAnim() {

        rb_start.setVisibility(View.GONE);
        ValueAnimator va = ValueAnimator.ofFloat(0, dp100).setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (float) animation.getAnimatedValue();
                float dis = value-lastValue;
                iv_back.setX(iv_back.getX()-dis);
                iv_finish.setX(iv_finish.getX()+dis);
                lastValue = value;
            }
        });
        va.start();
    }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(!recordedOver){
                rb_start.setProgress(mMediaObject.getDuration());
                myHandler.sendEmptyMessageDelayed(0, 50);
                tv_hint.setVisibility(View.GONE);
            }
        }
    };

    /**
     * 初始化录制对象
     */
    private void initMediaRecorder() {

        mMediaRecorder = new MediaRecorderNative();
        mMediaRecorder.setOnEncodeListener(this);
        String key = String.valueOf(System.currentTimeMillis());
        //设置缓存文件夹
        mMediaObject = mMediaRecorder.setOutputDirectory(key, VCamera.getVideoCachePath());
        //设置视频预览源
        mMediaRecorder.setSurfaceHolder(sv_ffmpeg.getHolder());
        //准备
        mMediaRecorder.prepare();
        //滤波器相关
        UtilityAdapter.freeFilterParser();
        UtilityAdapter.initFilterParser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaRecorder.startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaRecorder.stopPreview();
    }

    @Override
    public void onBackPressed() {
        if(rb_start.getVisibility() != View.VISIBLE) {
            initMediaRecorderState();
            LinkedList<MediaObject.MediaPart> medaParts = mMediaObject.getMediaParts();
            for (MediaObject.MediaPart part : medaParts) {
                mMediaObject.removePart(part, true);
            }
            mMediaRecorder.startPreview();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if(backX == -1) {
            backX = iv_back.getX();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_KEY){
                LinkedList<MediaObject.MediaPart> medaParts = mMediaObject.getMediaParts();
                for (MediaObject.MediaPart part : medaParts){
                    mMediaObject.removePart(part, true);
                }
                deleteDir(ConfigManager.VIDEO_PATH);
            }
        }
    }

    /**
     * 删除文件夹下所有文件
     */
    public static void deleteDir(String dirPath){

        File dir = new File(dirPath);
        if(dir.exists() && dir.isDirectory()){
            File[] files = dir.listFiles();
            for(File f: files){
                deleteDir(f.getAbsolutePath());
            }
        }else if(dir.exists()) {
            dir.delete();
        }
    }

    @Override
    public void onEncodeStart() {
        Log.i("Log.i", "onEncodeStart");
    }

    @Override
    public void onEncodeProgress(int progress) {

    }

    /**
     * 视频编辑完成
     */
    @Override
    public void onEncodeComplete() {
        final String path = mMediaObject.getOutputTempVideoPath();
        if(!TextUtils.isEmpty(path)){
            iv_finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(VideoRecorderActivity.this, EditVideoActivity.class);
//                    intent.putExtra("path", path);
//                    startActivityForResult(intent, REQUEST_KEY);
//                    initMediaRecorderState();
                    // 返回到播放页面
                    Intent intent = new Intent();
                    intent.putExtra("path",path);
                    setResult(RESULT_OK,intent);
                    finish();
                    initMediaRecorderState();
                }
            });

            vv_play.setVisibility(View.VISIBLE);
            vv_play.setVideoPath(path);
            vv_play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    vv_play.setLooping(true);
                    vv_play.start();
                }
            });
            vv_play.start();

            recordedOver = false;
            startAnim();
        }
    }

    @Override
    public void onEncodeError() {
        Log.i("Log.i", "onEncodeError");
    }
}
