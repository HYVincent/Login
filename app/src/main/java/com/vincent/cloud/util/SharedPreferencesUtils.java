package com.vincent.cloud.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * description ：
 * project name：MyAppProject
 * author : Vincent
 * creation date: 2017/2/21 8:45
 *
 * @version 1.0
 */

public class SharedPreferencesUtils {

    private static  SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private Context mContext;

    public SharedPreferencesUtils(Context context, String name){
        this.mContext = context;
        sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferences.Editor getEditor() {
        if(editor!=null)
            return editor;
        else
            return null;
    }

    /**
     * 写入String类型键值对
     * @param key
     * @param vlaues
     * @return
     */
    public boolean putString(String key,String vlaues){
        editor.putString(key,vlaues);
        boolean result = editor.commit();
        return result;
    }

    /**
     * 写入long
     * @param key
     * @param values
     * @return
     */
    public boolean putLong(String key,Long values){
        editor.putLong(key,values);
        boolean result = editor.commit();
        return result;
    }

    /**
     * 写入int
     * @param key
     * @param values
     * @return
     */
    public boolean putInt(String key,int values){
        editor.putInt(key,values);
        boolean result = editor.commit();
        return result;
    }

    /**
     * 写入boolean
     * @param key
     * @param values
     * @return
     */
    public boolean putBoolean(String key,boolean values){
        editor.putBoolean(key,values);
        boolean result = editor.commit();
        return result;
    }

    /**
     * 写入float
     * @param key
     * @param values
     * @return
     */
    public boolean putFloat(String key,float values){
        editor.putFloat(key,values);
        boolean result = editor.commit();
        return result;
    }

    /**
     * 移除某个值
     * @param key
     * @return
     */
    public boolean removeValues(String key){
        editor.remove(key);
        boolean result = editor.commit();
        return result;
    }

    /**
     * 清理
     * @return
     */
    public boolean clear(){
        editor.clear();
        return editor.commit();
    }

    /**
     * 获取String，默认值
     * @param key
     * @param defaultValues
     * @return
     */
    public String getString(String key,String defaultValues){
        return sharedPreferences.getString(key,defaultValues);
    }

    /**
     * 获取long
     * @param key
     * @return
     */
    public long getLoing(String key){
        return sharedPreferences.getLong(key,0);
    }

    /**
     * 获取int值
     * @param key
     * @return
     */
    public int getInt(String key){
        return sharedPreferences.getInt(key,0);
    }

    /**
     * 获取boolean
     * @param key
     * @return
     */
    public boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key,false);
    }

    /**
     * 获取float值
     * @param key
     * @return
     */
    public float getFloat(String key){
        return sharedPreferences.getFloat(key,0);
    }
}
