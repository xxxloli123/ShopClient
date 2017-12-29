package com.xxx.skynet.sqlSave;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;

import static com.xxx.skynet.tool.ClassInfoHelper.getClassShortName;

/**
 * Created by Administrator on 2017/11/2.
 */

public class ReflectSqliteHelper extends SqliteHelper {
    private static final String TAG = "ReflectSqliteHelper";
    public ReflectSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        /*try {
            context.deleteDatabase(getClassShortName(Class.forName(name)));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sql = new StringBuilder("create table ");
        try {
            Class cls = Class.forName(getDatabaseName());
            sql.append(getClassShortName(cls)).append("(id int primary key autoincrement,").append(getMemberNames(cls));//append("(id int primary key autoincrement,name varchar(32),phone varchar(11),account varchar(32),password varchar(32))");
            Log.i(TAG,"onCreate创建sqlite->"+sql);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        db.execSQL(sql.toString());
    }
    private String getMemberNames(Class<?> cls){
        StringBuilder sb = new StringBuilder();
        Field[] fields = cls.getDeclaredFields();
        if(null != fields)
            for(Field field :fields)
                sb.append(field.getName()).append(" ").append(getType(field.getType())).append(",");
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
