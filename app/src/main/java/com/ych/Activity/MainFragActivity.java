package com.ych.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ych.Fragment.LeftFragment;
import com.ych.Fragment.MiddleFragment;
import com.ych.Fragment.RightFragment;
import com.ych.R;
import com.ych.unit.ConfigUtil;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class MainFragActivity extends AppCompatActivity {
    ViewPager viewPager;
    RadioGroup radioGroup;
    private int[] rbs = {R.id.rb_home,R.id.rb_person,R.id.rb_person1};
    private List<Fragment> fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (ConfigUtil.needShowGuide(this))
          //  {
            startActivity(new Intent(this, WelcomeGuideActivity.class));
       // }
        setContentView(R.layout.activity_main_frag);
        viewPager = findViewById(R.id.vp);
        radioGroup = findViewById(R.id.rg_click);
        fragments = new ArrayList<>();
        LeftFragment leftFragment=new LeftFragment();
        RightFragment rightFragment = new RightFragment();
        MiddleFragment middleFragment = new MiddleFragment();
        fragments.add(middleFragment);
        fragments.add(leftFragment);
        fragments.add(rightFragment);


        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),fragments));
        viewPager.setOffscreenPageLimit(3);//设置预加载数量

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int j) {
                for (int i=0 ; i < rbs.length ;i++){
                    if (rbs[i] != j)
                        continue;
                    viewPager.setCurrentItem(i);//点击按钮，对应的页面改变

                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {//滑页的时候，按钮跟着变
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                radioGroup.check(rbs[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        radioGroup.check(rbs[0]);//设置初始值

    }


}
