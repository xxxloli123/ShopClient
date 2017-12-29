package com.xxx.skynet.sqlSave;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/11/2.
 * Android中自带的SQLite数据库 http://blog.csdn.net/quietshake/article/details/52852866
 * android之存储篇_SQLite数据库_让你彻底学会SQLite的使用 http://blog.csdn.net/jason0539/article/details/10248457
 * Android 操作SQLite基本用法 http://blog.csdn.net/codeeer/article/details/30237597/
 * Android SQLite详解 http://www.jianshu.com/p/5c33be6ce89d
 * 安卓sqlite之增删改查（一）http://blog.csdn.net/liu540885284/article/details/47430807
 */

public class TypeSqliteHelper extends SqliteHelper {
    private static final String TAG = "TypeSqliteHelper";
    private List<String> fields;

    public TypeSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, List<String> fields) {
        super(context, name, factory, version);//name == getDatabaseName();
        this.fields = fields;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(null != fields)
            db.execSQL(onCreateTable(fields.get(0)));
    }
    private String onCreateTable(String tableName){
        StringBuilder sql = new StringBuilder("create table if not exists ");
        sql.append(tableName).append("(keyId integer primary key autoincrement,");//append("(id int primary key autoincrement,name varchar(32),phone varchar(11),account varchar(32),password varchar(32))");
        for (int index = 1; index < fields.size(); index++)
            sql.append(fields.get(index)).append(" ").append("varchar(64)").append(",");
        sql.deleteCharAt(sql.length() - 1).append(");");
        Log.i(TAG, "onCreate创建sqlite->" + sql);
        return sql.toString();
    }
    public void insert(String tableName,HashMap<String,Object> hashMap){//方式1.ContentValues插入法
        if(null != tableName &&! "".equals(tableName) && null != hashMap && 0< hashMap.size()) {
            ContentValues contentValues = new ContentValues();
            Iterator iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()) {//hashMap这种遍历发是一样的
                String key = (String) iterator.next();
                contentValues.put(key, hashMap.get(key).toString());
            }
            Log.i(TAG, "insert->" + contentValues);
            if(!isExist(tableName)) {
                getWritableDatabase().execSQL(onCreateTable(tableName));
                getWritableDatabase().insert(tableName, null, contentValues);//参数2  空列的默认值
            }else if (0 == queryCounts(tableName)) //空数据表查询报错android.database.sqlite.SQLiteException: near ",": syntax error (code 1): , while compiling:
                getWritableDatabase().insert(tableName, null, contentValues);
        }
    }
    public void insertSql(String tableName,HashMap<String,Object> hashMap){//方式2.sql语句插入法
        if(null != tableName &&! "".equals(tableName) && null != hashMap && 0< hashMap.size())
            if(0< rawQuery(tableName,hashMap).size()) {
                StringBuilder sqlKey = new StringBuilder("insert into ");
                StringBuilder sqlValue = new StringBuilder("values(");
                sqlKey.append(tableName).append("(");
                Iterator iterator = hashMap.keySet().iterator();
                while (iterator.hasNext()) {//hashMap这种遍历发是一样的
                    String key = (String) iterator.next();
                    sqlKey.append(key).append(",");
                    sqlValue.append("'").append(hashMap.get(key)).append("',");
                }
                sqlKey.replace(sqlKey.length() - 1, sqlKey.length(), ")");
                sqlValue.deleteCharAt(sqlValue.length() - 1).append(")");
                Log.i(TAG, "insert->" + sqlKey + "\n" + sqlValue);
                SQLiteDatabase db = getWritableDatabase();
                db.execSQL(sqlKey.append(" ").append(sqlValue).toString().trim());
            }
    }
    public void delete(String tableName,HashMap<String,Object> hashMap){
        if(null != tableName &&! "".equals(tableName) && null != hashMap && 0< hashMap.size()) {
            Iterator iterator = hashMap.keySet().iterator();
            StringBuilder keys = new StringBuilder();
            String[] values = new String[hashMap.size()];
            int index = 0;
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                keys.append(key).append("=?");
                values[index++] = hashMap.get(key).toString();
            }
            getWritableDatabase().delete(tableName, keys.toString(), values);
        }
    }
    public void deleteSql(String tableName,HashMap<String,Object> hashMap){
        if(null != tableName && !"".equals(tableName) && null != hashMap && 0< hashMap.size()) {
            Iterator iterator = hashMap.keySet().iterator();
            StringBuilder sb = new StringBuilder();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                sb.append(key).append("='").append(hashMap.get(key)).append("',");
            }
            getWritableDatabase().execSQL("delete from " + tableName + " where " + sb.deleteCharAt(sb.length() - 1));
        }
    }
    public void update(String tableName,String whereKey,String whereValue,HashMap<String,Object> hashMap){
        if(null != tableName && !"".equals(tableName) && null != whereKey && "".equals(whereKey)&& null != hashMap && 0< hashMap.size()) {
            ContentValues values = new ContentValues();
            Iterator iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                values.put(key, hashMap.get(key).toString());
            }
            getWritableDatabase().update(tableName, values, whereKey, new String[]{whereValue});
        }
    }
    public void updateSql(String tableName,String whereKey,String whereValue,HashMap<String,Object> hashMap){
        if(null != tableName &&! "".equals(tableName) && null != whereKey && "".equals(whereKey)&& null != hashMap && 0< hashMap.size()) {
            StringBuilder sql = new StringBuilder("update ");
            sql.append(tableName).append("set ");
            Iterator iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                sql.append(key).append("=").append(hashMap.get(key)).append(",");
            }
            sql.deleteCharAt(sql.length() - 1).append(" where ").append(whereKey).append("=").append(whereValue);
            getWritableDatabase().execSQL(sql.toString());
        }
    }
    //field查询需要获取的字段值获取全部字段传递null,keys查找的字段对应的values的值
    public List<List<String>> query(String tableName, String[] field, HashMap<String,Object> select){
        String where = null;
        if(!isExist(tableName))return null;
        if(null != select && 0 < select.size()) {
            StringBuilder whereSb = new StringBuilder();
            Iterator iterator = select.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                whereSb.append(key).append("='").append(select.get(key)).append("',");
            }
            where = whereSb.deleteCharAt(whereSb.length() -1).toString().trim();
            Log.i(TAG, "query->" + where);
        }
        List<List<String>> result = new ArrayList<>();
        if(null != tableName &&! "".equals(tableName)) {
            Cursor cursor = getWritableDatabase().query(tableName, field, where, null, null, null, null);
            while (cursor.moveToNext()) {
                List<String> itemData = new ArrayList<>();
                for (int i = 0; i < field.length; i++)
                    itemData.add(cursor.getString(i));
                result.add(itemData);
            }
        }
        return result;
    }
    public List<List<String>> rawQuery(String tableName,HashMap<String,Object> hashMap){
        String where = "";
        String[] values = null;
        if(null != tableName &&! "".equals(tableName) && null != hashMap && 0 < hashMap.size()) {
            StringBuilder whereSb = new StringBuilder(" where ");
            Iterator iterator = hashMap.keySet().iterator();
            values = new String[hashMap.size()];
            int index = 0;
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                whereSb.append(key).append("=?,");//循环结束去掉,
                values[index++] = hashMap.get(key).toString();
            }
            where = whereSb.deleteCharAt(whereSb.length()-1).toString().trim();
        }
        //sqlite中如何查询某一特定行的所有数据 http://bbs.csdn.net/topics/390847402
        // limit 1值重复了值获取第一条例如："select * from "+tableName+" where "子段名=值,limit 1"
        Cursor cursor = getWritableDatabase().rawQuery("select * from "+ tableName + where,values);
        int count = cursor.getColumnNames().length;//select * from tableName a
        List<List<String>> result = new ArrayList<>();
        while(cursor.moveToNext()){
            List<String> itemData = new ArrayList<>();
            for(int i = 0;i<count;i++)
                itemData.add(cursor.getString(i));
            result.add(itemData);
        }
        return result;
    }
    public List<String> querySql(String tableName,HashMap<String,Object> datas){
        return null;
    }
}