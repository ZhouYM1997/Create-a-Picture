package com.ych.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ych.R;

public class change_info extends AppCompatActivity {
    private EditText nicheng,zhanghao,xiugaimima,phonenum,mail,xingbie;
    private Button save_btn;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        nicheng=findViewById(R.id.nicheng);
        zhanghao=findViewById(R.id.zhanghao);
        xiugaimima=findViewById(R.id.xiugaimima);
        phonenum=findViewById(R.id.phonenum);
        mail=findViewById(R.id.mail);
        xingbie=findViewById(R.id.xingbie);
        save_btn=findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 sp=getSharedPreferences("userinfo",Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString("Value1",nicheng.getText().toString().trim());
                edit.putString("Value2",zhanghao.getText().toString().trim());
                edit.putString("Value3",xiugaimima.getText().toString().trim());
                edit.putString("Value4",xingbie.getText().toString().trim());
                edit.putString("Value5",phonenum.getText().toString().trim());
                edit.putString("Value6",mail.getText().toString().trim());
                edit.apply();
                String value1 = sp.getString("Value1","Null");
                nicheng.setText(value1);
                String value2 = sp.getString("Value2","Null");
                zhanghao.setText(value2);
                String value3 = sp.getString("Value3","Null");
                xiugaimima.setText(value3);
                String value4 = sp.getString("Value4","Null");
                xingbie.setText(value4);
                String value5 = sp.getString("Value5","Null");
                phonenum.setText(value5);
                String value6 = sp.getString("Value6","Null");
                mail.setText(value6);
                setResult(44444);



            }
        });
    }






}
