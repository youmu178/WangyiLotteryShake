package com.youmu.wangyilotteryshake;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends ActionBarActivity implements ShakeListener.OnShakeListener, ShakeLayout.AnimListener {
//    @InjectView(R.id.shake_tv)
//    TextView shakeTv;
    @InjectView(R.id.shake_layout)
    ShakeLayout shakeLayout;
    private SoundPool soundPool;

    private ShakeListener shakeListener;
    private Handler mHandler = new Handler();
    private HashMap<Integer, Integer> aduioMap = new HashMap<Integer, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initAudio();

        shakeListener = new ShakeListener(this);
        shakeListener.setOnShakeListener(this);
        shakeLayout.setAnimListener(this);
    }


    private void initAudio() {
//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
//            SoundPool.Builder builder = new SoundPool.Builder();
//        }

        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        aduioMap.put(1, soundPool.load(this, R.raw.rotate, 1));
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            }
        });
    }


    @Override
    public void onShake() {
        shakeListener.stop();
        play();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                shakeLayout.anim();
                shakeListener.start();
            }
        }, 600);
    }


    public void play() {
        if (soundPool != null) {
            soundPool.play(aduioMap.get(1), 1.0f, 1.0f, 0, 0, 1);
        }
    }

    public void stop() {
        if (soundPool != null) {
            soundPool.release();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_local) {
            shakeLayout.setModel(ShakeLayout.LOCAL);
            return true;
        } else if (id == R.id.action_screen) {
            shakeLayout.setModel(ShakeLayout.SCREEN);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAnimFinish(int count) {
        Toast.makeText(MainActivity.this, "和值为：" + count, Toast.LENGTH_SHORT).show();
    }
}
