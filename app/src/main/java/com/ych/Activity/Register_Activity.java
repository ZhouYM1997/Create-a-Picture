package com.ych.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ych.R;
import  com.ych.unit.MD5;
import  com.ych.unit.MySqLiteHelper;
import  com.ych.unit.Person;
public class Register_Activity extends AppCompatActivity {
    Button   button_reg;
    EditText editText1;
    EditText editText2;
    MySqLiteHelper myPersonSqLiteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        button_reg=findViewById(R.id.reg_but);
        editText1=findViewById(R.id.reg_user);
        editText2=findViewById(R.id.reg_pass);

        myPersonSqLiteHelper = new MySqLiteHelper(this);
        button_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MD5 md5=new MD5();
                String string1= editText1.getText().toString();
                String string2= editText2.getText().toString();
                Person person=new Person();
                String string3=md5.getPassWordMD5_name(string2);
                person.setUsernname(string1);
                person.setUserpassword(string3);
                myPersonSqLiteHelper.addPerson(person);
                finish();
            }
        });

    }
}

