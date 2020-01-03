package com.qiyc.daohang;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

/**
 * @Description: java类作用描述
 * @Author: rand
 * @Date: 2019-12-30 19:21
 */
public class MyApp extends Application {
    DaoSession daoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        setMyDataBase();
    }

    private void setMyDataBase() {
        //创建数据库topnews.db"
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "topnews.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
