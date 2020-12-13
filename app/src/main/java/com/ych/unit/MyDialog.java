package com.ych.unit;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ych.Fragment.LeftFragment;
import com.ych.Fragment.MiddleFragment;
import com.ych.R;

public class MyDialog {
    //点击确认按钮回调接口
    public interface OnConfirmListener {
        public void onConfirmClick();
    }

    /**
     * @Title: show
     * @Description: 显示Dialog
     * @param activity
     * @param content
     *            提示内容
     * @param confirmListener
     *            void
     * @throws
     */
    public static void show(MiddleFragment activity, String content,
                            final OnConfirmListener confirmListener) {
        // 加载布局文件
        View view = View.inflate(activity.getContext(), R.layout.dialog_normal_layout, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);

        if (!StringUtil.isNullOrEmpty(content)) {
            text.setText(content);
        }

        // 创建Dialog
        final AlertDialog dialog = new AlertDialog.Builder(activity.getContext()).create();
        dialog.setCancelable(false);// 设置点击dialog以外区域不取消Dialog
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().setLayout(StringUtil.getWidth((AppCompatActivity) activity.getContext()) / 3 * 2,
                LinearLayout.LayoutParams.WRAP_CONTENT);//设置弹出框宽度为屏幕宽度的三分之二

        // 确定
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                confirmListener.onConfirmClick();
            }
        });
        // 取消
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static void show(LeftFragment activity, String content,
                            final OnConfirmListener confirmListener) {
        // 加载布局文件
        View view = View.inflate(activity.getContext(), R.layout.dialog_normal_layout, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        TextView confirm = (TextView) view.findViewById(R.id.confirm);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);

        if (!StringUtil.isNullOrEmpty(content)) {
            text.setText(content);
        }

        // 创建Dialog
        final AlertDialog dialog = new AlertDialog.Builder(activity.getContext()).create();
        dialog.setCancelable(false);// 设置点击dialog以外区域不取消Dialog
        dialog.show();
        dialog.setContentView(view);
        dialog.getWindow().setLayout(StringUtil.getWidth((AppCompatActivity) activity.getContext()) / 3 * 2,
                LinearLayout.LayoutParams.WRAP_CONTENT);//设置弹出框宽度为屏幕宽度的三分之二

        // 确定
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                confirmListener.onConfirmClick();
            }
        });
        // 取消
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
