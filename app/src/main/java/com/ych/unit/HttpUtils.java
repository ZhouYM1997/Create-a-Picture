package com.ych.unit;

import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;


/**

 */
public class HttpUtils {

    /**
     * 发送消息到服务器
     *
     * @param message
     *            ：发送的消息
     * @return：消息对象
     */
    public static ChatMessage sendMessage(String message) {
        ChatMessage chatMessage = new ChatMessage();
        String gsonResult = doGet(message);
        Gson gson = new Gson();
        Result result = null;
        Log.i("message", message);
        Log.i("gsonResult", gsonResult);
        if (gsonResult != null) {
            try {
                result = gson.fromJson(gsonResult, Result.class);
                chatMessage.setMessage(result.getText());
            } catch (Exception e) {
                e.printStackTrace();
                chatMessage.setMessage("服务器繁忙");
            }
        }
        chatMessage.setData(new Date());
        chatMessage.setType(ChatMessage.Type.INCOUNT);
        return chatMessage;
    }

    /**

     */

    public static String doGet(String message) {
        String result = "";
        String url = setParmat(message);
        System.out.println("------------url = " + url);
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {

            URL urls = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urls
                    .openConnection();
            connection.setReadTimeout(5 * 1000);
            connection.setConnectTimeout(5 * 1000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            Log.i("11111111", String.valueOf(urls));
            int code = connection.getResponseCode();
            if (code == 200) {

                is = connection.getInputStream(); // 得到网络返回的输入流

            } else {
                Log.i("errorhuawei", String.valueOf(urls));
                is = connection.getErrorStream(); // 得到网络返回的输入流

            }

            is = connection.getInputStream();
            baos = new ByteArrayOutputStream();
            Log.i("2222222bbbbb", String.valueOf(urls));
            int len = -1;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                baos.write(buff, 0, len);
            }
            baos.flush();
            result = new String(baos.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("werw",e.toString());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 设置参数
     *


     * @return ： url
     */
    private static String setParmat(String message) {
        String url = "";
        try {
            url = Config.URL_KEY + "?" + "key=" + Config.APP_KEY + "&info="
                    + URLEncoder.encode(message, "UTF-8");
            Log.i("qqqqqqqqqqqqqqqqqqq",url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}