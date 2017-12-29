package com.xxx.skynet.sqlSave;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.xxx.skynet.tool.ClassInfoHelper.getClassShortName;

/**
 * Created by Administrator on 2017/10/20.
 * 反射创建sqlite表
 * Android 操作SQLite数据库（初步）-在程序中删除数据库 http://blog.csdn.net/qiuzhping/article/details/17660111
 */

public abstract class SqliteHelper extends SQLiteOpenHelper{
    private static final String TAG = "SqliteHelper";
    private static final int VERSION = 1;
    private static final String types[] = {getClassShortName(byte.class),getClassShortName(short.class),getClassShortName(int.class),getClassShortName(long.class),
            getClassShortName(Byte.class),getClassShortName(Short.class),getClassShortName(Integer.class),getClassShortName(Long.class),
            getClassShortName(float.class),getClassShortName(long.class), getClassShortName(Float.class),getClassShortName(Double.class),
            getClassShortName(String.class)};
    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG,"onUpgrade更新sqlite->");
    }
    //Android SQLite如何判断表是否存在 http://blog.csdn.net/feather_wch/article/details/51046180
    protected boolean isExist(String tableName){
        Cursor cursor = getReadableDatabase().rawQuery("select count(*) as c from sqlite_master where type ='table' and name ='"+tableName.trim()+"'",null);
        if(cursor.moveToFirst())
            if(0 < cursor.getInt(0))
                return true;
        return false;
    }
    //SQL 数据类型 http://www.w3school.com.cn/sql/sql_datatypes.asp
    public static String getType(Class<?> cls){
        String type = getClassShortName(cls);
        if(type.endsWith(types[0])||type.endsWith(types[4]))
            return "byte";
        else if(type.endsWith(types[1])||type.endsWith(types[5]))
            return "short";
        else if (type.equals(types[2])||type.endsWith(types[6]))
            return "int(16)";
        else if(type.equals(types[3])||type.endsWith(types[7]))
            return "long(32)";
        else if(type.equals(types[8])||type.endsWith(types[10]))
            return "single(32)";
        else if (type.equals(types[9])||type.endsWith(types[11]))
            return"double(64)";
        else if (type.equals(types[12]))
            return"varchar(64)";
        return  null;
    }
    public long queryCounts(String tableName){
        if(null != tableName &&! "".equals(tableName)) {
            Cursor cursor = getWritableDatabase().query(tableName, new String[]{"count(*)"}, null, null, null, null, null);
            if (null != cursor && 0< cursor.getCount() && cursor.moveToNext())
                return cursor.getLong(0);
        }
        return 0;
    }
    public void removeSql(String tableName){//删除表
        if(null != tableName && "".equals(tableName))
            getWritableDatabase().execSQL("drop table "+tableName);
    }
    public void emptySql(String tableName){//清空表
        if(null != tableName && "".equals(tableName))
            getWritableDatabase().execSQL("delete from table "+tableName);
    }
}
