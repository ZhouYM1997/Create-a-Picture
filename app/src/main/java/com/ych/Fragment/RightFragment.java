package com.ych.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ych.Activity.Login_Activity;
import com.ych.Activity.Register_Activity;
import com.ych.Activity.SettingActivity;
import com.ych.Activity.about_us;
import com.ych.Activity.change_info;
import com.ych.R;


public class RightFragment extends Fragment implements View.OnClickListener{

    private  EditText editText;
    private WebView webView;
    private  Button button;
    Button denglu;
    Button regsister;
    Button esc;
    Button change_tx;
    View view1;
    LinearLayout after_denglu;
    LinearLayout esc_btn;
    LinearLayout before_denglu;
    LinearLayout update_version;
    LinearLayout linearLayout1;
    LinearLayout linearLayout2;
    LinearLayout linearLayout3;
    private static final String TAG = "Person_information";
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: ");
        final View view = inflater.inflate(R.layout.fragment_right, container, false);
        view1 = view;
//            before_denglu.setVisibility(View.GONE);
//            after_denglu.setVisibility(View.VISIBLE);
//            esc_btn.setVisibility(View.VISIBLE);
        linearLayout2 = view.findViewById(R.id.setting);
        linearLayout3 = view.findViewById(R.id.about_us);


        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(),SettingActivity.class);
                startActivity(intent);
            }
        });
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(),about_us.class);
                startActivity(intent);
            }
        });
        denglu=view.findViewById(R.id.denglu);
        change_tx=view.findViewById(R.id.change_tx);
        regsister=view.findViewById(R.id.regsister);
        esc=view.findViewById(R.id.esc);
        esc.setOnClickListener(this);
        after_denglu=(LinearLayout)view.findViewById(R.id.after_denglu);
        esc_btn=(LinearLayout)view.findViewById(R.id.esc_btn);
        before_denglu=(LinearLayout)view.findViewById(R.id.before_denglu) ;
        update_version=(LinearLayout)view.findViewById(R.id.update_version) ;
        update_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"已经是最新版本",Toast.LENGTH_LONG).show();
            }
        });
        change_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(view.getContext(),change_info.class);
                startActivityForResult(intent,2222);
            }
        });
        denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(view.getContext(),Login_Activity.class);
                startActivityForResult(intent,1111);

            }
        });
        regsister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(view.getContext(),Register_Activity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode , data);
        Log.i("1111111111111","qqqqqqqqqqqqqqqqqq");
        if(requestCode==1111&&resultCode==2222) {
            before_denglu.setVisibility(View.GONE);
            after_denglu.setVisibility(View.VISIBLE);
            esc_btn.setVisibility(View.VISIBLE);
        }
        else
        {
            Toast.makeText(view1.getContext(),"您没有登录，请登录",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.esc:
                before_denglu.setVisibility(View.VISIBLE);
                after_denglu.setVisibility(View.GONE);
                esc_btn.setVisibility(View.GONE);
                break;
                default:
                    break;
        }

    }
}
