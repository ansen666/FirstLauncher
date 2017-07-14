package com.ansen.firstlauncher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  ansen
 * Create Time 2017-07-14
 */
public class FirstLauncherActivity extends AppCompatActivity{
    private final int MAX_POINT = 3;
    private ViewPager viewPager;
    private List<View> mImageViews = new ArrayList<>();//要显示图片
    private List<ImageView> tips=new ArrayList<>();// 要显示点点点
    private ViewGroup group;

    private EdgeEffectCompat rightEdge;

    private TextView tvGotoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launcher);

        group = (ViewGroup) findViewById(R.id.viewGroup);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tvGotoMain= (TextView) findViewById(R.id.tv_goto_main);

        try {
            Field rightEdgeField = viewPager.getClass().getDeclaredField("mRightEdge");
            if (rightEdgeField != null){
                rightEdgeField.setAccessible(true);
                rightEdge = (EdgeEffectCompat) rightEdgeField.get(viewPager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 将图片装载到数组中
        for (int i = 0; i < MAX_POINT; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (i == 0) {
                imageView.setImageResource(R.mipmap.bg_launcher_one);
                mImageViews.add(imageView);
            } else if (i == 1) {
                imageView.setImageResource(R.mipmap.bg_launcher_two);
                mImageViews.add(imageView);
            } else if (i == 2) {
                imageView.setImageResource(R.mipmap.bg_launcher_three);
                mImageViews.add(imageView);
            }
        }

        // 将点点加入到ViewGroup中
        for (int i = 0; i < MAX_POINT; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            if (i == 0) {
                imageView.setBackgroundResource(R.mipmap.icon_first_launcher_page_select_one);
            } else {
                imageView.setBackgroundResource(R.mipmap.icon_first_launcher_page_normal);
            }
            tips.add(imageView);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            group.addView(imageView,layoutParams);
        }

        // 设置Adapter
        viewPager.setAdapter(new PreviewImageAdapter());
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(onPageChangeListener);// 设置监听，主要是设置点点的背景
        viewPager.setCurrentItem(0);

        tvGotoMain.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_goto_main:
                    gotoMain();
                    break;
            }
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position){
            if(position==2){
                tvGotoMain.setVisibility(View.VISIBLE);
            }else{
                tvGotoMain.setVisibility(View.INVISIBLE);
            }
            setImageBackground(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(rightEdge!=null&&!rightEdge.isFinished()){//到了最后一张并且还继续拖动，出现蓝色限制边条了
                gotoMain();
            }
        }
    };

    /**
     * 设置选中的tip的背景
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.size(); i++) {
            if (i == selectItems) {
                if(i==0){
                    tips.get(i).setBackgroundResource(R.mipmap.icon_first_launcher_page_select_one);
                }else if(i==1){
                    tips.get(i).setBackgroundResource(R.mipmap.icon_first_launcher_page_select_two);
                }else if(i==2){
                    tips.get(i).setBackgroundResource(R.mipmap.icon_first_launcher_page_select_three);
                }
            } else {
                tips.get(i).setBackgroundResource(R.mipmap.icon_first_launcher_page_normal);
            }
        }
    }

    public class PreviewImageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            if (position < mImageViews.size()) {
                ((ViewPager) container).removeView(mImageViews.get(position));
            }
        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(mImageViews.get(position));
            return mImageViews.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    /**
     * 第一次启动执行完成  值设置成true
     */
    public void setFirstLauncherBoolean(){
        SharedPreferences sp=getSharedPreferences("ansen",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=sp.edit();
        edit.putBoolean(LauncherActivity.FIRST_LAUNCHER,true);
        edit.commit();
    }

    /**
     * 跳转到首页
     */
    private void gotoMain(){
//        setFirstLauncherBoolean();

        Intent intent=new Intent(FirstLauncherActivity.this,MainActivity.class);
        startActivity(intent);

        finish();
    }
}
