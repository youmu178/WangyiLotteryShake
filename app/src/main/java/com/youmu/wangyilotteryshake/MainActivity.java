package com.youmu.wangyilotteryshake;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends ActionBarActivity implements ShakeListener.OnShakeListener {
    @InjectView(R.id.shake_tv)
    TextView shakeTv;
    @InjectView(R.id.shake_img1)
    ImageView shakeImg1;
    @InjectView(R.id.shake_img2)
    ImageView shakeImg2;
    @InjectView(R.id.shake_img3)
    ImageView shakeImg3;
    @InjectView(R.id.shake_layout)
    LinearLayout shakeLayout;
    private SoundPool soundPool;

    private ShakeListener shakeListener;
    private Handler mHandler = new Handler();
    private HashMap<Integer, Integer> aduioMap = new HashMap<Integer, Integer>();
    private Random mRandom = new Random();
    private int mImgs[] = {R.mipmap.dice1, R.mipmap.dice2, R.mipmap.dice3, R.mipmap.dice4, R.mipmap.dice5, R.mipmap.dice6};
    private int mImgsAnim[] = {R.mipmap.dice_f1, R.mipmap.dice_f2, R.mipmap.dice_f3, R.mipmap.dice_f4};
    private int mCount;
    private boolean isStopAnim = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initAudio();

        shakeListener = new ShakeListener(this);
        shakeListener.setOnShakeListener(this);
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

    private void randomImg(ImageView imageView) {
        int num = mRandom.nextInt(6);

        mCount += (num + 1);
        imageView.setImageResource(mImgs[num]);
    }

    private void randomImgAnim(final ImageView imageView) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStopAnim) {
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            int num = mRandom.nextInt(4);
                            imageView.setImageResource(mImgsAnim[num]);
                        }
                    });
                    SystemClock.sleep(80);
                }
            }
        }).start();

    }

    @Override
    public void onShake() {
        shakeListener.stop();
        play();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                anim();
//                showRadome();
                shakeListener.start();
            }
        }, 600);
    }

    private void anim() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 1.0f);
        alphaAnimation.setDuration(500);
        shakeLayout.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isStopAnim = false;
                Log.i("youzh", "onAnimationStart: " + isStopAnim);
                randomImgAnim(shakeImg1);
                randomImgAnim(shakeImg2);
                randomImgAnim(shakeImg3);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isStopAnim = true;
                Log.i("youzh", "onAnimationEnd: " + isStopAnim);
                shakeLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        showRadome();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void showRadome() {
        randomImg(shakeImg1);
        randomImg(shakeImg2);
        randomImg(shakeImg3);
        Toast.makeText(MainActivity.this, "总数为：" + mCount, Toast.LENGTH_SHORT).show();
        mCount = 0;
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
