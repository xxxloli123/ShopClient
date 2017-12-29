package com.zsh.shopclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zsh.shopclient.widget.AttributeTagView;

/**
 * Created by Administrator on 2017/12/24.
 */

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        AttributeTagView attributeTagView = findViewById(R.id.attributeTag);
        String[] tag = new String[]{"大红色","橙色","黄色","天蓝","深海红斑鱼色","葡萄紫色","火山岩洞口石黑色","火山灰色","不规则螺旋彩色条纹漩涡色",
        "枣色","橙色","红色","绿色","青色","蓝色","紫色"};
        for(int index = 0;index<tag.length;index++){
            TextView textView = new TextView(this);
            textView.setPadding(8,4,8,4);
            textView.setBackgroundResource(R.drawable.shape_rectangle_contour_gray);
            textView.setText(tag[index]);
            attributeTagView.addView(textView);
        }
    }
}
