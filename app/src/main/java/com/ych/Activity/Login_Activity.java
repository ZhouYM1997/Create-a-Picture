package com.ych.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ych.MainActivity;


import com.ych.R;
import  com.ych.unit.MD5;
import  com.ych.unit.MySqLiteHelper;
import  com.ych.unit.Person;

public class Login_Activity extends AppCompatActivity {
    SharedPreferences sp;
    MySqLiteHelper myPersonSqLiteHelper;
    String userNameValue,passwordValue;
    EditText userName, password;
    CheckBox jzmm;
    CheckBox auto;
    Button btn_login;
    Button btn_regisiter;
    ImageButton btnQuit;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myPersonSqLiteHelper = new MySqLiteHelper(this);//若该数据库已创建，就是将这个对象与该数据库连接，可调用方法操作这个数据库

        sp=getSharedPreferences("Myshare",MODE_PRIVATE);
        //去除标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_login_);

        //获得实例对象

        userName = (EditText) findViewById(R.id.et_zh);
        password = (EditText) findViewById(R.id.et_mima);
        jzmm= (CheckBox) findViewById(R.id.cb_mima);
        auto=findViewById(R.id.cb_auto);

        btn_login = (Button) findViewById(R.id.btn_login);
        btnQuit = (ImageButton)findViewById(R.id.img_btn);
        btn_regisiter=findViewById(R.id.btn_reg);

        btn_regisiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Login_Activity.this,Register_Activity.class);
                startActivity(intent);
            }
        });

        //判断记住密码多选框的状态
        if(sp.getBoolean("ISCHECK", false))
        {
            //设置默认是记录密码状态

            userName.setText(sp.getString("USER_NAME", ""));
            password.setText(sp.getString("PASSWORD", ""));
            //判断自动登陆多选框状态

        }
        //判断自动登录
        if(sp.getBoolean("AUTO_ISCHECK", false))
        {
            Intent intent = new Intent(Login_Activity.this,MainFragActivity.class);
            setResult(2222);
            //startActivity(intent);
            finish();

        }
        btn_login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Person person=new Person();
                userNameValue = userName.getText().toString();
                passwordValue = password.getText().toString();
                MD5 md5=new MD5();
                String string=md5.getPassWordMD5_name(passwordValue);
                person.setUsernname(userNameValue);
                person.setUserpassword(string);
                Boolean a;
                a=myPersonSqLiteHelper.checked(person);

                if(a)
                {   Log.i("setUsernname", String.valueOf(a));
                    Toast.makeText(Login_Activity.this,"登录成功", Toast.LENGTH_SHORT).show();
                    //登录成功和记住密码框为选中状态才保存用户信息
                    if(jzmm.isChecked())
                    {
                        //记住用户名、密码、
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("USER_NAME", userNameValue);
                        editor.putString("PASSWORD",passwordValue);
                        editor.commit();
                    }
                    setResult(2222);
                    finish();

                }else{
                    userName.setText(null);
                    password.setText(null);
                    Toast.makeText(Login_Activity.this,"该帐号不存在", Toast.LENGTH_LONG).show();

                }

            }
        });

        //监听记住密码多选框按钮事件
        jzmm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (jzmm.isChecked()) {

                    System.out.println("记住密码已选中");
                    sp.edit().putBoolean("ISCHECK", true).commit();

                }else {

                    System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("ISCHECK", false).commit();

                }

            }
        });

        auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (auto.isChecked())
                {System.out.println("自动登录已选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
                }else {

                    System.out.println("自动登录没有选中");
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();

                }
            }
        });

        btnQuit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}