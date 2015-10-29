package com.jxthelp.fragment;

import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jxthelp.App;
import com.jxthelp.R;
import com.jxthelp.ui.MainActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

/**
 * Created by idisfkj on 15-9-17 12:20.
 * Email: idisfkj@qq.com
 */
public class FragmentTest extends Fragment {

    @InjectView(R.id.surface_view)
    VideoView surfaceView;
    @InjectView(R.id.operation_bg)
    ImageView operationBg;
    @InjectView(R.id.operation_percent)
    ImageView operationPercent;
    @InjectView(R.id.operation_volume_brightness)
    FrameLayout operationVolumeBrightness;
    @InjectView(R.id.operation_full)
    ImageView operationFull;

    private AudioManager mAudioManager;
    private String url = "http://218.87.136.27:8080/attach/201510/20151024fVA0.flv";
    private String url1="http://www.modrails.com/videos/passenger_nginx.mov";
    /**
     * 最大声音
     */
    private int mMaxVolume;
    /**
     * 当前声音
     */
    private int mVolume = -1;
    /**
     * 当前亮度
     */
    private float mBrightness = -1f;
    /**
     * 当前缩放模式
     */
    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private GestureDetector mGestureDetector;
    private MediaController mMediaController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, null);
        ButterKnife.inject(this, view);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mGestureDetector.onTouchEvent(event))
                    return true;

                // 处理手势结束
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        endGesture();
                        break;
                }
                System.out.println("OK");
                return true;
            }
        });
        mAudioManager = (AudioManager) getActivity().getSystemService(App.getContext().AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        surfaceView.setVideoURI(Uri.parse(url));
        surfaceView.requestFocus();
        mMediaController = new MediaController(App.getContext());
        surfaceView.setMediaController(mMediaController);
        mGestureDetector = new GestureDetector(App.getContext(), new MyGestureDetector());
        return view;
    }


    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;

        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    /** 定时隐藏 */
    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            operationVolumeBrightness.setVisibility(View.GONE);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        /** 双击 */
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM) {
                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
            } else {
                mLayout++;
            }
            if (surfaceView != null)
                surfaceView.setVideoLayout(mLayout, 0);
            return true;
        }

        /** 滑动 */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float oX = e1.getX();
            float oY = e1.getY();
            int y = (int) e2.getRawY();
            int width=MainActivity.width;
            int height=MainActivity.height;
            if (oX > width * 4.0 / 5.0) {
                onVolume((oY - y) / height);//右滑动
            }else if (oX < width / 5.0) {
                onBrightness((oY - y) / height);//左滑动
            }
            return super.onScroll(e1, e2, distanceX, distanceY);

        }
    }

    /**
     * 滑动改变亮度
     *
     * @param m
     */
    private void onBrightness(float m) {
        if (mBrightness < 0) {
            mBrightness = getActivity().getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            // 显示
            operationBg.setImageResource(R.drawable.video_brightness_bg);
            operationVolumeBrightness.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = getActivity().getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + m;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getActivity().getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = operationPercent.getLayoutParams();
        lp.width = (int) (operationFull.getLayoutParams().width * lpa.screenBrightness);
        operationPercent.setLayoutParams(lp);

    }

    /**
     * 滑动改变声音大小
     *
     * @param m
     */
    private void onVolume(float m) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            // 显示
            operationBg.setImageResource(R.drawable.video_volumn_bg);
            operationVolumeBrightness.setVisibility(View.VISIBLE);
        }

        int index = (int) (m * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        // 变更声音
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        // 变更进度条
        ViewGroup.LayoutParams lp = operationPercent.getLayoutParams();
        lp.width = operationFull.getLayoutParams().width
                * index / mMaxVolume;
        operationPercent.setLayoutParams(lp);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(surfaceView!=null)
            surfaceView.setVideoLayout(mLayout,0);
        super.onConfigurationChanged(newConfig);
    }
}
