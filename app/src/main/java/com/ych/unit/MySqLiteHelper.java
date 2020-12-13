package com.ych.unit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySqLiteHelper extends SQLiteOpenHelper {
    SQLiteDatabase sqLiteDatabase;
    String create_TABLE_sql="create table person(id INTEGER PRIMARY KEY AUTOINCREMENT," +"username varchar(30),"+"userpassword varchar(30))";
    public MySqLiteHelper(Context context){
        super(context,"XFF.db",null,1);//数据库名 super(context, "testbold.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_TABLE_sql);//创建数据库
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addPerson(Person person){
        sqLiteDatabase = getWritableDatabase();//打开可读写的已有数据库
        String sql = "insert into person(id,username,userpassword) values(null,?,?)";
        sqLiteDatabase.execSQL(sql,new Object[]{person.getUsernname(),person.getUserpassword()});
        sqLiteDatabase.close();
    }
    public Boolean checked(Person person) {//登陆验证
        Boolean a=false;

        sqLiteDatabase = getReadableDatabase();
        String string = person.getUsernname();

        String string1=person.getUserpassword();
        String sql = "select * from person ";
        Cursor cursor = sqLiteDatabase.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String userpassword = cursor.getString(cursor.getColumnIndex("userpassword"));
            if (username.equals(string))
            {
                if (userpassword.equals(string1))
                {
                    a=true;
                    Log.i("setUsernnamesetame", String.valueOf(a));
                }
            }
        }     Log.i("setUsernnamesetame", String.valueOf(a));return a;
    }
}
