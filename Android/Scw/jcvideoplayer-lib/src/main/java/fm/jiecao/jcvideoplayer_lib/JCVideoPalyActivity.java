package fm.jiecao.jcvideoplayer_lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import de.greenrobot.event.EventBus;

/**
 * Created by liyushen on 2017/6/2 21:36
 */
public class JCVideoPalyActivity extends Activity{

    public static void toActivity(Context context, String url, String thumb, String title) {
        STATE = JCVideoPlayer.CURRENT_STATE_NORMAL;
        URL = url;
        THUMB = thumb;
        TITLE = title;
        start = true;
        Intent intent = new Intent(context, JCVideoPalyActivity.class);
        context.startActivity(intent);
    }

    JCVideoPlayer jcVideoPlayer;
    /**
     * 刚启动全屏时的播放状态
     */
    public static int STATE = -1;
    public static String URL;
    public static String TITLE;
    public static String THUMB;
    public static boolean manualQuit = false;
    protected static Skin skin;
    static boolean start = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        jcVideoPlayer = (JCVideoPlayer) findViewById(R.id.jcvideoplayer);
        if (skin != null) {
            jcVideoPlayer.setSkin(skin.titleColor, skin.timeColor, skin.seekDrawable, skin.bottomControlBackground,
                    skin.enlargRecId, skin.shrinkRecId);
        }
        Log.e( "onCreate: ", URL);
        jcVideoPlayer.setUp(URL, THUMB, TITLE);
        jcVideoPlayer.setState(STATE);
        JCMediaManager.intance().setUuid(jcVideoPlayer.uuid);
        manualQuit = false;
        if (start) {
            jcVideoPlayer.ivStart.performClick();
        }
    }

    public void onEventMainThread(VideoEvents videoEvents) {
        if (videoEvents.type == VideoEvents.VE_SURFACEHOLDER_FINISH_FULLSCREEN || videoEvents.type == VideoEvents.VE_MEDIAPLAYER_FINISH_COMPLETE) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
