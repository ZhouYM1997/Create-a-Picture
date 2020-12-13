package com.ych.unit;
import android.graphics.Bitmap;

import java.io.Serializable;

public class Person implements Serializable {//这是一个用户类
    private  int id;
    private String usernname;
    private String userpassword;
    private Bitmap bitmap;
    //http://img4.duitang.com/uploads/item/201306/26/20130626090600_AE4Pn.thumb.700_0.jpeg
    public void setId(int id) {
        this.id = id;
    }

    public void setUsernname(String usernname) {
        this.usernname = usernname;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public int getId() {
        return id;
    }

    public String getUsernname() {
        return usernname;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}