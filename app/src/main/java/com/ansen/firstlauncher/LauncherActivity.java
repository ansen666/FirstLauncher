package com.ansen.firstlauncher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * 启动页
 * Created by  ansen
 * Create Time 2017-07-14
 */
public class LauncherActivity extends AppCompatActivity {
    public static final String  FIRST_LAUNCHER="first_launcher";//是否第一次启动
    private final long waitTime = 1000;//1秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        start();
    }

    public void start(){
        new Handler().postDelayed(new Runnable(){
            public void run() {
                Intent intent;
                if(isFirstLauncher()){//为true第二次启动  因为第一次启动的时候我们会把FIRST_LAUNCHER的值变为true
                    intent=new Intent(LauncherActivity.this,MainActivity.class);
                }else{//第一次启动
                    intent=new Intent(LauncherActivity.this,FirstLauncherActivity.class);
                }
                startActivity(intent);

                finish();
            }
        },waitTime);
    }


    /**
     * false:第一次启动  true:第二次启动  这里不能用Activity的getPreferences方法，因为我们需要多个Activity用一个SharedPreference对象。所以调用getSharedPreferences方法。
     * @return
     */
    public boolean isFirstLauncher(){
        SharedPreferences sp=getSharedPreferences("ansen",Context.MODE_PRIVATE);
        return sp.getBoolean(FIRST_LAUNCHER,false);
    }
}
